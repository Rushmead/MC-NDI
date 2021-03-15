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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow @Final private Minecraft minecraft;
    @Shadow private float renderDistance;

    @Inject(method = "getProjectionMatrix(Lnet/minecraft/client/Camera;FZ)Lcom/mojang/math/Matrix4f;", at = @At("HEAD"), cancellable = true)
    private void getProjectionMatrix(Camera camera, float f, boolean bl, CallbackInfoReturnable<Matrix4f> cir) {
        if(!(minecraft.getCameraEntity() instanceof CameraEntity)) return;

        CameraEntity cameraEntity = (CameraEntity) minecraft.getCameraEntity();

        PoseStack matrixStack = new PoseStack();
        matrixStack.last().pose().setIdentity();

        matrixStack.last().pose().multiply(Matrix4f.perspective(70.0f - (cameraEntity.getZoom()), 16 / 9f, 0.05f, renderDistance * 4.0f));
        cir.setReturnValue(matrixStack.last().pose());
    }

}
