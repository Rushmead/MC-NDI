package dev.imabad.ndi;

import com.mojang.authlib.GameProfile;
import dev.imabad.ndi.screens.NameScreen;
import me.walkerknapp.devolay.DevolayMetadataFrame;
import me.walkerknapp.devolay.DevolaySender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class CameraEntity extends RemotePlayer {

    private float zoom = 0;
    private boolean isLive = false;
    private Component name;

    public CameraEntity(ClientLevel clientWorld, GameProfile gameProfile) {
        super(clientWorld, gameProfile);
        name = new TextComponent(getStringUUID());
    }

    public float getZoom() {
        return zoom;
    }

    @Override
    public boolean isInvisibleTo(Player player) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    @Override
    public boolean isInvisible() {
        return true;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    @Override
    public void setShiftKeyDown(boolean sneaking) {

    }

    public void setName(String text){
        if(this.name.getString().equals(text)){
            return;
        }
        this.name = new TextComponent(text);
        DevolaySender devolaySender = new DevolaySender("MC - " + text);
        DevolayMetadataFrame metadataFrame = new DevolayMetadataFrame();
        metadataFrame.setData("<ndi_capabilities ntk_ptz=\"true\"/>");
        devolaySender.addConnectionMetadata(metadataFrame);
        NDIMod.getCameraManager().cameraControls.get(getUUID()).updateSender(devolaySender);
        NDIMod.getCameraManager().cameras.get(getUUID()).updateSender(devolaySender);
    }

    @Override
    public Component getDisplayName() {
        return name;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        if(Minecraft.getInstance().getCameraEntity() instanceof CameraEntity){
            return false;
        }
        return super.shouldRenderAtSqrDistance(distance);
    }

    @Override
    public boolean isShiftKeyDown() {
        return false;
    }

    @Override
    protected Vec3 maybeBackOffFromEdge(Vec3 movement, MoverType type) {
        return movement;
    }

    @Override
    public boolean isCrouching() {
        return false;
    }

    @Override
    public void remove(RemovalReason removalReason) {
        super.remove(removalReason);
        NDIMod.getCameraManager().cameraControls.get(getUUID()).end();
        NDIMod.getCameraManager().cameras.get(getUUID()).end();
        NDIMod.getCameraManager().cameras.remove(getUUID());
        NDIMod.getCameraManager().cameraEntities.remove(this);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        Minecraft.getInstance().setScreen(new NameScreen(this));
        return super.interact(player, hand);
    }

    @Override
    public InteractionResult interactOn(Entity entity, InteractionHand hand) {
        Minecraft.getInstance().setScreen(new NameScreen(this));
        return InteractionResult.SUCCESS;
    }

    public CompoundTag getTag(){
        CompoundTag tag = new CompoundTag();
        tag.put("pos", this.newDoubleList(this.getX(), this.getY(), this.getZ()));
        tag.put("rotation", this.newFloatList(this.getYRot(), this.getXRot()));
        tag.putString("name", this.name.getContents());
        tag.putString("uuid", this.getStringUUID());
        tag.putFloat("zoom", this.zoom);
        return tag;
    }

    public void cameraFromTag(CompoundTag tag){
        ListTag pos = tag.getList("pos", 6);
        ListTag rotation = tag.getList("rotation", 5);
        this.setPos(pos.getDouble(0), pos.getDouble(1), pos.getDouble(2));
        setYRot(rotation.getFloat(0));
        setXRot(rotation.getFloat(1));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
        this.setYHeadRot(this.yHeadRot);
        this.setYBodyRot(this.yBodyRot);
        this.name = new TextComponent(tag.getString("name"));
        this.zoom = tag.getFloat("zoom");
        this.reapplyPosition();
    }
}
