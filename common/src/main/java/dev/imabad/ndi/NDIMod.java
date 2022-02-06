package dev.imabad.ndi;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.InputConstants;
import dev.imabad.ndi.blocks.BlockScreen;
import me.walkerknapp.devolay.Devolay;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.lwjgl.glfw.GLFW;

import java.util.UUID;

public class NDIMod {

    public static final String MOD_ID = "mcndi";

    private static CameraManager cameraManager;
    private static GameRenderHook gameRenderHook;

    public static CameraManager getCameraManager() {
        return cameraManager;
    }
    public static GameRenderHook getGameRenderHook() { return gameRenderHook; }
    private static KeyMapping newCameraKey, removeCameraMap;

    public static void init(){
        System.out.println("Starting Fabric NDI, loading NDI libraries.");
        Devolay.loadLibraries();
        cameraManager = new CameraManager();
        String sourceName = "Player";
        if(Minecraft.getInstance().getUser() != null){
            sourceName = Minecraft.getInstance().getUser().getName();
        }
        gameRenderHook = new GameRenderHook("MC - " + sourceName);
        newCameraKey = new KeyMapping("New Camera", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, "NDI");
        removeCameraMap = new KeyMapping("Remove all Cameras", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F, "NDI");
        Registrar.BLOCK.register("screen", BlockScreen.INSTANCE);
    }

    public static void handleKeybind(Minecraft instance) {
        if(newCameraKey.isDown() && instance.level != null && instance.player != null){
            UUID uuid = UUID.randomUUID();
            CameraEntity armorStandEntity = new CameraEntity(instance.level, new GameProfile(uuid, uuid.toString()));
            armorStandEntity.setPos(instance.player.getX(), instance.player.getY(), instance.player.getZ());
            armorStandEntity.setPacketCoordinates(instance.player.getX(), instance.player.getY(), instance.player.getZ());
            armorStandEntity.absMoveTo(instance.player.getX(), instance.player.getY(), instance.player.getZ(), instance.player.getYRot(), instance.player.getXRot());
            armorStandEntity.setYHeadRot(instance.player.yHeadRot);
            instance.level.putNonPlayerEntity(armorStandEntity.getId(), armorStandEntity);
            newCameraKey.setDown(false);
            cameraManager.cameraEntities.add(armorStandEntity);
        } else if(removeCameraMap.isDown() && instance.level != null && instance.player != null){
            for(Entity ent : cameraManager.cameraEntities){
                instance.level.removeEntity(ent.getId(), Entity.RemovalReason.DISCARDED);
            }
        }
    }

    public static KeyMapping getNewCameraKey() {
        return newCameraKey;
    }

    public static KeyMapping getRemoveCameraMap() {
        return removeCameraMap;
    }
}
