package com.laipel.ensorcellcraft.client.particle.controllers;

import com.laipel.ensorcellcraft.client.particle.ParticleInfo;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;

public interface ControllerType<T extends ControllerData> {

    @Nonnull Vec3 handleMovement(ParticleInfo info, T data);

    T fromCommand( StringReader pReader) throws CommandSyntaxException;

    T fromNetwork(FriendlyByteBuf pBuffer);


}
