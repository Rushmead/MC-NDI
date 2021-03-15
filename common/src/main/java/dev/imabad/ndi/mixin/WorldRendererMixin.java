package dev.imabad.ndi.mixin;

import dev.imabad.ndi.CameraEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelRenderer.class)
public class WorldRendererMixin {

    @Shadow @Final protected Minecraft minecraft;

    @Inject(method= "shouldShowEntityOutlines()Z", at =@At("HEAD"), cancellable=true)
    private void canDrawEntityOutlines(CallbackInfoReturnable<Boolean> cr){
        if(minecraft.getCameraEntity() instanceof CameraEntity){
            cr.setReturnValue(false);
            cr.cancel();
        }
    }

    @Redirect(method="renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getEntity()Lnet/minecraft/world/entity/Entity;", ordinal = 3))
    private Entity getFocusedEntity(Camera camera){
        if(minecraft.getCameraEntity() instanceof CameraEntity){
            return minecraft.player;
        }
        return camera.getEntity();
    }

}
