package dev.imabad.ndi.forge;

import dev.imabad.ndi.api.IConstructHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ConstructHelperForge implements IConstructHelper {
    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> blockEntityType(BiFunction<BlockPos, BlockState, T> factory, Block... blocks) {
        return () -> BlockEntityType.Builder.of(factory::apply, blocks).build(null);
    }
}
