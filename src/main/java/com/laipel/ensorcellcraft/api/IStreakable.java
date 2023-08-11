package com.laipel.ensorcellcraft.api;

import lombok.Builder;
import lombok.Getter;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NonBlocking;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;

public interface IStreakable<T extends Entity> {

    @NotNull T get();

    int ticksBeforeDeath();

    @NotNull List<Streak> getStreaks();

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
}
