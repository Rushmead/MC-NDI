package dev.imabad.ndi.mixin;

import dev.imabad.ndi.CameraEntity;
import dev.imabad.ndi.CameraExt;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin implements CameraExt {
    @Shadow private Entity entity;

    @Shadow private float eyeHeight;

    @Shadow private float eyeHeightOld;

    @Override
    public void setCameraY(float cameraY) {
        this.eyeHeight = cameraY;
        this.eyeHeightOld = cameraY;
    }

    @Override
    public float getCameraY() {
        return this.eyeHeight;
    }

    @Inject(method = "tick()V", at = @At("HEAD"), cancellable = true)
    public void updateEyeHeight(CallbackInfo ci) {
        if(this.entity != null && this.entity instanceof CameraEntity){
            ci.cancel();
        }
    }
}
