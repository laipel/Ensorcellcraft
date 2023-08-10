package com.laipel.ensorcellcraft.api.soul;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public interface IPositionedSoulContainer extends ISoulContainer {

    double getX();

    double getY();

    double getZ();

    Level getLevel();

    static IPositionedSoulContainer of(LivingEntity livingEntity) {
        return (IPositionedSoulContainer) livingEntity;
    }

}
