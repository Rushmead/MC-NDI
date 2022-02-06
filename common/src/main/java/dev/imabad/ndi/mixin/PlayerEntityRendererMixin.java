package dev.imabad.ndi.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.imabad.ndi.CameraEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public PlayerEntityRendererMixin(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> model, float someFloat) {
        super(context, model, someFloat);
    }

    @Inject(method= "setModelProperties(Lnet/minecraft/client/player/AbstractClientPlayer;)V", at=@At("HEAD"), cancellable = true)
    public void setModelPose(AbstractClientPlayer abstractClientPlayerEntity, CallbackInfo callbackInfo){
        PlayerModel<AbstractClientPlayer> playerEntityModel = this.getModel();
        if(abstractClientPlayerEntity instanceof CameraEntity){
            playerEntityModel.setAllVisible(false);
            playerEntityModel.head.visible = true;
            playerEntityModel.crouching = false;
            playerEntityModel.hat.visible = true;
            callbackInfo.cancel();
        }
    }

    @Inject(method= "renderNameTag(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/network/chat/Component;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at=@At("HEAD"))
    public void renderLabelIfPresent(AbstractClientPlayer abstractClientPlayerEntity, Component text, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, CallbackInfo callbackInfo){
        if(abstractClientPlayerEntity instanceof CameraEntity) {
            CameraEntity cameraEntity = (CameraEntity) abstractClientPlayerEntity;
            if(cameraEntity.isLive()){
                matrixStack.pushPose();
                matrixStack.translate(0, 0.2f, 0);
                super.renderNameTag(abstractClientPlayerEntity, new TextComponent(ChatFormatting.RED + "● LIVE ●"), matrixStack, vertexConsumerProvider, i);
                matrixStack.popPose();
            }
        }
    }
}
