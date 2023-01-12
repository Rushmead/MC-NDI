package dev.imabad.ndi.forge;

import dev.imabad.ndi.NDIMod;
import dev.imabad.ndi.Registrar;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(NDIMod.MOD_ID)
public class NDIModForge {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, NDIMod.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, NDIMod.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, NDIMod.MOD_ID);

    public NDIModForge() {
        Registrar.BLOCK = new ForgeRegistrar<>(BLOCKS);
        Registrar.ITEM = new ForgeRegistrar<>(ITEMS);
        Registrar.BLOCK_ENTITY = new ForgeRegistrar<>(BLOCK_ENTITY_TYPES);
        NDIMod.init();
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCK_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegisterKeys);
    }

    public void clientSetup(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.addListener(this::onClientTickEvent);
    }

    public void onRegisterKeys(RegisterKeyMappingsEvent event) {
        event.register(NDIMod.getNewCameraKey());
        event.register(NDIMod.getRemoveCameraMap());
    }

    public void onClientTickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        NDIMod.handleKeybind(Minecraft.getInstance());
    }


    public static class ForgeRegistrar<T > extends Registrar<T> {
        private final DeferredRegister<T> reg;

        private ForgeRegistrar(DeferredRegister<T> reg) {
            this.reg = reg;
        }

        public void register(String id, T obj) {
            reg.register(id, () -> obj);
        }
    }
}
