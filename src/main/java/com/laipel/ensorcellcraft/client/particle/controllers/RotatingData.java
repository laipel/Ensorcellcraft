package com.laipel.ensorcellcraft.client.particle.controllers;

import com.laipel.ensorcellcraft.common.registry.ParticleRegistry;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class RotatingData implements ControllerData {

    @Setter
    @Nullable private Vec3 staticMovement;
    @Builder.Default
    private Vec3 axis = new Vec3(0, 1, 0);
    @Builder.Default
    private Vec3 center = new Vec3(0, 0, 0);
    @Builder.Default
    private double rotatingSpeed = 0.3;
    @Builder.Default
    private double resizeSpeed = 1.0;

    public RotatingData(double centerX, double centerY, double centerZ, double axisX, double axisY, double axisZ,
                        double rotatingSpeed, double resizeSpeed) {
        this.axis = new Vec3(axisX, axisY, axisZ);
        this.center = new Vec3(centerX, centerY, centerZ);
        this.rotatingSpeed = rotatingSpeed;
        this.resizeSpeed = resizeSpeed;
    }

    @Builder
    public RotatingData(Vec3 axis, Vec3 center, double rotatingSpeed, double resizeSpeed) {
        this.axis = axis;
        this.center = center;
        this.rotatingSpeed = rotatingSpeed;
        this.resizeSpeed = resizeSpeed;
    }

    @Override
    @NotNull
    public ControllerType<RotatingData> getType() {
        return ParticleRegistry.ParticleBehaviorRegistry.ROTATING_CONTROLLER;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        pBuffer.writeDouble(axis.x);
        pBuffer.writeDouble(axis.y);
        pBuffer.writeDouble(axis.z);
        pBuffer.writeDouble(center.x);
        pBuffer.writeDouble(center.y);
        pBuffer.writeDouble(center.z);
        pBuffer.writeDouble(rotatingSpeed);
        pBuffer.writeDouble(resizeSpeed);
    }

    @Override
    public String writeToString() {
        return String.format("%f %f %f %f %f %f %f %f", axis.x, axis.y, axis.z, center.x, center.y, center.z,
                rotatingSpeed, resizeSpeed);
    }

}
