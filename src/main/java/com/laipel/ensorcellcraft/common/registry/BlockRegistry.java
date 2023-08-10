package com.laipel.ensorcellcraft.common.registry;

import com.laipel.ensorcellcraft.Ensorcellcraft;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Ensorcellcraft.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Ensorcellcraft.MODID);

    public static void register() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());

        for (RegistryObject<? extends Block> block : BLOCKS.getEntries())
            ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));

        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}