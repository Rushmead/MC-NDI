package dev.imabad.ndi.fabric;

import dev.imabad.ndi.api.IConstructHelper;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ConstructHelperFabric implements IConstructHelper {
    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> blockEntityType(BiFunction<BlockPos, BlockState, T> factory, Block... blocks) {
        return () -> FabricBlockEntityTypeBuilder.create(factory::apply, blocks).build();
    }
}
