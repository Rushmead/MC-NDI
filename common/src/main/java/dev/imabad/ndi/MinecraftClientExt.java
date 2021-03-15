package dev.imabad.ndi;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.Minecraft;

public interface MinecraftClientExt {

    void setFramebuffer(RenderTarget fb);

    static MinecraftClientExt from(Minecraft self){
        return (MinecraftClientExt)self;
    }

}
