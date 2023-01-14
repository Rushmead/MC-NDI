package dev.imabad.ndi.mixin;

import dev.imabad.ndi.CameraEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.sounds.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundManager.class)
public class SoundManagerMixin {

    @Inject(method = "updateSource(Lnet/minecraft/client/Camera;)V", at = @At("HEAD"), cancellable = true)
    public void injectUpdateSource(Camera $$0, CallbackInfo ci) {
        if($$0.getEntity() instanceof CameraEntity) ci.cancel();
    }

}
