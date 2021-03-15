package dev.imabad.ndi.fabric;

import dev.imabad.ndi.NDIMod;
import net.fabricmc.api.ModInitializer;

public class NDIModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        NDIMod.init();
    }
}
