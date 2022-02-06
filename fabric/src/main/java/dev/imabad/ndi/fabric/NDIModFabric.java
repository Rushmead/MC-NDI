package dev.imabad.ndi.fabric;

import dev.imabad.ndi.NDIMod;
import dev.imabad.ndi.Registrar;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

public class NDIModFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Registrar.BLOCK = new FabricRegistrar<>(Registry.BLOCK);
        Registrar.ITEM = new FabricRegistrar<>(Registry.ITEM);
        Registrar.BLOCK_ENTITY = new FabricRegistrar<>(Registry.BLOCK_ENTITY_TYPE);
        NDIMod.init();
        KeyBindingHelper.registerKeyBinding(NDIMod.getNewCameraKey());
        KeyBindingHelper.registerKeyBinding(NDIMod.getRemoveCameraMap());
        ClientTickEvents.END_CLIENT_TICK.register(NDIMod::handleKeybind);
    }

    public static class FabricRegistrar<T> extends Registrar<T> {
        private final Registry<T> registry;

        public FabricRegistrar(Registry<T> registry) {
            this.registry = registry;
        }

        @Override
        public void register(String name, T value) {
            Registry.register(registry, new ResourceLocation(NDIMod.MOD_ID, name), value);
        }
    }
}
