package com.laipel.ensorcellcraft.api.soul;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class LimitedSoulList extends SoulList {

    int maxSpace;

    public LimitedSoulList(int maxSpace) {
        this.maxSpace = maxSpace;
    }

    public LimitedSoulList(ISoul soul, int amount, int maxSpace) {
        super(soul, amount);
        this.maxSpace = maxSpace;
    }

    public LimitedSoulList(Map<ISoul, Integer> map, int maxSpace) {
        super(map);
        this.maxSpace = maxSpace;
    }

    public int getMaxSpace() {
        return maxSpace;
    }

    public void setMaxSpace(int maxSpace) {
        this.maxSpace = maxSpace;
    }

    public SoulList getRemainder(@NotNull SoulList soulList) {
        return soulList.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue() - e1.getValue())
                .reduce(new SoulList(),
                        (souls, e) -> {
                            souls.put(e.getKey(), getRemainder(e.getKey(), e.getValue()));
                            return souls;
                        },
                        (s1, s2) -> {
                            s1.add(s2);
                            return s1;
                        });
    }

    public int getRemainder(@NotNull ISoul soul, int value) {
        int usedSpace = this.getUsedSpace();
        int volume = soul.gerVolume() * value;
        int remained = maxSpace - usedSpace;
        return Math.max(volume - remained, 0) / soul.gerVolume();
    }

    @Override
    public void add(@NotNull SoulList soulList) {
        soulList.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue() - e1.getValue())
                .forEachOrdered(entry -> this.add(entry.getKey(), entry.getValue()));
        super.add(soulList);
    }

    @Override
    public void add(@NotNull ISoul soul, int value) {
        int usedSpace = this.getUsedSpace();
        int volume = soul.gerVolume() * value;
        int remained = maxSpace - usedSpace;
        if (volume > remained) {
            int canAbsorb = (volume - remained) / soul.gerVolume();
            super.add(soul, canAbsorb);
        } else
            super.add(soul, value);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.putInt("space", maxSpace);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        maxSpace = nbt.getInt("space");
        nbt.remove("space");
        super.deserializeNBT(nbt);
    }

    @Override
    public SoulList copy() {
        return new LimitedSoulList(this, maxSpace);
    }
}
