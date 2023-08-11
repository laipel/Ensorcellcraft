package com.laipel.ensorcellcraft.client.particle.controllers;

import com.laipel.ensorcellcraft.client.particle.ParticleInfo;
import com.mojang.brigadier.StringReader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import static com.laipel.ensorcellcraft.common.registry.ParticleRegistry.ParticleBehaviorRegistry.STANDART_CONTROLLER;


public class StandartController implements ControllerData, ControllerType<StandartController> {

    @Override
    public @NotNull ControllerType<StandartController> getType() {
        return STANDART_CONTROLLER;
    }

    @Override
    public StandartController fromCommand(StringReader pReader) {
        return this;
    }

    @Override
    public StandartController fromNetwork(FriendlyByteBuf pBuffer) {
        return this;
    }

    @Override
    @Nonnull
    public Vec3 handleMovement(ParticleInfo info, StandartController data) {

        double mx = info.movement().x;
        double my = info.movement().y;
        double mz = info.movement().z;
        my -= 0.04D * (double) info.gravity();

        mx *= info.friction();
        my *= info.friction();
        mz *= info.friction();

        return new Vec3(mx, my, mz);

    }

    @Override
    public String writeToString() {
        return "";
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {

    }
}
