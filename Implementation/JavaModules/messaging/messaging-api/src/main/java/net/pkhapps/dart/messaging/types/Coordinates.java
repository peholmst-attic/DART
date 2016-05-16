package net.pkhapps.dart.messaging.types;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public interface Coordinates {

    @NotNull
    BigDecimal getLatitude();

    @NotNull
    BigDecimal getLongitude();
}
