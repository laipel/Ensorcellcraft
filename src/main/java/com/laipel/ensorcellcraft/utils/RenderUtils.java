package com.laipel.ensorcellcraft.utils;

import com.laipel.ensorcellcraft.common.registry.ShaderRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import lombok.Builder;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.List;

import static com.laipel.ensorcellcraft.utils.VectorUtils.Y_VEC;

public class RenderUtils {

    public static final RenderType DEFAULT_COLOR = RenderType.create("default_color_tex", DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder()
                    .setLightmapState(new RenderStateShard.LightmapStateShard(false))
                    .setTransparencyState(new RenderStateShard.TransparencyStateShard("custom_transparancy", () -> {
                        RenderSystem.enableBlend();
                        RenderSystem.defaultBlendFunc();
                    }, () -> {
                        RenderSystem.disableBlend();
                        RenderSystem.defaultBlendFunc();
                    }))
                    .setCullState(new RenderStateShard.CullStateShard(false))
                    .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorShader))
                    .createCompositeState(false));

    public static void drawStreak(Entity entity, float pTicks, PoseStack poseStack, MultiBufferSource bufferSourceList, List<Streak> streaks) {

        ClientLevel world = Minecraft.getInstance().level;
        if (world == null)
            return;
        long time = world.dayTime();
        if (streaks.isEmpty())
            return;
        for (int j = 0; j < streaks.size(); j++) {
            Streak streak = streaks.get(j);
            CompoundTag persistentData = entity.getPersistentData();
            CompoundTag nbttc = persistentData.getCompound("Ens:streak" + j);
            int segments = 0;
            if (streak.getSegments() <= 0 || streak.getSegmentsLife() <= 0)
                continue;
            Vec3[] prevPoss = new Vec3[streak.getSegments() + 1];
            int tick = (int) (time % streak.getSegmentsLife());
            if (time % streak.getSegmentsLife() == 0 && nbttc.getLong("time") != time) {
                nbttc.putLong("time", time);
                for (int i = streak.getSegments() + 1; i >= 0; i--) {
                    if (i == 0) {
                        nbttc.putDouble("x" + i, entity.xo);
                        nbttc.putDouble("y" + i, entity.yo);
                        nbttc.putDouble("z" + i, entity.zo);
                    } else if (nbttc.contains("x" + (i - 1))) {
                        double x = nbttc.getDouble("x" + (i - 1));
                        double y = nbttc.getDouble("y" + (i - 1));
                        double z = nbttc.getDouble("z" + (i - 1));
                        nbttc.putDouble("x" + i, x);
                        nbttc.putDouble("y" + i, y);
                        nbttc.putDouble("z" + i, z);
                    }
                }

                persistentData.put("Ens:streak" + j, nbttc);
            }

            prevPoss[0] = new Vec3(
                    entity.xo + (entity.position().x - entity.xo) * pTicks,
                    entity.yo + (entity.position().y - entity.yo) * pTicks,
                    entity.zo + (entity.position().z - entity.zo) * pTicks);

            for (int i = 1; i <= streak.getSegments() + 1; i++) {
                if (!nbttc.contains("x" + (i - 1)))
                    return;
                double x = nbttc.getDouble("x" + (i - 1));
                double y = nbttc.getDouble("y" + (i - 1));
                double z = nbttc.getDouble("z" + (i - 1));
                if (i == streak.getSegments() + 1) {
                    Vec3 vec = new Vec3(x, y, z);
                    prevPoss[i - 1] = prevPoss[i - 1].add(vec.subtract(prevPoss[i - 1])
                            .scale(1 - (tick + pTicks) / (float) streak.getSegmentsLife()));
                    continue;
                }
                segments++;
                prevPoss[i] = new Vec3(x, y, z);
                if (nbttc.contains("x" + i))
                    prevPoss[i - 1] = prevPoss[i - 1].add(prevPoss[i].subtract(prevPoss[i - 1])
                            .scale(1 - (tick + pTicks) / (float) streak.getSegmentsLife()));
            }

            Vec3[] crossVecs = new Vec3[segments];

            for (int i = 1; i < segments + 1; i++) {
                if (streak.getSegments() < 2)
                    break;
                if (prevPoss[i] == null) {
                    break;
                }
                Vec3 pos1 = prevPoss[i - 1];
                Vec3 pos2 = prevPoss[i];
                Vec3 vec1 = pos1.subtract(pos2);
                if (vec1.normalize().equals(new Vec3(0, 1, 0)))
                    crossVecs[i - 1] = new Vec3(streak.getWidth() * (1 - (i - 1) / (float) segments), 0, 0);
                else
                    crossVecs[i - 1] = vec1.cross(Y_VEC).normalize()
                            .scale(streak.getWidth() * (1 - (i - 1) / (float) segments));
            }

            Color color1 = new Color(streak.getFirstColor().getRed(), streak.getFirstColor().getGreen(), streak.getFirstColor().getRed(), (int) (streak.getStartAlpha() * 255));
            Color color2 = new Color(streak.getSecondColor().getRed(), streak.getSecondColor().getGreen(), streak.getSecondColor().getRed(), (int) (streak.getFinalAlpha() * 255));

            poseStack.pushPose();
            Matrix4f matrix4f = poseStack.last().pose();

            VertexConsumer tes = bufferSourceList.getBuffer(DEFAULT_COLOR);
            for (int i = 0; i < crossVecs.length; i++) {
                if (crossVecs[i] == null)
                    break;
                Color c1 = ColorUtils.blend(color2, color1, ((float) i) / crossVecs.length);
                Color c2 = ColorUtils.blend(color2, color1, ((float) i + 1) / crossVecs.length);
                Vec3 axis1 = prevPoss[i].subtract(prevPoss[i + 1]);

                Vec3 vec1 = VectorUtils.rotate(crossVecs[i], axis1, 90);
                if (i == crossVecs.length - 1 || crossVecs[i + 1] == null) {
                    Vec3 pos11 = prevPoss[i].add(crossVecs[i]);
                    Vec3 pos12 = prevPoss[i].add(vec1);
                    Vec3 pos13 = prevPoss[i].add(crossVecs[i].scale(-1));
                    Vec3 pos14 = prevPoss[i].add(vec1.scale(-1));
                    Vec3 pos2 = prevPoss[i + 1];

                    TesselatorUtils.drawQuadGradient(tes, matrix4f, pos11.x, pos11.y, pos11.z, pos2.x, pos2.y,
                            pos2.z, pos2.x, pos2.y, pos2.z, pos12.x, pos12.y, pos12.z, c1, c2);
                    TesselatorUtils.drawQuadGradient(tes, matrix4f, pos12.x, pos12.y, pos12.z, pos2.x, pos2.y,
                            pos2.z, pos2.x, pos2.y, pos2.z, pos13.x, pos13.y, pos13.z, c1, c2);
                    TesselatorUtils.drawQuadGradient(tes, matrix4f, pos13.x, pos13.y, pos13.z, pos2.x, pos2.y,
                            pos2.z, pos2.x, pos2.y, pos2.z, pos14.x, pos14.y, pos14.z, c1, c2);
                    TesselatorUtils.drawQuadGradient(tes, matrix4f, pos14.x, pos14.y, pos14.z, pos2.x, pos2.y,
                            pos2.z, pos2.x, pos2.y, pos2.z, pos11.x, pos11.y, pos11.z, c1, c2);
                } else {
                    Vec3 axis2 = prevPoss[i + 1].subtract(prevPoss[i + 2]);
                    Vec3 vec2 = VectorUtils.rotate(crossVecs[i], axis2, 90);

                    Vec3 pos11 = prevPoss[i].add(crossVecs[i]);
                    Vec3 pos12 = prevPoss[i].add(vec1);
                    Vec3 pos13 = prevPoss[i].add(crossVecs[i].scale(-1));
                    Vec3 pos14 = prevPoss[i].add(vec1.scale(-1));
                    Vec3 pos21 = prevPoss[i + 1].add(crossVecs[i + 1]);
                    Vec3 pos22 = prevPoss[i + 1].add(vec2);
                    Vec3 pos23 = prevPoss[i + 1].add(crossVecs[i + 1].scale(-1));
                    Vec3 pos24 = prevPoss[i + 1].add(vec2.scale(-1));

                    if (i == 0) {
                        TesselatorUtils.drawFullQuadWithColor(tes, matrix4f, pos11.x, pos11.y, pos11.z, pos12.x, pos12.y,
                                pos12.z, pos13.x, pos13.y, pos13.z, pos14.x, pos14.y, pos14.z, color1);
                    }

                    TesselatorUtils.drawQuadGradient(tes, matrix4f, pos11.x, pos11.y, pos11.z, pos21.x, pos21.y,
                            pos21.z, pos22.x, pos22.y, pos22.z, pos12.x, pos12.y, pos12.z, c1, c2);
                    TesselatorUtils.drawQuadGradient(tes, matrix4f, pos12.x, pos12.y, pos12.z, pos22.x, pos22.y,
                            pos22.z, pos23.x, pos23.y, pos23.z, pos13.x, pos13.y, pos13.z, c1, c2);
                    TesselatorUtils.drawQuadGradient(tes, matrix4f, pos13.x, pos13.y, pos13.z, pos23.x, pos23.y,
                            pos23.z, pos24.x, pos24.y, pos24.z, pos14.x, pos14.y, pos14.z, c1, c2);
                    TesselatorUtils.drawQuadGradient(tes, matrix4f, pos14.x, pos14.y, pos14.z, pos24.x, pos24.y,
                            pos24.z, pos21.x, pos21.y, pos21.z, pos11.x, pos11.y, pos11.z, c1, c2);
                }
            }

            poseStack.popPose();
        }
    }

    @Builder
    @Getter
    public static final class Streak {

        // Width of segments
        float width;

        // Number of segments in the streak
        @Builder.Default
        int segments = 4;

        // Lifetime of segments
        @Builder.Default
        int segmentsLife = 40;

        // The first color of the streak
        @Builder.Default
        Color firstColor = Color.WHITE;

        // The second color of the streak
        @Builder.Default
        Color secondColor = Color.WHITE;

        // Start color of streak
        @Builder.Default
        float startAlpha = 1;

        // End color of streak
        @Builder.Default
        float finalAlpha = 0.5f;

        // If after-entity-death streak will generate particles
        @Builder.Default
        boolean particlesAfterDeath = false;

    }

}
