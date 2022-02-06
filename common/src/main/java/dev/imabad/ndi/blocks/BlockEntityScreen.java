package dev.imabad.ndi.blocks;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityScreen extends BlockEntity {
    public static final BlockEntityType<BlockEntityScreen> TYPE = BlockEntityType.Builder.of(BlockEntityScreen::new).build(null);
}
