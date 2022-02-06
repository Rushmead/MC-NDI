package dev.imabad.ndi;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public abstract class Registrar<T> {

    public static Registrar<Block> BLOCK;
    public static Registrar<Item> ITEM;
    public static Registrar<BlockEntityType<?>> BLOCK_ENTITY;

    abstract public void register(String name, T value);
}
