package dev.imabad.ndi.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.imabad.ndi.CameraEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class NameScreen extends Screen {

    private CameraEntity cameraEntity;
    private EditBox nameField;

    public NameScreen(CameraEntity cameraEntity) {
        super(new TextComponent("Edit Camera"));
        this.cameraEntity = cameraEntity;
    }

    protected void init() {
        super.init();
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        int i = this.width / 2;
        int j = this.height / 2;
        this.nameField = new EditBox(this.font, i - 75, j - 10, 150, 20, new TranslatableComponent("container.repair"));
        this.nameField.setValue(cameraEntity.getDisplayName().getContents());
        this.nameField.setFocus(true);
        this.nameField.setCanLoseFocus(false);
        this.nameField.changeFocus(true);
        this.nameField.setMaxLength(35);
        this.children.add(this.nameField);
        this.setInitialFocus(this.nameField);
        this.addButton(new Button(i - 20, j + 20, 40, 20, new TextComponent("Delete"), this::buttonClick));
    }

    public void buttonClick(Button buttonWidget){
        this.cameraEntity.remove();
        this.minecraft.player.clientSideCloseContainer();
    }

    public void resize(Minecraft client, int width, int height) {
        String string = this.nameField.getValue();
        this.init(client, width, height);
        this.nameField.setValue(string);
    }

    public void removed() {
        super.removed();
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            if (!this.nameField.getValue().isEmpty()) {
                String string = this.nameField.getValue();
                cameraEntity.setName(string);
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
        this.font.draw(matrixStack, this.title.getContents(), (this.width / 2) - (this.font.width(this.title.getString()) / 2), (this.height / 2) - 30, 0xffffff);
    }


}
