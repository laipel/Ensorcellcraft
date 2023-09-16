package com.laipel.ensorcellcraft.api.soul;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class StaticPositionSoulContainer implements IWorldSoulContainer {

    final ISoulContainer container;
    final Vec3 position;
    final Level level;

    public StaticPositionSoulContainer(ISoulContainer container, Vec3 vec, Level level) {
        this.container = container;
        this.position = vec;
        this.level = level;
    }

    @Override
    public double getX() {
        return position.x;
    }

    @Override
    public double getY() {
        return position.y;
    }

    @Override
    public double getZ() {
        return position.z;
    }

    @Override
    public Level getLevel() {
        return level;
    }

    @Override
    public int addSoul(ISoul soul, int amount) {
        return container.addSoul(soul, amount);
    }

    @NotNull
    @Override
    public SoulList addSouls(SoulList soulList) {
        return container.addSouls(soulList);
    }

    @Override
    public int receiveSoul(ISoul soul, int amount) {
        return container.receiveSoul(soul, amount);
    }

    @NotNull
    @Override
    public SoulList receiveSouls(SoulList soulList) {
        return container.receiveSouls(soulList);
    }

    @Override
    public boolean canAbsorbSouls(SoulList soulList) {
        return container.canAbsorbSouls(soulList);
    }

    @Override
    public boolean canAbsorbSoul(ISoul soul, int amount) {
        return container.canAbsorbSoul(soul, amount);
    }

    @Override
    public boolean hasSouls(SoulList soulList) {
        return container.hasSouls(soulList);
    }

    @Override
    public boolean hasSoul(ISoul soul, int amount) {
        return container.hasSoul(soul, amount);
    }

    @NotNull
    @Override
    public SoulList getSouls() {
        return container.getSouls();
    }

    @Override
    public void clear() {
        container.clear();
    }
}
