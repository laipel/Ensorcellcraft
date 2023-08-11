package com.laipel.ensorcellcraft.utils;

import com.laipel.ensorcellcraft.api.IStreakable;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.laipel.ensorcellcraft.client.render.renderTypes.EnsRenderType.getLightColor;
import static com.laipel.ensorcellcraft.utils.VectorUtils.Y_VEC;

public class RenderUtils {

    public static void drawStreak(IStreakable<? extends Entity> streakable, float pTicks, PoseStack poseStack, MultiBufferSource bufferSourceList) {

        ClientLevel world = Minecraft.getInstance().level;
        Entity entity = streakable.get();
        List<IStreakable.Streak> streaks = streakable.getStreaks();
        CompoundTag persistentData = entity.getPersistentData();

        double d0 = Mth.lerp(pTicks, entity.xOld, entity.getX());
        double d1 = Mth.lerp(pTicks, entity.yOld, entity.getY());
        double d2 = Mth.lerp(pTicks, entity.zOld, entity.getZ());

        if (world == null)
            return;
        long time = world.dayTime();
        if (streaks.isEmpty())
            return;

        for (int j = 0; j < streaks.size(); j++) {

            IStreakable.Streak streak = streaks.get(j);
            CompoundTag nbttc = persistentData.getCompound("Ens:Streak" + j);
            int segmentsDisappearingSpeed = 5;
            int segments = streak.getSegments() - Math.max(0,
                    streak.getSegments() - streakable.ticksBeforeDeath() / segmentsDisappearingSpeed);

            if (segments < 0 || streak.getSegmentsLife() <= 0)
                continue;
            List<Vec3> savedPoss = new ArrayList<>();
            List<Vec3> prevPoses = new ArrayList<>();
            boolean anotherTick = nbttc.getLong("time") != time;
            float partial = (int) (time % streak.getSegmentsLife()) + pTicks;

            Vec3 vecOld = new Vec3(entity.xo, entity.yo, entity.zo);
            Vec3 matrixTranslation = new Vec3(d0, d1, d2);

            for (int i = 0; nbttc.contains(String.valueOf(i)) && i <= segments + 1; i++)
                savedPoss.add(VectorUtils.loadFromNBT(String.valueOf(i), nbttc));

            if (time % streak.getSegmentsLife() == 0 && anotherTick) {
                nbttc.putLong("time", time);
                if (savedPoss.size() >= segments + 1) {
                    Collections.rotate(savedPoss, 1);
                    savedPoss.set(0, vecOld);
                } else {
                    savedPoss.add(vecOld);
                    Collections.rotate(savedPoss, 1);
                }
            }

            prevPoses.add(new Vec3(0, 0, 0));

            for (int i = 0; i < savedPoss.size(); i++) {
                Vec3 pos = savedPoss.get(i);
                if (time % streak.getSegmentsLife() == 0 && anotherTick)
                    VectorUtils.saveToNBT(String.valueOf(i), nbttc, pos);
                if (i == 0)
                    continue;
                Vec3 translated = pos.subtract(matrixTranslation);
                Vec3 translatedPrev = savedPoss.get(i - 1).subtract(matrixTranslation);
                Vec3 toAdd = translatedPrev.add(translated.subtract(translatedPrev).scale(1 - partial / (float) streak.getSegmentsLife()));
                prevPoses.add(toAdd);
            }

            float deathPartial = (segmentsDisappearingSpeed - streakable.ticksBeforeDeath() % segmentsDisappearingSpeed) - 1 + pTicks;

            if (streak.getSegments() > segments) {
                for (int i = prevPoses.size() - 1; i > 0; i--) {
                    Vec3 pos = prevPoses.get(i);
                    Vec3 prev = prevPoses.get(i - 1);
                    Vec3 toAdd = prev.add(pos.subtract(prev).scale(1 - deathPartial / (float) segmentsDisappearingSpeed));
                    prevPoses.set(i, toAdd);
                }
            }


            persistentData.put("Ens:Streak" + j, nbttc);

            Vec3[][] crossVecs = new Vec3[prevPoses.size()][2];

            for (int i = 1; i < prevPoses.size(); i++) {
                Vec3 pos1 = prevPoses.get(i - 1);
                Vec3 pos2 = prevPoses.get(i);
                Vec3 vec1 = pos2.subtract(pos1);
                Vec3 notScaled = vec1.normalize().equals(new Vec3(0, 1, 0))
                        ? vec1.add(0.0001, 0, 0).cross(Y_VEC)
                        : vec1.cross(Y_VEC).normalize();
                double len = 1 - (i - 1) / (float) prevPoses.size();
                crossVecs[i - 1][0] = notScaled.normalize()
                        .scale(streak.getWidth() * len);
                Vec3 axis = prevPoses.get(i - 1).subtract(prevPoses.get(i));
                crossVecs[i - 1][1] = VectorUtils.rotate(crossVecs[i - 1][0], axis, 90).normalize().scale(crossVecs[i - 1][0].length());
            }

            if (streak.getSegments() > segments) {
                for (int i = prevPoses.size() - 2; i > 0; i--) {
                    double len = 1 - i / (float) prevPoses.size();
                    double prevLen = 1 - (i - 1) / (float) prevPoses.size(); // prevLen > len
                    double finalLen = Mth.lerp(deathPartial / (float) segmentsDisappearingSpeed, len, prevLen);
                    crossVecs[i][0] = crossVecs[i][0].normalize().scale(streak.getWidth() * finalLen);
                    crossVecs[i][1] = crossVecs[i][1].normalize().scale(streak.getWidth() * finalLen);
                }
            }

            Color color1 = new Color(streak.getFirstColor().getRed(), streak.getFirstColor().getGreen(), streak.getFirstColor().getBlue(), (int) (streak.getStartAlpha() * 255));
            Color color2 = new Color(streak.getSecondColor().getRed(), streak.getSecondColor().getGreen(), streak.getSecondColor().getBlue(), (int) (streak.getFinalAlpha() * 255));

            poseStack.pushPose();
            Matrix4f matrix4f = poseStack.last().pose();

            VertexConsumer tes = bufferSourceList.getBuffer(getLightColor());
            for (int i = 0; i < prevPoses.size(); i++) {
                if (crossVecs[i][0] == null)
                    break;
                Color c1 = ColorUtils.blend(color1, color2, ((float) i) / (prevPoses.size() - 1));
                Color c2 = ColorUtils.blend(color1, color2, ((float) i + 1) / (prevPoses.size() - 1));

                if (i == crossVecs.length - 1 || crossVecs[i + 1][0] == null) {
                    Vec3 pos11 = prevPoses.get(i).add(crossVecs[i][0]);
                    Vec3 pos12 = prevPoses.get(i).add(crossVecs[i][1]);
                    Vec3 pos13 = prevPoses.get(i).add(crossVecs[i][0].scale(-1));
                    Vec3 pos14 = prevPoses.get(i).add(crossVecs[i][1].scale(-1));
                    Vec3 pos2 = prevPoses.get(i + 1);

                    TesselatorUtils.drawQuadGradient(tes, matrix4f, pos12.x, pos12.y, pos12.z,
                            pos2.x, pos2.y, pos2.z, pos2.x, pos2.y, pos2.z, pos13.x, pos13.y, pos13.z, c1, c2);
                    TesselatorUtils.drawQuadGradient(tes, matrix4f, pos11.x, pos11.y, pos11.z, pos2.x, pos2.y,
                            pos2.z, pos2.x, pos2.y, pos2.z, pos12.x, pos12.y, pos12.z, c1, c2);
                    TesselatorUtils.drawQuadGradient(tes, matrix4f, pos13.x, pos13.y, pos13.z, pos2.x, pos2.y,
                            pos2.z, pos2.x, pos2.y, pos2.z, pos14.x, pos14.y, pos14.z, c1, c2);
                    TesselatorUtils.drawQuadGradient(tes, matrix4f, pos14.x, pos14.y, pos14.z, pos2.x, pos2.y,
                            pos2.z, pos2.x, pos2.y, pos2.z, pos11.x, pos11.y, pos11.z, c1, c2);
                } else {

                    Vec3 pos11 = prevPoses.get(i).add(crossVecs[i][0]);
                    Vec3 pos12 = prevPoses.get(i).add(crossVecs[i][1]);
                    Vec3 pos13 = prevPoses.get(i).add(crossVecs[i][0].scale(-1));
                    Vec3 pos14 = prevPoses.get(i).add(crossVecs[i][1].scale(-1));
                    Vec3 pos21 = prevPoses.get(i + 1).add(crossVecs[i + 1][0]);
                    Vec3 pos22 = prevPoses.get(i + 1).add(crossVecs[i + 1][1]);
                    Vec3 pos23 = prevPoses.get(i + 1).add(crossVecs[i + 1][0].scale(-1));
                    Vec3 pos24 = prevPoses.get(i + 1).add(crossVecs[i + 1][1].scale(-1));

                    if (i == 0) {
                        TesselatorUtils.drawQuadGradient(tes, matrix4f, pos11.x, pos11.y, pos11.z, pos12.x, pos12.y,
                                pos12.z, pos13.x, pos13.y, pos13.z, pos14.x, pos14.y, pos14.z, color1, color1);
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

}
