package com.laipel.ensorcellcraft.client.render.entity;

import com.laipel.ensorcellcraft.api.soul.ISoul;
import com.laipel.ensorcellcraft.client.render.renderTypes.EnsRenderType;
import com.laipel.ensorcellcraft.common.entity.SoulEntity;
import com.laipel.ensorcellcraft.utils.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.awt.*;

import static com.laipel.ensorcellcraft.Ensorcellcraft.MODID;
import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

@OnlyIn(Dist.CLIENT)
public class SoulRenderer extends EntityRenderer<SoulEntity> {

    public SoulRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0F;
    }

    @Override
    public boolean shouldRender(SoulEntity p_114491_, Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
        return true;
    }

    @Override
    public void render(SoulEntity soulEntity, float p_114486_, float pTicks, PoseStack poseStack, MultiBufferSource bufferSource, int p_114490_) {

        ISoul soul = soulEntity.getSoul();
        if (soul == null)
            return;

        poseStack.pushPose();
        poseStack.scale(0.6f, 0.6f, 0.6f);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

        Color color = soul.getColor();
        Matrix4f matrix4f = poseStack.last().pose();

        VertexConsumer bufferBuilder = bufferSource.getBuffer(EnsRenderType.depthLightningRenderType(getTextureLocation(soulEntity)));

        bufferBuilder.vertex(matrix4f, -1.2f, -1.2f, 0.01F).color(color.getRed(), color.getGreen(), color.getBlue(), 70).uv(0.0F, 1.0F).uv2(p_114490_).overlayCoords(NO_OVERLAY).endVertex();
        bufferBuilder.vertex(matrix4f, 1.2f, -1.2f, 0.01F).color(color.getRed(), color.getGreen(), color.getBlue(), 70).uv(1.0F, 1.0F).uv2(p_114490_).overlayCoords(NO_OVERLAY).endVertex();
        bufferBuilder.vertex(matrix4f, 1.2f, 1.2f, 0.01F).color(color.getRed(), color.getGreen(), color.getBlue(), 70).uv(1.0F, 0.0F).uv2(p_114490_).overlayCoords(NO_OVERLAY).endVertex();
        bufferBuilder.vertex(matrix4f, -1.2f, 1.2f, 0.00F).color(color.getRed(), color.getGreen(), color.getBlue(), 70).uv(0.0F, 0.0F).uv2(p_114490_).overlayCoords(NO_OVERLAY).endVertex();

        bufferBuilder.vertex(matrix4f, (float) (0f - 0.6), (float) (0.0f - 0.6), 0.02F).color(color.getRed(), color.getGreen(), color.getBlue(), 100).uv(0.0F, 1.0F).uv2(p_114490_).overlayCoords(NO_OVERLAY).endVertex();
        bufferBuilder.vertex(matrix4f, 1f - 0.4f, 0.0f - 0.6f, 0.02f).color(color.getRed(), color.getGreen(), color.getBlue(), 100).uv(1.0F, 1.0F).uv2(p_114490_).overlayCoords(NO_OVERLAY).endVertex();
        bufferBuilder.vertex(matrix4f, 1f - 0.4f, 1.0f - 0.4f, 0.02f).color(color.getRed(), color.getGreen(), color.getBlue(), 100).uv(1.0F, 0.0F).uv2(p_114490_).overlayCoords(NO_OVERLAY).endVertex();
        bufferBuilder.vertex(matrix4f, 0f - 0.6f, 1.0f - 0.4f, 0.02f).color(color.getRed(), color.getGreen(), color.getBlue(), 100).uv(0.0F, 0.0F).uv2(p_114490_).overlayCoords(NO_OVERLAY).endVertex();

        bufferBuilder.vertex(matrix4f, (0f - 0.5f) + 0.13f, (0.0f - 0.5f) + 0.13f, 0.03f).color(color.getRed(), color.getGreen(), color.getBlue(), 120).uv(0.0F, 1.0F).overlayCoords(NO_OVERLAY).uv2(p_114490_).endVertex();
        bufferBuilder.vertex(matrix4f, 1f - 0.5f - 0.13f, (0.0f - 0.5f) + 0.13f, 0.03f).color(color.getRed(), color.getGreen(), color.getBlue(), 120).uv(1.0F, 1.0F).overlayCoords(NO_OVERLAY).uv2(p_114490_).endVertex();
        bufferBuilder.vertex(matrix4f, 1f - 0.5f - 0.13f, 1.0f - 0.5f - 0.13f, 0.03f).color(color.getRed(), color.getGreen(), color.getBlue(), 120).uv(1.0F, 0.0F).overlayCoords(NO_OVERLAY).uv2(p_114490_).endVertex();
        bufferBuilder.vertex(matrix4f, (0f - 0.5f) + 0.13f, 1.0f - 0.5f - 0.13f, 0.03f).color(color.getRed(), color.getGreen(), color.getBlue(), 120).uv(0.0F, 0.0F).overlayCoords(NO_OVERLAY).uv2(p_114490_).endVertex();

        Color color2 = Color.WHITE;

        bufferBuilder.vertex(matrix4f, (0f - 0.5f) + 0.26f, (0.0f - 0.5f) + 0.26f, 0.04f).color(color2.getRed(), color2.getGreen(), color2.getBlue(), 130).uv(0.0F, 1.0F).overlayCoords(NO_OVERLAY).uv2(p_114490_).endVertex();
        bufferBuilder.vertex(matrix4f, 1f - 0.5f - 0.26f, (0.0f - 0.5f) + 0.26f, 0.04f).color(color2.getRed(), color2.getGreen(), color2.getBlue(), 130).uv(1.0F, 1.0F).overlayCoords(NO_OVERLAY).uv2(p_114490_).endVertex();
        bufferBuilder.vertex(matrix4f, 1f - 0.5f - 0.26f, 1.0f - 0.5f - 0.26f, 0.04f).color(color2.getRed(), color2.getGreen(), color2.getBlue(), 130).uv(1.0F, 0.0F).overlayCoords(NO_OVERLAY).uv2(p_114490_).endVertex();
        bufferBuilder.vertex(matrix4f, (0f - 0.5f) + 0.26f, 1.0f - 0.5f - 0.26f, 0.04f).color(color2.getRed(), color2.getGreen(), color2.getBlue(), 130).uv(0.0F, 0.0F).overlayCoords(NO_OVERLAY).uv2(p_114490_).endVertex();

        bufferBuilder.vertex(matrix4f, (0f - 0.5f) + 0.4f, (0.0f - 0.5f) + 0.4f, 0.05f).color(color2.getRed(), color2.getGreen(), color2.getBlue(), 80).uv(0.0F, 1.0F).overlayCoords(NO_OVERLAY).uv2(p_114490_).endVertex();
        bufferBuilder.vertex(matrix4f, 1f - 0.5f - 0.4f, (0.0f - 0.5f) + 0.4f, 0.05f).color(color2.getRed(), color2.getGreen(), color2.getBlue(), 80).uv(1.0F, 1.0F).overlayCoords(NO_OVERLAY).uv2(p_114490_).endVertex();
        bufferBuilder.vertex(matrix4f, 1f - 0.5f - 0.4f, 1.0f - 0.5f - 0.4f, 0.05f).color(color2.getRed(), color2.getGreen(), color2.getBlue(), 80).uv(1.0F, 0.0F).overlayCoords(NO_OVERLAY).uv2(p_114490_).endVertex();
        bufferBuilder.vertex(matrix4f, (0f - 0.5f) + 0.4f, 1.0f - 0.5f - 0.4f, 0.05f).color(color2.getRed(), color2.getGreen(), color2.getBlue(), 80).uv(0.0F, 0.0F).overlayCoords(NO_OVERLAY).uv2(p_114490_).endVertex();
        poseStack.popPose();

        RenderUtils.drawStreak(soulEntity, pTicks, poseStack, bufferSource);

        super.render(soulEntity, p_114486_, pTicks, poseStack, bufferSource, p_114490_);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(SoulEntity soul) {
        return new ResourceLocation(MODID, "textures/particle/wisp.png");
    }
}
