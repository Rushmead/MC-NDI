package dev.imabad.ndi.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import dev.imabad.ndi.CameraEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow @Final private Minecraft minecraft;
    @Shadow private float renderDistance;

    @Inject(method = "getProjectionMatrix(D)Lcom/mojang/math/Matrix4f;", at = @At("HEAD"), cancellable = true)
    private void getProjectionMatrix(double $$0, CallbackInfoReturnable<Matrix4f> cir) {
        if(!(minecraft.getCameraEntity() instanceof CameraEntity)) return;

        CameraEntity cameraEntity = (CameraEntity) minecraft.getCameraEntity();
        PoseStack $$1 = new PoseStack();
        $$1.last().pose().setIdentity();
        if (cameraEntity.getZoom() != 0.0F) {
            $$1.scale(cameraEntity.getZoom(), cameraEntity.getZoom(), 1.0F);
        }

        $$1.last().pose().multiply(Matrix4f.perspective(70f, (float)this.minecraft.getWindow().getWidth() / (float)this.minecraft.getWindow().getHeight(), 0.05F, renderDistance * 4.0f));
        cir.setReturnValue($$1.last().pose());
    }


}
