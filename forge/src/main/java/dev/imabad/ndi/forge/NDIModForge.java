package dev.imabad.ndi.forge;

import dev.imabad.ndi.NDIMod;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(NDIMod.MOD_ID)
public class NDIModForge {
    public NDIModForge() {
        NDIMod.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    public void clientSetup(FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(NDIMod.getNewCameraKey());
        ClientRegistry.registerKeyBinding(NDIMod.getRemoveCameraMap());
        MinecraftForge.EVENT_BUS.addListener(this::onClientTickEvent);
    }

    public void onClientTickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        NDIMod.handleKeybind(Minecraft.getInstance());
    }
}
