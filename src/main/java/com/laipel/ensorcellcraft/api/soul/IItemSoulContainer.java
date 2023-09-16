package com.laipel.ensorcellcraft.api.soul;

import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public interface IItemSoulContainer {

    int addSoul(ISoul soul, int amount, ItemStack stack);

    @Nonnull
    SoulList addSouls(SoulList soulList, ItemStack stack);

    int receiveSoul(ISoul soul, int amount, ItemStack stack);

    @Nonnull SoulList receiveSouls(SoulList soulList, ItemStack stack);

    boolean canAbsorbSouls(SoulList soulList, ItemStack stack);

    boolean canAbsorbSoul(ISoul soul, int amount, ItemStack stack);

    boolean hasSouls(SoulList soulList, ItemStack stack);

    boolean hasSoul(ISoul soul, int amount, ItemStack stack);

    @Nonnull SoulList getSouls(ItemStack stack);

    void clear(ItemStack stack);

}
