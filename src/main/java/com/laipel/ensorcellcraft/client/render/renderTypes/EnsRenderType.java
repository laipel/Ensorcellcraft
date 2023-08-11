package com.laipel.ensorcellcraft.client.render.renderTypes;

import com.laipel.ensorcellcraft.common.registry.ShaderRegistry;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
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

    public static RenderType depthLightningRenderType(ResourceLocation loc){
        return create("ec_light_color_tex", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true,
                RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(loc, false, false))
                        .setTransparencyState(LIGHTNING_TRANSPARENCY)
                        .setOutputState(OutputStateShard.ITEM_ENTITY_TARGET)
                        .setLightmapState(NO_LIGHTMAP)
                        .createCompositeState(false));
    }

    public static RenderType getLightColor() {
        return create("ec_light_color", DefaultVertexFormat.POSITION_COLOR,
                VertexFormat.Mode.QUADS, 256, false, false,
                RenderType.CompositeState.builder()
                        .setTransparencyState(LIGHTNING_TRANSPARENCY)
                        .setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
                        .setOutputState(OutputStateShard.MAIN_TARGET)
                        .setDepthTestState(LEQUAL_DEPTH_TEST)
                        .createCompositeState(false));
    }

    public EnsRenderType(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }
}
