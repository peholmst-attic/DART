package net.pkhapps.dart.types;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;

public class Coordinates implements Serializable {

    private static final MathContext MATH_CONTEXT = new MathContext(9);
    private final BigDecimal latitude;
    private final BigDecimal longitude;

    public Coordinates(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude.round(MATH_CONTEXT);
        this.longitude = longitude.round(MATH_CONTEXT);
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return String.format("Coordinates(lat=%f, lon=%f)", latitude, longitude);
    }
}
