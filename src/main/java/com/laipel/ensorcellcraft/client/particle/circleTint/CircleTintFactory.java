package com.laipel.ensorcellcraft.client.particle.circleTint;

import com.mojang.serialization.Codec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;

import javax.annotation.Nullable;

public class CircleTintFactory implements ParticleProvider<CircleTintData> {

    final SpriteSet sprites;

    @Nullable
    @Override
    public CircleTintParticle createParticle(CircleTintData circleTintData, ClientLevel world, double xPos, double yPos,
                                   double zPos, double xVelocity, double yVelocity, double zVelocity) {

        CircleTintParticle particle = new CircleTintParticle(circleTintData.getController(), world, xPos, yPos, zPos, xVelocity, yVelocity, zVelocity,
                circleTintData.getTint(), circleTintData.getDiameter(), circleTintData.getLifeTime(), circleTintData.getResizeSpeed(), circleTintData.shouldCollide(), circleTintData.standardBlending(), sprites);

        particle.pickSprite(sprites);

        return particle;
    }

    public CircleTintFactory(SpriteSet sprite) {
        this.sprites = sprite;
    }

    public static class CircleTintType extends ParticleType<CircleTintData> {
        public CircleTintType() {
            super(false, CircleTintData.DESERIALIZER);
        }

        @Override
        public Codec<CircleTintData> codec() {
            return CircleTintData.CODEC;
        }
    }
}
