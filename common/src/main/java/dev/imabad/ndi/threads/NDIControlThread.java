package dev.imabad.ndi.threads;

import com.walker.devolay.DevolayFrameType;
import com.walker.devolay.DevolayMetadataFrame;
import com.walker.devolay.DevolaySender;
import com.walker.devolay.DevolayTally;
import dev.imabad.ndi.CameraEntity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.minecraft.util.Mth;
import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.atomic.AtomicReference;

public class NDIControlThread extends Thread {

    public boolean running = true;

    public final AtomicReference<DevolaySender> sender;
    private CameraEntity entity;

    public NDIControlThread(DevolaySender sender, CameraEntity entity){
        this.sender = new AtomicReference<>(sender);
        this.entity = entity;
    }

    public void updateSender(DevolaySender sender){
        this.sender.set(sender);
    }

    public void end() {
        running = false;
    }

    @Override
    public void run() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        while(running){
            if(sender.get().getConnectionCount(0) < 1){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            DevolayMetadataFrame metadataFrame = new DevolayMetadataFrame();
            if (sender.get().receiveCapture(metadataFrame, 100) == DevolayFrameType.METADATA) {
                try {
                    Document doc = db.parse(new InputSource(new StringReader(metadataFrame.getData())));
                    String type = doc.getFirstChild().getNodeName();
                    if(type.equals("ntk_ptz_pan_tilt_speed")){
                        Element element = (Element) doc.getFirstChild();
                        float panSpeed = Float.parseFloat(element.getAttribute("pan_speed"));
                        float tiltSpeed = Float.parseFloat(element.getAttribute("tilt_speed"));
                        float tilt = 5 * tiltSpeed;
                        float pan = 5 * panSpeed;
                        float pitch = entity.xRot - tilt;
                        float yaw = entity.yRot - pan;
                        entity.setYHeadRot(yaw);
                        entity.absMoveTo(entity.getX(), entity.getY(), entity.getZ(), yaw, pitch);
                    } else if(type.equals("ntk_ptz_zoom_speed")){
                        Element element = (Element) doc.getFirstChild();
                        float zoomSpeed = Float.parseFloat(element.getAttribute("zoom_speed"));
                        float fov = 2 * zoomSpeed;
                        int newZoom = (int) Mth.clamp((entity.getZoom() + fov), -50f, 69f);
                        entity.setZoom(newZoom);
                    }
                } catch (SAXException | IOException e) {
                    e.printStackTrace();
                }
            }
            DevolayTally tally = sender.get().getTally(0);
            if(tally != null){
                entity.setLive(tally.isOnProgram());
            }
        }
    }
}
