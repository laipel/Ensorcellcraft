package com.laipel.ensorcellcraft.client.particle;

import com.laipel.ensorcellcraft.client.particle.controllers.ControllerData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.phys.Vec3;

public abstract class EnsTexturedSheetParticle extends TextureSheetParticle {

    public ControllerData data;

    protected EnsTexturedSheetParticle(ControllerData data, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.data = data;
    }

    @Override
    public void tick() {

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            Vec3 dmove = data.getType().handleMovement(new ParticleInfo(level, new Vec3(xd, yd, zd), new Vec3(x, y, z),
                    new Vec3(xo, yo, zo), this.random, onGround, bbWidth, bbHeight, lifetime, age, gravity, friction), data);

            this.xd = dmove.x;
            this.yd = dmove.y;
            this.zd = dmove.z;

            this.move(xd, yd, zd);
        }
    }

}
