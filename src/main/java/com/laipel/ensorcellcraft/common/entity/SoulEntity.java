package com.laipel.ensorcellcraft.common.entity;

import com.laipel.ensorcellcraft.api.IStreakable;
import com.laipel.ensorcellcraft.api.soul.IPositionedSoulContainer;
import com.laipel.ensorcellcraft.api.soul.ISoul;
import com.laipel.ensorcellcraft.api.soul.ISoulContainer;
import com.laipel.ensorcellcraft.api.soul.StaticPositionSoulContainer;
import com.laipel.ensorcellcraft.client.particle.circleTint.CircleTintData;
import com.laipel.ensorcellcraft.client.particle.controllers.ScatterController;
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
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Comparator;

import static com.laipel.ensorcellcraft.common.registry.SoulRegistry.HOLLOW_SOUL;
import static com.laipel.ensorcellcraft.common.registry.SoulRegistry.SOUL_REGISTRY;

public class SoulEntity extends Entity implements IStreakable<SoulEntity> {

    private IPositionedSoulContainer container;
    private static final EntityDataAccessor<Integer> VALUE = SynchedEntityData.defineId(SoulEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> SOUL = SynchedEntityData.defineId(SoulEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> AGE = SynchedEntityData.defineId(SoulEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MAX_AGE = SynchedEntityData.defineId(SoulEntity.class, EntityDataSerializers.INT);
    Vec3 accel = new Vec3(0, 0, 0);

    public SoulEntity(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
        this.noPhysics = true;
        setMaxAge(100);
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

        int age = this.getAge();
        this.setAge(++age);
        if (age >= this.getMaxAge())
            this.remove(RemovalReason.DISCARDED);

        this.xo = this.position().x;
        this.yo = this.position().y;
        this.zo = this.position().z;
        this.move(MoverType.SELF, getDeltaMovement());

        if (level.isClientSide)
            return;

        accel = accel.scale(0.97f);

        if (this.getDeltaMovement().lengthSqr() > 0.02)
            this.setDeltaMovement(this.getDeltaMovement().scale(0.96));

        this.setDeltaMovement(this.getDeltaMovement().scale(0.98));

        if (this.tickCount % 20 == 0)
            this.checkContainers();

        if (container == null && tickCount % 5 == 0) {
            updateAccel();
            setRandomAccel();
        } else {
            if (tickCount % 10 == 0)
                if (container.canAbsorbSoul(this.getSoul(), 1)
                        && this.distanceToSqr(container.getX(), container.getY(), container.getZ()) < 64) {
                    Vec3 pos = new Vec3(container.getX(), container.getY(), container.getZ());
                    accel = pos.subtract(this.position()).normalize().scale(0.06f);
                } else {
                    container = null;
                    this.checkContainers();
                }
        }

        this.addDeltaMovement(accel);

        double height = this.getHeight();
        double dh = Math.max(-3.3, Math.min(4.3, 5 - height));
        accel = new Vec3(0.96 * accel.x, 0.94 * accel.y, 0.96 * accel.z);

        if (dh < 0 && tickCount % (30 + this.random.nextInt(10)) == 0)
            this.accel = accel.add(0, -0.006, 0);

        this.accel = accel.add(0, dh * 0.0001, 0);

    }

    protected double getHeight() {
        int y1 = this.blockPosition().getY();
        BlockPos blockPos;
        while (level.getBlockState(blockPos = new BlockPos(this.blockPosition().getX(), y1--, this.blockPosition().getZ())).getCollisionShape(level, blockPos).isEmpty() && y1 - level.getMinBuildHeight() > 0)
            ;
        return this.position().y - y1;
    }

    @Override
    public boolean isInLava() {
        return false;
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

    @Override
    public boolean shouldRender(double p_20296_, double p_20297_, double p_20298_) {
        return true;
    }

    protected void updateAccel() {
        if (this.getFeetBlockState().getCollisionShape(level, this.blockPosition()).isEmpty()) {
            for (int i = -2; i <= 2; i++)
                for (int k = -2; k <= 2; k++) {
                    BlockPos blockPos = new BlockPos(
                            this.blockPosition().getX() + i,
                            this.blockPosition().getY(),
                            this.blockPosition().getZ() + k);
                    Vec3 pos = new Vec3(i, 0, k);
                    if (!getLevel().getBlockState(blockPos)
                            .getCollisionShape(level, blockPos).isEmpty()) {
                        accel = accel.add(pos.scale(-0.001));
                    }
                }
        }
    }

    protected void setRandomAccel() {
        float f = 0.006f;
        Vec3 acopy = this.getDeltaMovement().multiply(1, 0, 1);
        if (acopy.lengthSqr() == 0)
            acopy = new Vec3(0.5 - Math.random(), 0, 0.5 - Math.random());
        acopy = acopy.normalize().scale(f).yRot((float) Math.toRadians(150 - Math.random() * 300));
        accel = accel.add(acopy.x, Math.random() * 0.0015, acopy.z);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(VALUE, 0);
        this.entityData.define(SOUL, HOLLOW_SOUL.getKey().registry().toString());
        this.entityData.define(AGE, 0);
        this.entityData.define(MAX_AGE, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.entityData.set(VALUE, tag.getInt("SoulValue"));
        this.entityData.set(SOUL, tag.getString("Soul"));
        this.setAge(tag.getInt("Age"));
        this.setMaxAge(tag.getInt("MaxAge"));
        this.accel = VectorUtils.loadFromNBT("Accel", tag);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("SoulValue", this.entityData.get(VALUE));
        tag.putString("Soul", this.entityData.get(SOUL));
        tag.putInt("Age", this.getAge());
        tag.putInt("MaxAge", this.getMaxAge());
        VectorUtils.saveToNBT("Accel", tag, accel);
    }

    @Override
    public void onClientRemoval() {
        super.onClientRemoval();

        Color color = this.getSoul().getColor();
        int r = Math.max(color.getRed() - 40, 0);
        int g = Math.max(color.getGreen() - 40, 0);
        int b = Math.max(color.getBlue() - 40, 0);

        for (int i = 0; i < 20; i++) {
            CircleTintData data = new CircleTintData(new Color(r, g, b), 0.2F, 40, 0.95F, false, false, new ScatterController());
            level.addAlwaysVisibleParticle(data, true, this.position().x, this.position().y, this.position().z,
                    0.02 - Math.random() * 0.04, 0.02 - Math.random() * 0.04, 0.02 - Math.random() * 0.04);
        }
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
        return this.entityData.get(AGE);
    }

    public void setAge(int age) {
        this.entityData.set(AGE, age);
    }

    public int getMaxAge() {
        return this.entityData.get(MAX_AGE);
    }

    public void setMaxAge(int maxAge) {
        this.entityData.set(MAX_AGE, maxAge);
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

    @Override
    public @NotNull SoulEntity get() {
        return this;
    }

    @Override
    public int ticksBeforeDeath() {
        return this.getMaxAge() - this.getAge();
    }

    private StreakBuffer streakBuffer;
    private static final int SEGMENTS_SIZE = 5;

    @Override
    public @NotNull Streak getStreak() {
        return IStreakable.Streak.builder()
                .width(0.08f)
                .segments(SEGMENTS_SIZE)
                .startAlpha(0.8f)
                .finalAlpha(0.1f)
                .firstColor(Color.WHITE)
                .secondColor(getSoul().getColor())
                .segmentsLife(5)
                .build();
    }

    @Override
    public @NotNull StreakBuffer getStreakBuffer() {
        if (streakBuffer == null)
            streakBuffer = new StreakBuffer(SEGMENTS_SIZE);
        return streakBuffer;
    }
}
