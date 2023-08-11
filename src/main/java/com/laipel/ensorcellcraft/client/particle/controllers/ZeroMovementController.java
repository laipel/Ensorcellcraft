package com.laipel.ensorcellcraft.client.particle.controllers;

import com.laipel.ensorcellcraft.client.particle.ParticleInfo;
import com.mojang.brigadier.StringReader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import static com.laipel.ensorcellcraft.common.registry.ParticleRegistry.ParticleBehaviorRegistry.ZERO_MOVEMENT;

public class ZeroMovementController implements ControllerData, ControllerType<ZeroMovementController> {

    @Override
    public @NotNull ControllerType<ZeroMovementController> getType() {
        return ZERO_MOVEMENT;
    }

    @Override
    public ZeroMovementController fromCommand(StringReader pReader) {
        return this;
    }

    @Override
    public ZeroMovementController fromNetwork(FriendlyByteBuf pBuffer) {
        return this;
    }

    @Override
    @Nonnull
    public Vec3 handleMovement(ParticleInfo info, ZeroMovementController data) {
        return new Vec3(0, 0, 0);
    }

    @Override
    public String writeToString() {
        return "";
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {

    }
}
