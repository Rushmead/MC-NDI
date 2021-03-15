package dev.imabad.ndi.threads;

import com.walker.devolay.DevolayFrameFourCCType;
import com.walker.devolay.DevolaySender;
import com.walker.devolay.DevolayVideoFrame;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class NDIThread extends Thread {

    public final AtomicReference<DevolaySender> sender;
    private AtomicReference<ByteBuffer> byteBuffer;
    private AtomicReference<DevolayVideoFrame> videoFrame;
    private AtomicBoolean needsFrame, hasFlipped;
    public AtomicInteger width, height;
    public boolean running = true;

    public NDIThread(DevolaySender sender, ByteBuffer image, int width, int height){
        this.sender = new AtomicReference<>(sender);
        byteBuffer = new AtomicReference<>(image);
        videoFrame = new AtomicReference<>();
        this.width = new AtomicInteger(width);
        this.height = new AtomicInteger(height);
        needsFrame = new AtomicBoolean(true);
        hasFlipped = new AtomicBoolean(false);
        DevolayVideoFrame videoFrame1 = new DevolayVideoFrame();
        videoFrame1.setResolution(width, height);
        videoFrame1.setFourCCType(DevolayFrameFourCCType.RGBX);
        videoFrame1.setLineStride(width * 4);
        videoFrame1.setFrameRate(30, 1);
        videoFrame.set(videoFrame1);
    }

    public void updateSender(DevolaySender sender){
        this.sender.get().close();
        this.sender.set(sender);
    }

    public void updateVideoFrame(int width, int height){
        this.width.set(width);
        this.height.set(height);
        DevolayVideoFrame videoFrame1 = videoFrame.get();
        videoFrame1.setResolution(width, height);
        videoFrame1.setLineStride(width * 4);
        videoFrame.set(videoFrame1);
    }

    public void setByteBuffer(ByteBuffer buffer){
        this.byteBuffer.set(buffer);
        this.needsFrame.set(false);
        this.hasFlipped.set(false);
    }

    public AtomicBoolean getNeedsFrame(){
        return needsFrame;
    }

    public void end(){
        this.running = false;
        videoFrame.get().close();
        sender.get().close();
    }

    @Override
    public void run() {
        int frameCounter = 0;
        long fpsPeriod = System.currentTimeMillis();
        long lastFrame = System.currentTimeMillis();
        float amountofTime = (1f / 30f) * 1000;
        while(running) {
            long timeSinceLastFrame = System.currentTimeMillis() - lastFrame;
            if(timeSinceLastFrame < amountofTime){
                try {
                    Thread.sleep((long) (amountofTime - timeSinceLastFrame));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            if(sender.get().getConnectionCount(0) < 1){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            if(needsFrame.get()){
                continue;
            }
            DevolayVideoFrame frame = videoFrame.get();
            ByteBuffer buffer = byteBuffer.get();
            frame.setData(buffer);
            this.sender.get().sendVideoFrame(frame);
            needsFrame.set(true);
            lastFrame = System.currentTimeMillis();
            if(frameCounter % 30 == 29) {
                long timeSpent = System.currentTimeMillis() - fpsPeriod;
                System.out.println("Sent 30 frames. Average FPS: " + 30f / (timeSpent / 1000f));
                fpsPeriod = System.currentTimeMillis();
            }

            frameCounter++;
        }
    }
}
