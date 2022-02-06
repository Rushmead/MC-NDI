package dev.imabad.ndi.fabric;

import dev.imabad.ndi.NDIMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class NDIModFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        NDIMod.init();
        KeyBindingHelper.registerKeyBinding(NDIMod.getNewCameraKey());
        KeyBindingHelper.registerKeyBinding(NDIMod.getRemoveCameraMap());
        ClientTickEvents.END_CLIENT_TICK.register(NDIMod::handleKeybind);
    }
}
