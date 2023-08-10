package com.laipel.ensorcellcraft.client.render.entity;

import com.laipel.ensorcellcraft.api.soul.ISoul;
import com.laipel.ensorcellcraft.client.render.renderTypes.EnsRenderType;
import com.laipel.ensorcellcraft.common.entity.SoulEntity;
import com.laipel.ensorcellcraft.utils.RenderUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.Collections;
import java.util.List;

import static com.laipel.ensorcellcraft.Ensorcellcraft.MODID;

@OnlyIn(Dist.CLIENT)
public class SoulRenderer extends EntityRenderer<SoulEntity> {

    public SoulRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0F;
    }

    public List<RenderUtils.Streak> getStreaks(SoulEntity soul) {
        return Collections.singletonList(RenderUtils.Streak.builder()
                .width(0.03f)
                .segments(4)
                .startAlpha(0.5f)
                .finalAlpha(0.1f)
                .firstColor(soul.getSoul().getColor().brighter())
                .secondColor(Color.WHITE)
                .segmentsLife(40)
                .build());
    }

    @Override
    public void render(SoulEntity soulEntity, float p_114486_, float pTicks, PoseStack poseStack, MultiBufferSource bufferSource, int p_114490_) {

        ISoul soul = soulEntity.getSoul();

        if (soul == null)
            return;

        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        poseStack.pushPose();
        poseStack.scale(0.6f, 0.6f, 0.6f);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

        Minecraft.getInstance().textureManager.getTexture(getTextureLocation(soulEntity)).setBlurMipmap(true, false);

        Color color = soul.getColor();
        Matrix4f matrix4f = poseStack.last().pose();

        VertexConsumer bufferBuilder = bufferSource.getBuffer(EnsRenderType.SOUL_RENDER_TYPE);

        bufferBuilder.vertex(matrix4f, -1.2f, -1.2f, 0).uv(0.0F, 1.0F).color(color.getRed(), color.getBlue(), color.getGreen(), 70).uv2(p_114490_)
                .endVertex();
        bufferBuilder.vertex(matrix4f, 1.2f, -1.2f, 0).uv(1.0F, 1.0F).color(color.getRed(), color.getBlue(), color.getGreen(), 70).uv2(p_114490_).endVertex();
        bufferBuilder.vertex(matrix4f, 1.2f, 1.2f, 0).uv(1.0F, 0.0F).color(color.getRed(), color.getBlue(), color.getGreen(), 70).uv2(p_114490_).endVertex();
        bufferBuilder.vertex(matrix4f, -1.2f, 1.2f, 0).uv(0.0F, 0.0F).color(color.getRed(), color.getBlue(), color.getGreen(), 70).uv2(p_114490_).endVertex();

        bufferBuilder.vertex(matrix4f, (float) (0f - 0.6), (float) (0.0f - 0.6), 0).uv(0.0F, 1.0F).color(color.getRed(), color.getBlue(), color.getGreen(), 100).uv2(p_114490_).endVertex();
        bufferBuilder.vertex(matrix4f, 1f - 0.4f, 0.0f - 0.6f, 0).uv(1.0F, 1.0F).color(color.getRed(), color.getBlue(), color.getGreen(), 100).uv2(p_114490_).endVertex();
        bufferBuilder.vertex(matrix4f, 1f - 0.4f, 1.0f - 0.4f, 0).uv(1.0F, 0.0F).color(color.getRed(), color.getBlue(), color.getGreen(), 100).uv2(p_114490_).endVertex();
        bufferBuilder.vertex(matrix4f, 0f - 0.6f, 1.0f - 0.4f, 0).uv(0.0F, 0.0F).color(color.getRed(), color.getBlue(), color.getGreen(), 100).uv2(p_114490_).endVertex();
        
        bufferBuilder.vertex(matrix4f, (0f - 0.5f) + 0.13f, (0.0f - 0.5f) + 0.13f, 0f).uv(0.0F, 1.0F).color(color.getRed(), color.getBlue(), color.getGreen(), 120).uv2(p_114490_).endVertex();
        bufferBuilder.vertex(matrix4f, 1f - 0.5f - 0.13f, (0.0f - 0.5f) + 0.13f, 0f).uv(1.0F, 1.0F).color(color.getRed(), color.getBlue(), color.getGreen(), 120).uv2(p_114490_).endVertex();
        bufferBuilder.vertex(matrix4f, 1f - 0.5f - 0.13f, 1.0f - 0.5f - 0.13f, 0).uv(1.0F, 0.0F).color(color.getRed(), color.getBlue(), color.getGreen(), 120).uv2(p_114490_).endVertex();
        bufferBuilder.vertex(matrix4f, (0f - 0.5f) + 0.13f, 1.0f - 0.5f - 0.13f, 0).uv(0.0F, 0.0F).color(color.getRed(), color.getBlue(), color.getGreen(), 120).uv2(p_114490_).endVertex();

        Color color2 = Color.WHITE;
        
        bufferBuilder.vertex(matrix4f, (0f - 0.5f) + 0.26f, (0.0f - 0.5f) + 0.26f, 0.0f).uv(0.0F, 1.0F).color(color2.getRed(), color2.getBlue(), color2.getGreen(), 130).uv2(p_114490_).endVertex();
        bufferBuilder.vertex(matrix4f, 1f - 0.5f - 0.26f, (0.0f - 0.5f) + 0.26f, 0.0f).uv(1.0F, 1.0F).color(color2.getRed(), color2.getBlue(), color2.getGreen(), 130).uv2(p_114490_).endVertex();
        bufferBuilder.vertex(matrix4f, 1f - 0.5f - 0.26f, 1.0f - 0.5f - 0.26f, 0.0f).uv(1.0F, 0.0F).color(color2.getRed(), color2.getBlue(), color2.getGreen(), 130).uv2(p_114490_).endVertex();
        bufferBuilder.vertex(matrix4f, (0f - 0.5f) + 0.26f, 1.0f - 0.5f - 0.26f, 0.0f).uv(0.0F, 0.0F).color(color2.getRed(), color2.getBlue(), color2.getGreen(), 130).uv2(p_114490_).endVertex();
        
        bufferBuilder.vertex(matrix4f, (0f - 0.5f) + 0.4f, (0.0f - 0.5f) + 0.4f, 0.0f).uv(0.0F, 1.0F).color(color2.getRed(), color2.getBlue(), color2.getGreen(), 80).uv2(p_114490_).endVertex();
        bufferBuilder.vertex(matrix4f, 1f - 0.5f - 0.4f, (0.0f - 0.5f) + 0.4f, 0.0f).uv(1.0F, 1.0F).color(color2.getRed(), color2.getBlue(), color2.getGreen(), 80).uv2(p_114490_).endVertex();
        bufferBuilder.vertex(matrix4f, 1f - 0.5f - 0.4f, 1.0f - 0.5f - 0.4f, 0.0f).uv(1.0F, 0.0F).color(color2.getRed(), color2.getBlue(), color2.getGreen(), 80).uv2(p_114490_).endVertex();
        bufferBuilder.vertex(matrix4f, (0f - 0.5f) + 0.4f, 1.0f - 0.5f - 0.4f, 0.0f).uv(0.0F, 0.0F).color(color2.getRed(), color2.getBlue(), color2.getGreen(), 80).uv2(p_114490_).endVertex();

        poseStack.popPose();

        // RenderUtils.drawStreak(soulEntity, pTicks, poseStack, bufferSource, getStreaks(soulEntity));

        super.render(soulEntity, p_114486_, pTicks, poseStack, bufferSource, p_114490_);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(SoulEntity soul) {
        return new ResourceLocation(MODID, "textures/particle/wisp.png");
    }
}
