package com.laipel.ensorcellcraft.mixins;

import com.laipel.ensorcellcraft.api.soul.IPositionedSoulContainer;
import com.laipel.ensorcellcraft.api.soul.ISoul;
import com.laipel.ensorcellcraft.api.soul.ISoulContainer;
import com.laipel.ensorcellcraft.api.soul.SoulList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Implements({@Interface(iface = IPositionedSoulContainer.class, prefix = "i$")})
@Mixin(LivingEntity.class)
public abstract class MobMixin extends Entity {

    private MobMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot p_21467_);

    @Nullable private ISoulContainer getSoulContainerInMainHand() {
        if (this.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof ISoulContainer container)
            return container;
        return null;
    }

    @Nullable private ISoulContainer getSoulContainerInOffHand() {
        if (this.getItemBySlot(EquipmentSlot.OFFHAND).getItem() instanceof ISoulContainer container)
            return container;
        return null;
    }

    public int i$addSoul(ISoul soul, int amount) {
        ISoulContainer mainContainer = this.getSoulContainerInMainHand();
        if (mainContainer != null)
            amount = mainContainer.addSoul(soul, amount);

        if (amount == 0)
            return 0;

        ISoulContainer offContainer = this.getSoulContainerInOffHand();
        if (offContainer == null)
            return amount;
        return offContainer.addSoul(soul, amount);
    }

    @NotNull
    public SoulList i$addSouls(SoulList soulList) {
        ISoulContainer mainContainer = this.getSoulContainerInMainHand();
        if (mainContainer != null)
            soulList = mainContainer.addSouls(soulList);

        if (soulList.hasNoSouls())
            return soulList;

        ISoulContainer offContainer = this.getSoulContainerInOffHand();
        if (offContainer == null)
            return soulList;
        return offContainer.addSouls(soulList);
    }

    public int i$receiveSoul(ISoul soul, int amount) {
        ISoulContainer mainContainer = this.getSoulContainerInMainHand();
        if (mainContainer != null)
            amount = mainContainer.receiveSoul(soul, amount);

        if (amount == 0)
            return 0;

        ISoulContainer offContainer = this.getSoulContainerInOffHand();
        if (offContainer == null)
            return amount;
        return offContainer.receiveSoul(soul, amount);
    }

    @NotNull
    public SoulList i$receiveSouls(SoulList soulList) {
        ISoulContainer mainContainer = this.getSoulContainerInMainHand();
        if (mainContainer != null)
            soulList = mainContainer.receiveSouls(soulList);

        if (soulList.hasNoSouls())
            return soulList;

        ISoulContainer offContainer = this.getSoulContainerInOffHand();
        if (offContainer == null)
            return soulList;
        return offContainer.receiveSouls(soulList);
    }

    public boolean i$canAbsorbSouls(SoulList soulList) {
        ISoulContainer mainContainer = this.getSoulContainerInMainHand();
        if (mainContainer != null && mainContainer.canAbsorbSouls(soulList))
            return true;

        ISoulContainer offContainer = this.getSoulContainerInOffHand();
        return offContainer != null && offContainer.canAbsorbSouls(soulList);
    }

    public boolean i$canAbsorbSoul(ISoul soul, int amount) {
        ISoulContainer mainContainer = this.getSoulContainerInMainHand();
        if (mainContainer != null && mainContainer.canAbsorbSoul(soul, amount))
            return true;

        ISoulContainer offContainer = this.getSoulContainerInOffHand();
        return offContainer != null && offContainer.canAbsorbSoul(soul, amount);
    }

    public boolean i$hasSouls(SoulList soulList) {
        ISoulContainer mainContainer = this.getSoulContainerInMainHand();
        if (mainContainer != null && mainContainer.hasSouls(soulList))
            return true;

        ISoulContainer offContainer = this.getSoulContainerInOffHand();
        return offContainer != null && offContainer.hasSouls(soulList);
    }

    public boolean i$hasSoul(ISoul soul, int amount) {
        ISoulContainer mainContainer = this.getSoulContainerInMainHand();
        if (mainContainer != null && mainContainer.hasSoul(soul, amount))
            return true;

        ISoulContainer offContainer = this.getSoulContainerInOffHand();
        return offContainer != null && offContainer.hasSoul(soul, amount);
    }

    @NotNull
    public SoulList i$getSouls() {
        SoulList soulList = new SoulList();
        ISoulContainer mainContainer = this.getSoulContainerInMainHand();
        if (mainContainer != null)
            soulList.add(mainContainer.getSouls());

        if (soulList.hasNoSouls())
            return soulList;

        ISoulContainer offContainer = this.getSoulContainerInOffHand();
        if (offContainer == null)
            return soulList;
        soulList.add(offContainer.getSouls());
        return soulList;
    }

    public void i$clear() {
        ISoulContainer mainContainer = this.getSoulContainerInMainHand();
        if (mainContainer != null)
            mainContainer.clear();

        ISoulContainer offContainer = this.getSoulContainerInOffHand();
        if (offContainer != null)
            offContainer.clear();
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
