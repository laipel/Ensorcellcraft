package com.laipel.ensorcellcraft.api.soul;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public interface IWorldSoulContainer extends ISoulContainer {

    double getX();

    double getY();

    double getZ();

    Level getLevel();

    static IWorldSoulContainer of(LivingEntity livingEntity) {
        return (IWorldSoulContainer) livingEntity;
    }

}
