package net.pkhapps.dart.messaging.common.types;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public interface Coordinates {

    @NotNull
    BigDecimal getLatitude();

    @NotNull
    BigDecimal getLongitude();
}
