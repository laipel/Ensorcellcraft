package com.laipel.ensorcellcraft.mixins;

import com.laipel.ensorcellcraft.api.soul.*;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Implements({@Interface(iface = IWorldSoulContainer.class, prefix = "i$")})
@Mixin(LivingEntity.class)
public abstract class MobMixin extends Entity {

    private MobMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot p_21467_);

    @Shadow protected abstract Brain<?> makeBrain(Dynamic<?> p_21069_);

    @Nullable private Pair<IItemSoulContainer, ItemStack> getSoulContainerInMainHand() {
        ItemStack stack = getItemBySlot(EquipmentSlot.MAINHAND);
        if (stack.getItem() instanceof IItemSoulContainer container)
            return Pair.of(container, stack);
        return null;
    }

    @Nullable private Pair<IItemSoulContainer, ItemStack> getSoulContainerInOffHand() {
        ItemStack stack = getItemBySlot(EquipmentSlot.OFFHAND);
        if (stack.getItem() instanceof IItemSoulContainer container)
            return Pair.of(container, stack);
        return null;
    }

    public int i$addSoul(ISoul soul, int amount) {
        Pair<IItemSoulContainer, ItemStack> mainContainer = this.getSoulContainerInMainHand();
        if (mainContainer != null)
            amount = mainContainer.getKey().addSoul(soul, amount, mainContainer.getValue());

        if (amount == 0)
            return 0;

        Pair<IItemSoulContainer, ItemStack> offContainer = this.getSoulContainerInOffHand();
        if (offContainer == null)
            return amount;
        return offContainer.getKey().addSoul(soul, amount, offContainer.getValue());
    }

    @NotNull
    public SoulList i$addSouls(SoulList soulList) {
        Pair<IItemSoulContainer, ItemStack> mainContainer = this.getSoulContainerInMainHand();
        if (mainContainer != null)
            soulList = mainContainer.getKey().addSouls(soulList, mainContainer.getValue());

        if (soulList.hasNoSouls())
            return soulList;

        Pair<IItemSoulContainer, ItemStack> offContainer = this.getSoulContainerInOffHand();
        if (offContainer == null)
            return soulList;
        return offContainer.getKey().addSouls(soulList, offContainer.getValue());
    }

    public int i$receiveSoul(ISoul soul, int amount) {
        Pair<IItemSoulContainer, ItemStack> mainContainer = this.getSoulContainerInMainHand();
        if (mainContainer != null)
            amount = mainContainer.getKey().receiveSoul(soul, amount, mainContainer.getValue());

        if (amount == 0)
            return 0;

        Pair<IItemSoulContainer, ItemStack> offContainer = this.getSoulContainerInOffHand();
        if (offContainer == null)
            return amount;
        return offContainer.getKey().receiveSoul(soul, amount, offContainer.getValue());
    }

    @NotNull
    public SoulList i$receiveSouls(SoulList soulList) {
        Pair<IItemSoulContainer, ItemStack> mainContainer = this.getSoulContainerInMainHand();
        if (mainContainer != null)
            soulList = mainContainer.getKey().receiveSouls(soulList, mainContainer.getValue());

        if (soulList.hasNoSouls())
            return soulList;

        Pair<IItemSoulContainer, ItemStack> offContainer = this.getSoulContainerInOffHand();
        if (offContainer == null)
            return soulList;
        return offContainer.getKey().receiveSouls(soulList, offContainer.getValue());
    }

    public boolean i$canAbsorbSouls(SoulList soulList) {
        Pair<IItemSoulContainer, ItemStack> mainContainer = this.getSoulContainerInMainHand();
        if (mainContainer != null && mainContainer.getKey().canAbsorbSouls(soulList, mainContainer.getValue()))
            return true;

        Pair<IItemSoulContainer, ItemStack> offContainer = this.getSoulContainerInOffHand();
        return offContainer != null && offContainer.getKey().canAbsorbSouls(soulList, offContainer.getValue());
    }

    public boolean i$canAbsorbSoul(ISoul soul, int amount) {
        Pair<IItemSoulContainer, ItemStack> mainContainer = this.getSoulContainerInMainHand();
        if (mainContainer != null && mainContainer.getKey().canAbsorbSoul(soul, amount, mainContainer.getValue()))
            return true;

        Pair<IItemSoulContainer, ItemStack> offContainer = this.getSoulContainerInOffHand();
        return offContainer != null && offContainer.getKey().canAbsorbSoul(soul, amount, offContainer.getValue());
    }

    public boolean i$hasSouls(SoulList soulList) {
        Pair<IItemSoulContainer, ItemStack> mainContainer = this.getSoulContainerInMainHand();
        if (mainContainer != null && mainContainer.getKey().hasSouls(soulList, mainContainer.getValue()))
            return true;

        Pair<IItemSoulContainer, ItemStack> offContainer = this.getSoulContainerInOffHand();
        return offContainer != null && offContainer.getKey().hasSouls(soulList, offContainer.getValue());
    }

    public boolean i$hasSoul(ISoul soul, int amount) {
        Pair<IItemSoulContainer, ItemStack> mainContainer = this.getSoulContainerInMainHand();
        if (mainContainer != null && mainContainer.getKey().hasSoul(soul, amount, mainContainer.getValue()))
            return true;

        Pair<IItemSoulContainer, ItemStack> offContainer = this.getSoulContainerInOffHand();
        return offContainer != null && offContainer.getKey().hasSoul(soul, amount, offContainer.getValue());
    }

    @NotNull
    public SoulList i$getSouls() {
        SoulList soulList = new SoulList();
        Pair<IItemSoulContainer, ItemStack> mainContainer = this.getSoulContainerInMainHand();
        if (mainContainer != null)
            soulList.add(mainContainer.getKey().getSouls(mainContainer.getValue()));

        if (soulList.hasNoSouls())
            return soulList;

        Pair<IItemSoulContainer, ItemStack> offContainer = this.getSoulContainerInOffHand();
        if (offContainer == null)
            return soulList;
        soulList.add(offContainer.getKey().getSouls(offContainer.getValue()));
        return soulList;
    }

    public void i$clear() {
        Pair<IItemSoulContainer, ItemStack> mainContainer = this.getSoulContainerInMainHand();
        if (mainContainer != null)
            mainContainer.getKey().clear(mainContainer.getValue());

        Pair<IItemSoulContainer, ItemStack> offContainer = this.getSoulContainerInOffHand();
        if (offContainer != null)
            offContainer.getKey().clear(offContainer.getValue());
    }

    public double i$getX() {
        return this.getX();
    }

    public double i$getY() {
        return this.getY();
    }

    public double i$getZ() {
        return this.getZ();
    }

    public Level i$getLevel() {
        return this.level;
    }
}
