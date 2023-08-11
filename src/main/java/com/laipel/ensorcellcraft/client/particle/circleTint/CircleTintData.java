package com.laipel.ensorcellcraft.client.particle.circleTint;

import com.laipel.ensorcellcraft.client.particle.EnsParticleData;
import com.laipel.ensorcellcraft.client.particle.controllers.ControllerData;
import com.laipel.ensorcellcraft.client.particle.controllers.ControllerType;
import com.laipel.ensorcellcraft.client.particle.controllers.StandartController;
import com.laipel.ensorcellcraft.common.registry.ParticleRegistry;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Locale;

public class CircleTintData implements EnsParticleData<CircleTintData> {
    private final Color tint;
    private final float diameter;
    private final int lifeTime;
    private final float resizeSpeed;
    private final boolean shouldCollide;
    private final boolean standardBlending;
    private @Nonnull ControllerData controllerData = new StandartController();

    public CircleTintData(Color tint, float diameter, int lifeTime, float resizeSpeed, boolean shouldCollide, boolean standardBlending, ControllerData controllerData) {
        this.tint = tint;
        this.lifeTime = lifeTime;
        this.diameter = validateDiameter(diameter);
        this.resizeSpeed = resizeSpeed;
        this.shouldCollide = shouldCollide;
        this.standardBlending = standardBlending;
        this.controllerData = controllerData == null ? new StandartController() : controllerData;
    }

    public CircleTintData(Color tint, float diameter, int lifeTime, float resizeSpeed, boolean shouldCollide, boolean standardBlending) {
        this.tint = tint;
        this.lifeTime = lifeTime;
        this.diameter = validateDiameter(diameter);
        this.resizeSpeed = resizeSpeed;
        this.shouldCollide = shouldCollide;
        this.standardBlending = standardBlending;
    }

    public Color getTint() {
        return tint;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public float getDiameter() {
        return diameter;
    }

    public float getResizeSpeed() {
        return resizeSpeed;
    }

    public boolean shouldCollide() {
        return shouldCollide;
    }

    public boolean standardBlending() {
        return standardBlending;
    }
    @Nonnull
    @Override
    public ParticleType<CircleTintData> getType() {
        return ParticleRegistry.CIRCLE_TINT.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeInt(tint.getRed());
        buf.writeInt(tint.getGreen());
        buf.writeInt(tint.getBlue());
        buf.writeFloat(diameter);
        buf.writeInt(lifeTime);
        buf.writeFloat(resizeSpeed);
        buf.writeBoolean(shouldCollide);
        buf.writeBoolean(standardBlending);
        buf.writeInt(ParticleRegistry.ParticleBehaviorRegistry.getId(this.controllerData.getType()));
        controllerData.writeToNetwork(buf);
    }

    @Nonnull
    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %d %d %d %.2f %d %f %b %s",
                ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()), tint.getRed(), tint.getGreen(), tint.getBlue(), diameter, lifeTime, resizeSpeed, shouldCollide, controllerData.writeToString());
    }

    private static float validateDiameter(float diameter) {
        return (float) Mth.clamp(diameter, 0.05, 5.0);
    }

    public static final Codec<CircleTintData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("tint").forGetter(d -> d.tint.getRGB()),
                    Codec.FLOAT.fieldOf("diameter").forGetter(d -> d.diameter),
                    Codec.INT.fieldOf("life_time").forGetter(d -> d.lifeTime),
                    Codec.FLOAT.fieldOf("resize_speed").forGetter(d -> d.resizeSpeed),
                    Codec.BOOL.fieldOf("should_collide").forGetter(d -> d.shouldCollide)
            ).apply(instance, (tintRGB, diameter1, lifeTime1, resizeSpeed1, shouldCollide1) -> new CircleTintData(tintRGB, diameter1, lifeTime1, resizeSpeed1, shouldCollide1,
                    new StandartController()))
    );

    private CircleTintData(int tintRGB, float diameter, int lifeTime, float resizeSpeed, boolean shouldCollide, ControllerData controllerData) {
        this.tint = new Color(tintRGB);
        this.lifeTime = lifeTime;
        this.diameter = validateDiameter(diameter);
        this.resizeSpeed = resizeSpeed;
        this.shouldCollide = shouldCollide;
        this.standardBlending = false;
        this.controllerData = controllerData;
    }

    public static final ParticleOptions.Deserializer<CircleTintData> DESERIALIZER = new ParticleOptions.Deserializer<>() {
        @Nonnull
        @Override
        public CircleTintData fromCommand(@Nonnull ParticleType<CircleTintData> type, @Nonnull StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            int red = Mth.clamp(reader.readInt(), 0, 255);
            reader.expect(' ');
            int green = Mth.clamp(reader.readInt(), 0, 255);
            reader.expect(' ');
            int blue = Mth.clamp(reader.readInt(), 0, 255);

            reader.expect(' ');
            float diameter = validateDiameter(reader.readFloat());

            reader.expect(' ');
            int lifeTime = reader.readInt();

            reader.expect(' ');
            float resizeSpeed = reader.readFloat();

            reader.expect(' ');
            boolean shouldCollide = reader.readBoolean();

            reader.expect(' ');
            boolean standardBlending = reader.readBoolean();

            reader.expect(' ');
            int id = reader.readInt();
            ControllerType<? extends ControllerData> ctype = ParticleRegistry.ParticleBehaviorRegistry.byId(id);

            return new CircleTintData(new Color(red, green, blue), diameter, lifeTime, resizeSpeed, shouldCollide, standardBlending, ctype.fromCommand(reader));
        }

        @Override
        public CircleTintData fromNetwork(@Nonnull ParticleType<CircleTintData> type, FriendlyByteBuf buf) {
            int red = Mth.clamp(buf.readInt(), 0, 255);
            int green = Mth.clamp(buf.readInt(), 0, 255);
            int blue = Mth.clamp(buf.readInt(), 0, 255);
            Color color = new Color(red, green, blue);

            float diameter = validateDiameter(buf.readFloat());

            int lifeTime = buf.readInt();

            float resizeSpeed = buf.readFloat();

            boolean shouldCollide = buf.readBoolean();

            boolean standardBlending = buf.readBoolean();

            int id = buf.readInt();
            ControllerType<? extends ControllerData> ctype = ParticleRegistry.ParticleBehaviorRegistry.byId(id);

            return new CircleTintData(color, diameter, lifeTime, resizeSpeed, shouldCollide, standardBlending, ctype.fromNetwork(buf));
        }
    };

    @Override
    public ControllerData getController() {
        return this.controllerData;
    }

    @Override
    public CircleTintData setController(ControllerData data) {
        this.controllerData = data == null ? new StandartController() : data;
        return this;
    }
}
