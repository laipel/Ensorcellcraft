package com.laipel.ensorcellcraft.common.entity;

import com.laipel.ensorcellcraft.api.soul.IPositionedSoulContainer;
import com.laipel.ensorcellcraft.api.soul.ISoul;
import com.laipel.ensorcellcraft.api.soul.ISoulContainer;
import com.laipel.ensorcellcraft.api.soul.StaticPositionSoulContainer;
import com.laipel.ensorcellcraft.utils.VectorUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.Comparator;

import static com.laipel.ensorcellcraft.common.registry.SoulRegistry.HOLLOW_SOUL;
import static com.laipel.ensorcellcraft.common.registry.SoulRegistry.SOUL_REGISTRY;

public class SoulEntity extends Entity {

    private IPositionedSoulContainer container;
    private static final EntityDataAccessor<Integer> VALUE = SynchedEntityData.defineId(SoulEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> SOUL = SynchedEntityData.defineId(SoulEntity.class, EntityDataSerializers.STRING);
    Vec3 accel = new Vec3(0, 0,0);

    private int age;
    private int maxAge = 100;

    public SoulEntity(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
        this.noPhysics = true;
        this.maxAge = 100;
    }

    public SoulEntity(EntityType<?> p_19870_, Level p_19871_, ISoul soul, int value) {
        super(p_19870_, p_19871_);
        this.entityData.set(VALUE, value);
        this.entityData.set(SOUL, SOUL_REGISTRY.get().getKey(soul).toString());
        this.noPhysics = true;
    }

    public boolean shouldRenderAtSqrDistance(double p_19883_) {
        return true;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        if (level.isClientSide)
            return;

        if (this.tickCount % 20 == 0)
            this.checkContainers();

        if (container == null && tickCount % 5 == 0) {
            updateAccel();
            setRandomAccel();
        } else {
            if (tickCount % 10 == 0
                    && container.canAbsorbSoul(this.getSoul(), 1)
                    && this.distanceToSqr(container.getX(), container.getY(), container.getZ()) < 64) {
                Vec3 pos = new Vec3(container.getX(), container.getY(), container.getZ());
                accel = pos.subtract(this.position()).normalize().scale(0.08f);
            } else {
                container = null;
                this.checkContainers();
            }
        }

        this.addDeltaMovement(accel);

        accel = accel.scale(0.97f);

        if (this.getDeltaMovement().lengthSqr() > 0.02)
            this.setDeltaMovement(this.getDeltaMovement().scale(0.96));

        double height = this.getHeight();
        double dh = Math.max(-5, Math.min(5.3, 7 - height));

        accel = new Vec3(accel.x, 0, accel.z);
        this.accel = accel.add(0, dh * 0.001, 0);

        this.move(MoverType.SELF, getDeltaMovement());

        if ((this.age++) >= this.maxAge)
            this.remove(RemovalReason.DISCARDED);
    }

    protected double getHeight() {
        int y1 = this.blockPosition().getY();
        while (level.getBlockState(new BlockPos(this.blockPosition().getX(), y1--, this.blockPosition().getZ())).isAir() && y1 - level.getMinBuildHeight() > 0);
        return this.position().y - y1;
    }

    protected void checkContainers() {
        double distance = Double.MAX_VALUE;
        container = this.getLevel().getEntitiesOfClass(LivingEntity.class,
                        this.getBoundingBox().inflate(7), e -> e instanceof IPositionedSoulContainer container$
                                && container$.canAbsorbSoul(getSoul(), 1))
                .stream().map(IPositionedSoulContainer::of)
                .min(Comparator.comparing(e -> ((LivingEntity) e).distanceToSqr(this)))
                .orElse(null);
        if (container != null)
            distance = this.distanceToSqr(container.getX(), container.getY(), container.getZ());
        for (int i = -2; i < 2; i++)
            for (int j = -2; j < 2; j++)
                for (int k = -2; k < 2; k++) {
                    BlockPos pos = new BlockPos(
                            this.blockPosition().getX() + i,
                            this.blockPosition().getY() + j,
                            this.blockPosition().getZ() + k
                    );
                    BlockState state = getLevel().getBlockState(pos);
                    if (state.getBlock() instanceof ISoulContainer posContainer && posContainer.canAbsorbSoul(getSoul(), 1)) {
                        Vec3 vec = VectorUtils.parse(pos).add(0.5, 0.5, 0.5);
                        double newDistance = this.distanceToSqr(vec);
                        if (newDistance < distance) {
                            distance = newDistance;
                            this.container = new StaticPositionSoulContainer(posContainer, vec, level);
                        }
                    }
                }
    }

    protected void updateAccel() {
        Vec3 pos;
        if (this.getFeetBlockState().isAir()) {
            for (int i = -2; i <= 2; i++)
                for (int k = -2; k <= 2; k++) {
                    pos = new Vec3(this.position().x + i, this.blockPosition().getY(), this.position().z + k);
                    if (!getLevel().getBlockState(new BlockPos(
                            (int) pos.x, (int) pos.y, (int) pos.z)).isAir()) {
                        accel = accel.add(position().subtract(pos).scale(0.0000001));

                    }
                }
        }
    }

    protected void setRandomAccel() {
        float f = 0.011f;
        Vec3 acopy = this.getDeltaMovement().multiply(1, 0, 1);
        acopy.normalize().scale(f).yRot((float) Math.toRadians(60 - Math.random() * 120));
        accel = accel.add(acopy.x, 0.0015 + Math.random() * 0.0015, acopy.z);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(VALUE, 0);
        this.entityData.define(SOUL, HOLLOW_SOUL.getKey().registry().toString());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.entityData.set(VALUE, tag.getInt("SoulValue"));
        this.entityData.set(SOUL, tag.getString("Soul"));
        this.age = tag.getInt("Age");
        this.maxAge = tag.getInt("MaxAge");
        this.accel = VectorUtils.loadFromNBT("Accel", tag);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("SoulValue", this.entityData.get(VALUE));
        tag.putString("Soul", this.entityData.get(SOUL));
        tag.putInt("Age", age);
        tag.putInt("MaxAge", maxAge);
        VectorUtils.saveToNBT("Accel", tag, accel);
    }

    public IPositionedSoulContainer getContainer() {
        return container;
    }

    public void setContainer(IPositionedSoulContainer container) {
        this.container = container;
    }

    public Vec3 getAccel() {
        return accel;
    }

    public void setAccel(Vec3 accel) {
        this.accel = accel;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public ISoul getSoul() {
        return SOUL_REGISTRY.get().getValue(new ResourceLocation(entityData.get(SOUL)));
    }

    public void setSoul(ISoul soul) {
        this.entityData.set(SOUL, SOUL_REGISTRY.get().getKey(soul).toString());
    }

    public int getValue() {
        return this.entityData.get(VALUE);
    }

    public void setValue(int value) {
        this.entityData.set(VALUE, value);
    }

    @Nonnull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
