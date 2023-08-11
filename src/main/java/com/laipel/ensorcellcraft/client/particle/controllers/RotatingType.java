package com.laipel.ensorcellcraft.client.particle.controllers;

import com.laipel.ensorcellcraft.client.particle.ParticleInfo;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;

public class RotatingType implements ControllerType<RotatingData> {

    @Override
    @Nonnull
    public Vec3 handleMovement(ParticleInfo info, RotatingData data) {

        if (data.getStaticMovement() == null)
            data.setStaticMovement(new Vec3(info.movement().x, info.movement().y, info.movement().z));

        Vec3 vec = new Vec3(info.pos().x, info.pos().y, info.pos().z)
                .subtract(data.getCenter().x, data.getCenter().y, data.getCenter().z);

        Vec3 axis = new Vec3(data.getAxis().x, data.getAxis().y, data.getAxis().z);
        Vec3 cross = vec.cross(axis).normalize();
        Vec3 movementNotScaled = cross.subtract(vec.normalize().scale(1 - data.getResizeSpeed()));

        return movementNotScaled.normalize().scale(data.getRotatingSpeed());
    }

    @Override
    public RotatingData fromCommand(StringReader pReader) throws CommandSyntaxException {

        double axisX = pReader.readDouble();
        pReader.expect(' ');

        double axisY = pReader.readDouble();
        pReader.expect(' ');

        double axisZ = pReader.readDouble();
        pReader.expect(' ');

        double centerX = pReader.readDouble();
        pReader.expect(' ');

        double centerY = pReader.readDouble();
        pReader.expect(' ');

        double centerZ = pReader.readDouble();
        pReader.expect(' ');

        double rotatingSpeed = pReader.readDouble();
        pReader.expect(' ');

        double resizeSpeed = pReader.readDouble();
        pReader.expect(' ');

        return RotatingData.builder()
                .axis(new Vec3(axisX, axisY, axisZ))
                .center(new Vec3(centerX, centerY, centerZ))
                .rotatingSpeed(rotatingSpeed)
                .resizeSpeed(resizeSpeed)
                .build();
    }

    @Override
    public RotatingData fromNetwork(FriendlyByteBuf pBuffer) {

        Vec3 axis = new Vec3(pBuffer.readDouble(), pBuffer.readDouble(), pBuffer.readDouble());
        Vec3 center = new Vec3(pBuffer.readDouble(), pBuffer.readDouble(), pBuffer.readDouble());
        double rotatingSpeed = pBuffer.readDouble();
        double resizeSpeed = pBuffer.readDouble();

        return RotatingData.builder()
                .axis(axis)
                .center(center)
                .rotatingSpeed(rotatingSpeed)
                .resizeSpeed(resizeSpeed)
                .build();
    }

}
