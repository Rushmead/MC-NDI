package dev.imabad.ndi.threads;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.walkerknapp.devolay.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class NDIOutputThread extends Thread {

    public final AtomicReference<DevolayReceiver> receiver;
    private AtomicReference<DevolayVideoFrame> videoFrame;
    public AtomicInteger textureID;
    public boolean running = true;

    public NDIOutputThread(DevolayReceiver receiver) {
        this.receiver = new AtomicReference<>(receiver);
    }

    public void init(){
        RenderSystem.enableTexture();
        int textureID = GlStateManager._genTexture();
        this.textureID.set(textureID);
        RenderSystem.bindTexture(textureID);
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
        RenderSystem.bindTexture(0);
    }

    public void end() {
        this.running = false;
        videoFrame.get().close();
        receiver.get().close();
    }

    @Override
    public void run() {
        DevolayVideoFrame videoFrame = new DevolayVideoFrame();
        DevolayAudioFrame audioFrame = new DevolayAudioFrame();
        DevolayMetadataFrame metadataFrame = new DevolayMetadataFrame();
        this.videoFrame.set(videoFrame);
        while (running) {
            switch(receiver.get().receiveCapture(videoFrame, audioFrame, metadataFrame, 5000)) {
                case VIDEO -> {
                    RenderSystem.recordRenderCall(() -> {
                        RenderSystem.bindTexture(textureID.get());
                        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, videoFrame.getXResolution(), videoFrame.getYResolution(), 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, videoFrame.getData());
                    });
                }
            }
        }
    }
}
