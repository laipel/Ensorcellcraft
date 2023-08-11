package com.laipel.ensorcellcraft.client.particle.controllers;

import net.minecraft.network.FriendlyByteBuf;

import javax.annotation.Nonnull;

public interface ControllerData {

    @Nonnull
    <T extends ControllerData> ControllerType<T> getType();

    void writeToNetwork(FriendlyByteBuf pBuffer);

    String writeToString();

}
