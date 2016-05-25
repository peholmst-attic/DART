package net.pkhapps.dart.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Color {

    private final String color;

    public Color(@NotNull String color) {
        this.color = validate(Objects.requireNonNull(color, "color must not be null"));
    }

    private String validate(String color) {
        if (color.startsWith("#")) {
            color = color.substring(1);
        }
        if (color.length() < 6) {
            throw new IllegalArgumentException("Color code is too short");
        } else if (color.length() > 6) {
            throw new IllegalArgumentException("Color code is too long");
        } else if (!color.matches("[A-Fa-f0-9]{6}")) {
            throw new IllegalArgumentException("Color code is incorrect");
        }
        return color;
    }

    @NotNull
    public String getColor() {
        return color;
    }

    @Nullable
    public static Color valueOf(@Nullable String color) {
        return color == null ? null : new Color(color);
    }
}
