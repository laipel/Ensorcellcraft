package com.laipel.ensorcellcraft.api;

import lombok.Builder;
import lombok.Getter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public interface IStreakable<T extends Entity> {

    @NotNull T get();

    int ticksBeforeDeath();

    @NotNull Streak getStreak();

    @NotNull StreakBuffer getStreakBuffer();

    @Builder
    @Getter
    final class Streak {

        // Width of segments
        private float width;

        // Number of segments in the streak
        @Builder.Default
        private int segments = 4;

        // Lifetime of segments
        @Builder.Default
        private int segmentsLife = 40;

        // The first color of the streak
        @Builder.Default
        private Color firstColor = Color.WHITE;

        // The second color of the streak
        @Builder.Default
        private Color secondColor = Color.WHITE;

        // Start color of streak
        @Builder.Default
        private float startAlpha = 1;

        // End color of streak
        @Builder.Default
        private float finalAlpha = 0.5f;

        // If after-entity-death streak will generate particles
        @Builder.Default
        private boolean particlesAfterDeath = false;

    }

    class StreakBuffer {

        long time = 0;
        Vec3[] poses;
        int size;

        public StreakBuffer(int size) {
            poses = new Vec3[size + 1];
            this.size = size;
        }

        public boolean contains(int index) {
            return poses.length > index && poses[index] != null;
        }

        public Vec3 get(int index) {
            return poses[index];
        }

        public void put(int index, Vec3 vec3) {
            poses[index] = vec3;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }


    }
}
