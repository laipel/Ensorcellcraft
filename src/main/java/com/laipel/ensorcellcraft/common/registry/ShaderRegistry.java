package com.laipel.ensorcellcraft.common.registry;

import com.laipel.ensorcellcraft.Ensorcellcraft;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Ensorcellcraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ShaderRegistry {

    private static ShaderInstance smoothPosTexColor;

    @SubscribeEvent
    public static void shaderRegistry(RegisterShadersEvent event) throws IOException
    {
        // Adds a shader to the list, the callback runs when loading is complete.
        event.registerShader(new ShaderInstance(event.getResourceProvider(), new ResourceLocation("ensorcellcraft:position_color_tex_smooth"), DefaultVertexFormat.POSITION_TEX_COLOR), shaderInstance -> {
            smoothPosTexColor = shaderInstance;
        });
    }

    public static ShaderInstance getSmoothPosTexColorShader() {
        return smoothPosTexColor;
    }

}
