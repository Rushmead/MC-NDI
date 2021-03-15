package dev.imabad.ndi.forge;

import dev.imabad.ndi.NDIMod;
import net.minecraftforge.fml.common.Mod;

@Mod(NDIMod.MOD_ID)
public class NDIModForge {
    public NDIModForge() {
        NDIMod.init();
    }
}
