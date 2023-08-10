package com.laipel.ensorcellcraft.client.render.renderTypes;

import com.laipel.ensorcellcraft.client.render.entity.SoulRenderer;
import com.laipel.ensorcellcraft.common.registry.ShaderRegistry;
import com.laipel.ensorcellcraft.common.registry.SoulRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import static com.laipel.ensorcellcraft.Ensorcellcraft.MODID;

public class EnsRenderType extends RenderType {

    public static final RenderType SOUL_RENDER_TYPE = RenderType.create("soul_render_type", DefaultVertexFormat.POSITION_TEX_COLOR,
            VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder()
                    .setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(MODID, "textures/particle/wisp.png"), true, true))
                    .setLightmapState(LIGHTMAP)
                    .setTransparencyState(LIGHTNING_TRANSPARENCY)
                    .setWriteMaskState(COLOR_DEPTH_WRITE)
                    .setCullState(RenderStateShard.CULL)
                    .setShaderState(new RenderStateShard.ShaderStateShard(ShaderRegistry::getSmoothPosTexColorShader))
                    .createCompositeState(true));

    public EnsRenderType(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }
}
