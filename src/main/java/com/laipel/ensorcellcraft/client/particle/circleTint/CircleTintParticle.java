package com.laipel.ensorcellcraft.client.particle.circleTint;

import com.laipel.ensorcellcraft.Ensorcellcraft;
import com.laipel.ensorcellcraft.client.particle.EnsTexturedSheetParticle;
import com.laipel.ensorcellcraft.client.particle.controllers.ControllerData;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.awt.*;

public class CircleTintParticle extends EnsTexturedSheetParticle {

    private final boolean standardBlending;
    float resizeSpeed;
    SpriteSet sprites;

    public CircleTintParticle(ControllerData data, ClientLevel world, double x, double y, double z, double velocityX,
                              double velocityY, double velocityZ, Color spark, float diameter,
                              int lifeTime, float resizeSpeed, boolean shouldCollide, boolean standardBlending, SpriteSet spriteSet) {
        super(data, world, x, y, z, velocityX, velocityY, velocityZ);
        this.resizeSpeed = resizeSpeed;
        setColor(spark.getRed() / 255.0F, spark.getGreen() / 255.0F, spark.getBlue() / 255.0F);
        setSize(diameter, diameter);
        this.sprites = spriteSet;

        lifetime = lifeTime;

        quadSize = diameter;
        this.alpha = 1.0F;

        xd = velocityX;
        yd = velocityY;
        zd = velocityZ;

        this.standardBlending = standardBlending;

        this.hasPhysics = shouldCollide;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        return this.quadSize;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return LightTexture.pack(15, 15);
    }

    @Nonnull
    @Override
    public ParticleRenderType getRenderType() {
        return standardBlending ? RENDERER_STANDARD_BLEND : RENDERER;
    }

    @Override
    public void tick() {

        super.tick();

        this.quadSize *= this.resizeSpeed;

        if (this.lifetime / 10 <= age && this.quadSize > 0.5)
            this.resizeSpeed = Math.min(0.8f, this.resizeSpeed);

    }

    private static final ParticleRenderType RENDERER = new ParticleRenderType() {
        @Override
        public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
            RenderSystem.setShader(GameRenderer::getParticleShader);
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);

            RenderSystem.depthMask(false);

            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

            textureManager.getTexture(TextureAtlas.LOCATION_PARTICLES).setBlurMipmap(true, false);

            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator tessellator) {
            tessellator.end();

            RenderSystem.enableDepthTest();

            Minecraft.getInstance().textureManager.getTexture(TextureAtlas.LOCATION_PARTICLES).restoreLastBlurMipmap();

            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
        }

        @Override
        public String toString() {
            return Ensorcellcraft.MODID + ":" + "wisp";
        }
    };

    private static final ParticleRenderType RENDERER_STANDARD_BLEND = new ParticleRenderType() {
        @Override
        public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
            RenderSystem.setShader(GameRenderer::getParticleShader);
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);

            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            textureManager.getTexture(TextureAtlas.LOCATION_PARTICLES).setBlurMipmap(true, false);

            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator tessellator) {
            tessellator.end();

            RenderSystem.enableDepthTest();

            Minecraft.getInstance().textureManager.getTexture(TextureAtlas.LOCATION_PARTICLES).restoreLastBlurMipmap();

            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
        }

        @Override
        public String toString() {
            return Ensorcellcraft.MODID + ":" + "wisp";
        }
    };
}
