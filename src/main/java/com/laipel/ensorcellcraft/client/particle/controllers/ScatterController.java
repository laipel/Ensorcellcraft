package com.laipel.ensorcellcraft.client.particle.controllers;

import com.laipel.ensorcellcraft.client.particle.ParticleInfo;
import com.laipel.ensorcellcraft.common.registry.ParticleRegistry;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ScatterController implements ControllerData, ControllerType<ScatterController> {

    Vec3 a = Vec3.ZERO;

    @NotNull
    @Override
    public <T extends ControllerData> ControllerType<T> getType() {
        return (ControllerType<T>) ParticleRegistry.ParticleBehaviorRegistry.SCATTER_CONTROLLER;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {

    }

    @Override
    public String writeToString() {
        return "";
    }

    @NotNull
    @Override
    public Vec3 handleMovement(ParticleInfo info, ScatterController data) {

        double length = info.movement().length();
        if ((info.age() + 1 + info.random().nextInt(7)) % 13 == 0) {
            a = new Vec3(0.5 - Math.random(), 0.5 - Math.random(), 0.5 - Math.random())
                    .scale(length * (0.3 + Math.random() * 0.5));
        }
        return  (new Vec3(info.movement().x, info.movement().y, info.movement().z).add(a)).normalize().scale(length);

    }

    @Override
    public ScatterController fromCommand(StringReader pReader) throws CommandSyntaxException {
        return new ScatterController();
    }

    @Override
    public ScatterController fromNetwork(FriendlyByteBuf pBuffer) {
        return new ScatterController();
    }
}
