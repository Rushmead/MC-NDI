package dev.imabad.ndi.blocks;

import dev.imabad.ndi.Registrar;
import dev.imabad.ndi.api.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityScreen extends BlockEntity {
    public static final BlockEntityType<BlockEntityScreen> TYPE = Services.CONSTRUCTS.blockEntityType(BlockEntityScreen::new, BlockScreen.INSTANCE).get();

    public BlockEntityScreen(BlockPos $$0, BlockState $$1) {
        super(TYPE, $$0, $$1);
    }

}
