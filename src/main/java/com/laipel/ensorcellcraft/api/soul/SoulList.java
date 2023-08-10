package com.laipel.ensorcellcraft.api.soul;

import com.google.common.collect.ImmutableMap;
import com.laipel.ensorcellcraft.common.registry.SoulRegistry;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class SoulList extends HashMap<ISoul, Integer> implements INBTSerializable<CompoundTag> {

    public SoulList(ISoul soul, int amount) {
        super(ImmutableMap.of(soul, amount));
    }

    public SoulList(@NotNull Map<ISoul, Integer> map) {
        super(map);
    }

    public SoulList() {}

    public int getUsedSpace() {
        return this.values().stream().mapToInt(e -> e).sum();
    }

    public SoulList copy() {
        return new SoulList(this);
    }

    public void add(ISoul soul, int value) {
        this.put(soul, this.get(soul) + value);
    }

    public void add(@NotNull SoulList list) {
        list.forEach((s, v) -> this.put(s, this.get(s) + v));
    }

    public int subtract(ISoul soul, int value) {
        int before = this.get(soul);
        int newValue = Math.max(before - value, 0);
        this.put(soul, value);
        return newValue - (before - value);
    }

    public SoulList subtract(@NotNull SoulList list) {
        list.replaceAll((s, v) -> {
            int before = this.get(s);
            int newValue = Math.max(before - v, 0);
            this.put(s, v);
            return newValue - (before - v);
        });
        list.values().removeIf(e -> e == 0);
        return list;
    }

    public boolean has(ISoul soul, int num) {
        return this.get(soul) >= num;
    }

    public boolean has(@NotNull SoulList soulList) {
        return soulList.entrySet().stream().reduce(false,
                (b, entry) -> b && this.get(entry.getKey()) > entry.getValue(), (b1, b2) -> b1 && b2);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        this.forEach((s, v) -> {
            ResourceLocation rl = SoulRegistry.SOUL_REGISTRY.get().getKey(s);
            if (rl == null) {
                LOGGER.error(String.format("Failed to get registry name of %s.", s.toString()));
                return;
            }
            tag.putInt(rl.toString(), v);
        });
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        nbt.getAllKeys().forEach(key -> {
            ResourceLocation rl = new ResourceLocation(key);
            int value = nbt.getInt(key);
            ISoul soul = SoulRegistry.SOUL_REGISTRY.get().getValue(rl);
            this.put(soul, value);
        });
    }

    public boolean hasNoSouls() {
        return this.getUsedSpace() == 0;
    }

    public boolean contains(@NotNull SoulList soulList) {
        return soulList.entrySet().stream().allMatch(e -> this.containsKey(e.getKey())
            && e.getValue() <= this.get(e.getKey()));
    }

    private static final Logger LOGGER = LogUtils.getLogger();

}
