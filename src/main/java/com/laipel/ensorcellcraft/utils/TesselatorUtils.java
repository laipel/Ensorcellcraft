package com.laipel.ensorcellcraft.utils;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;

import java.awt.*;

public class TesselatorUtils {

    public static void drawFullQuadWithColor(VertexConsumer tes, Matrix4f matrix4f, double pos1X, double pos1Y, double pos1Z, double pos2X,
                                    double pos2Y, double pos2Z, double pos3X, double pos3Y, double pos3Z, double pos4X, double pos4Y,
                                    double pos4Z, Color color) {

        if (matrix4f != null) {
            tes.vertex(matrix4f, (float) pos1X, (float) pos1Y, (float) pos1Z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                    .endVertex();
            tes.vertex(matrix4f, (float) pos2X, (float) pos2Y, (float) pos2Z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
            tes.vertex(matrix4f, (float) pos3X, (float) pos3Y, (float) pos3Z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
            tes.vertex(matrix4f, (float) pos4X, (float) pos4Y, (float) pos4Z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();

            tes.vertex(matrix4f, (float) pos4X, (float) pos4Y, (float) pos4Z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
            tes.vertex(matrix4f, (float) pos3X, (float) pos3Y, (float) pos3Z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
            tes.vertex(matrix4f, (float) pos2X, (float) pos2Y, (float) pos2Z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
            tes.vertex(matrix4f, (float) pos1X, (float) pos1Y, (float) pos1Z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        } else {
            tes.vertex( pos1X,  pos1Y,  pos1Z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
            tes.vertex( pos2X,  pos2Y,  pos2Z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
            tes.vertex( pos3X,  pos3Y,  pos3Z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
            tes.vertex( pos4X,  pos4Y,  pos4Z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();

            tes.vertex( pos4X,  pos4Y,  pos4Z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
            tes.vertex( pos3X,  pos3Y,  pos3Z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
            tes.vertex( pos2X,  pos2Y,  pos2Z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
            tes.vertex( pos1X,  pos1Y,  pos1Z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        }

    }

    public static void drawQuad(VertexConsumer tes, double pos1X, double pos1Y, double pos1Z, double pos2X,
                                double pos2Y, double pos2Z, double pos3X, double pos3Y, double pos3Z, double pos4X, double pos4Y,
                                double pos4Z) {

        tes.vertex(pos1X, pos1Y, pos1Z).endVertex();
        tes.vertex(pos2X, pos2Y, pos2Z).endVertex();
        tes.vertex(pos3X, pos3Y, pos3Z).endVertex();
        tes.vertex(pos4X, pos4Y, pos4Z).endVertex();

    }

    /* public static void drawQuad(BufferBuilder tes, IIcon icon, double pos1X, double pos1Y, double pos1Z, double pos2X,
                                double pos2Y, double pos2Z, double pos3X, double pos3Y, double pos3Z, double pos4X, double pos4Y,
                                double pos4Z) {

        float maxU = icon.getMaxU();
        float maxV = icon.getMaxV();
        float minU = icon.getMinU();
        float minV = icon.getMinV();

        tes.addVertexWithUV(pos1X, pos1Y, pos1Z, maxU, maxV);
        tes.addVertexWithUV(pos2X, pos2Y, pos2Z, maxU, minV);
        tes.addVertexWithUV(pos3X, pos3Y, pos3Z, minU, minV);
        tes.addVertexWithUV(pos4X, pos4Y, pos4Z, minU, maxV);

    } */

    public static void drawQuadGradient(VertexConsumer tes, Matrix4f matrix4f, double pos1X, double pos1Y, double pos1Z, double pos2X,
                                        double pos2Y, double pos2Z, double pos3X, double pos3Y, double pos3Z, double pos4X, double pos4Y,
                                        double pos4Z, Color color1, Color color2) {

        if (matrix4f != null) {
            tes.vertex(matrix4f, (float) pos4X, (float) pos4Y, (float) pos4Z).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).endVertex();
            tes.vertex(matrix4f, (float) pos1X, (float) pos1Y, (float) pos1Z).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).endVertex();
            tes.vertex(matrix4f, (float) pos2X, (float) pos2Y, (float) pos2Z).color(color2.getRed(), color2.getGreen(), color2.getBlue(), color2.getAlpha()).endVertex();
            tes.vertex(matrix4f, (float) pos3X, (float) pos3Y, (float) pos3Z).color(color2.getRed(), color2.getGreen(), color2.getBlue(), color2.getAlpha()).endVertex();
        } else {
            tes.vertex(pos4X, pos4Y, pos4Z).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).endVertex();
            tes.vertex(pos1X, pos1Y, pos1Z).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).endVertex();
            tes.vertex(pos2X, pos2Y, pos2Z).color(color2.getRed(), color2.getGreen(), color2.getBlue(), color2.getAlpha()).endVertex();
            tes.vertex(pos3X, pos3Y, pos3Z).color(color2.getRed(), color2.getGreen(), color2.getBlue(), color2.getAlpha()).endVertex();
        }
    }

}