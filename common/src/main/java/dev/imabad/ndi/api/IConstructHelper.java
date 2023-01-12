package dev.imabad.ndi.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public interface IConstructHelper {

    <T extends BlockEntity> Supplier<BlockEntityType<T>> blockEntityType(BiFunction<BlockPos, BlockState, T> factory, Block... blocks);

}
