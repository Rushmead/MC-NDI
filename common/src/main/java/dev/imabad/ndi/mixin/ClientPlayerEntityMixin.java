package dev.imabad.ndi.mixin;

import dev.imabad.ndi.CameraEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class ClientPlayerEntityMixin {

    @Shadow @Final protected Minecraft minecraft;

    @Inject(method= "isLocalPlayer()Z", at =@At("HEAD"), cancellable=true)
    private void isLocalPlayer(CallbackInfoReturnable<Boolean> cr){
        if(minecraft.getCameraEntity() instanceof CameraEntity){
            cr.setReturnValue(false);
            cr.cancel();
        }
    }

}
