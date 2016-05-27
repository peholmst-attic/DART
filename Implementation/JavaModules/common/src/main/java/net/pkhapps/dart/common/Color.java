package net.pkhapps.dart.common;

import java.util.Objects;

public class Color {

    private final String color;

    public Color(String color) {
        this.color = validate(Objects.requireNonNull(color));
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

    public String getColor() {
        return color;
    }

    public static Color valueOf(String color) {
        return color == null ? null : new Color(color);
    }
}
