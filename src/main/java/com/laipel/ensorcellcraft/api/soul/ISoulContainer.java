package com.laipel.ensorcellcraft.api.soul;

import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nonnull;

public interface ISoulContainer {

    int addSoul(ISoul soul, int amount);

    @Nonnull SoulList addSouls(SoulList soulList);

    int receiveSoul(ISoul soul, int amount);

    @Nonnull SoulList receiveSouls(SoulList soulList);

    boolean canAbsorbSouls(SoulList soulList);

    boolean canAbsorbSoul(ISoul soul, int amount);

    boolean hasSouls(SoulList soulList);

    boolean hasSoul(ISoul soul, int amount);

    @Nonnull SoulList getSouls();

    void clear();

}
