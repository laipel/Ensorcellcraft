package com.laipel.ensorcellcraft.common.souls;

import com.laipel.ensorcellcraft.api.soul.ISoul;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Soul implements ISoul {

    int volume;
    Color color;

    public Soul(int volume, Color color) {
        this.volume = volume;
        this.color = color;
    }

    @Override
    public int gerVolume() {
        return volume;
    }

    @Override
    public @NotNull Color getColor() {
        return color;
    }

}
