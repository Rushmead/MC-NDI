package dev.imabad.ndi;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.InputConstants;
import com.walker.devolay.Devolay;
import me.shedaniel.architectury.event.events.client.ClientTickEvent;
import me.shedaniel.architectury.registry.KeyBindings;
import me.shedaniel.architectury.registry.Registries;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.util.LazyLoadedValue;
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
        KeyBindings.registerKeyBinding(newCameraKey);
        KeyBindings.registerKeyBinding(removeCameraMap);
        ClientTickEvent.CLIENT_PRE.register(instance -> {
            if(newCameraKey.isDown() && instance.level != null && instance.player != null){
                UUID uuid = UUID.randomUUID();
                CameraEntity armorStandEntity = new CameraEntity(instance.level, new GameProfile(uuid, uuid.toString()));
                armorStandEntity.setPosAndOldPos(instance.player.getX(), instance.player.getY(), instance.player.getZ());
                armorStandEntity.setPacketCoordinates(instance.player.getX(), instance.player.getY(), instance.player.getZ());
                armorStandEntity.absMoveTo(instance.player.getX(), instance.player.getY(), instance.player.getZ(), instance.player.yRot, instance.player.xRot);
                armorStandEntity.setYHeadRot(instance.player.yHeadRot);
                instance.level.putNonPlayerEntity(armorStandEntity.getId(), armorStandEntity);
                newCameraKey.setDown(false);
                cameraManager.cameraEntities.add(armorStandEntity);
            } else if(removeCameraMap.isDown() && instance.level != null && instance.player != null){
                for(Entity ent : cameraManager.cameraEntities){
                    instance.level.removeEntity(ent.getId());
                }
            }
        });
    }

}
