package dev.imabad.ndi.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.imabad.ndi.CameraEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

import java.awt.*;

public class NameScreen extends Screen {

    private CameraEntity cameraEntity;
    private EditBox nameField;
    private EditBox zoomField;
    private Button deleteButton;

    public NameScreen(CameraEntity cameraEntity) {
        super(Component.literal("Edit Camera"));
        this.cameraEntity = cameraEntity;
    }

    protected void init() {
        super.init();
        int i = this.width / 2;
        int j = this.height / 2;
        this.nameField = new EditBox(this.font, i - 75, j - 10, 150, 20, Component.translatable("container.repair"));
        this.nameField.setValue(cameraEntity.getDisplayName().getString());
        this.nameField.setFocus(true);
        this.nameField.setCanLoseFocus(false);
        this.nameField.changeFocus(true);
        this.nameField.setMaxLength(35);
        this.addWidget(this.nameField);
        this.zoomField = new EditBox(this.font, i - 75, j + 20, 150, 20, Component.translatable("container.repair"));
        this.zoomField.setValue(cameraEntity.getZoom() + "");
        this.zoomField.setFocus(true);
        this.zoomField.setCanLoseFocus(false);
        this.zoomField.changeFocus(true);
        this.zoomField.setMaxLength(35);
        this.setInitialFocus(this.zoomField);
        this.deleteButton = new PlainTextButton(i - 20, j + 50, 40, 20, Component.literal("Delete"), this::buttonClick, Minecraft.getInstance().font);
        this.addWidget(this.deleteButton);
    }

    public void buttonClick(Button buttonWidget){
        this.cameraEntity.remove(Entity.RemovalReason.KILLED);
        this.minecraft.player.clientSideCloseContainer();
    }

    public void resize(Minecraft client, int width, int height) {
        String string = this.nameField.getValue();
        String zoom = this.zoomField.getValue();
        this.init(client, width, height);
        this.nameField.setValue(string);
        this.zoomField.setValue(zoom);
    }

    public void removed() {
        super.removed();
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            if (!this.nameField.getValue().isEmpty()) {
                String string = this.nameField.getValue();
                cameraEntity.setName(string);
            }
            if(!this.nameField.getValue().isEmpty()) {
                String zoom = this.zoomField.getValue();
                cameraEntity.setZoom(Float.parseFloat(zoom));
            }
            this.minecraft.player.clientSideCloseContainer();
        }

        return !this.nameField.keyPressed(keyCode, scanCode, modifiers) && !this.nameField.canConsumeInput() ? super.keyPressed(keyCode, scanCode, modifiers) : true;
    }

    public void render(PoseStack matrixStack, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, delta);
        RenderSystem.disableBlend();
        this.nameField.render(matrixStack, mouseX, mouseY, delta);
        this.zoomField.render(matrixStack, mouseX, mouseY, delta);
        this.deleteButton.render(matrixStack, mouseX, mouseY, delta);
        this.font.draw(matrixStack, this.title.getString(), (this.width / 2) - (this.font.width(this.title.getString()) / 2), (this.height / 2) - 30, 0xffffff);
    }


}
