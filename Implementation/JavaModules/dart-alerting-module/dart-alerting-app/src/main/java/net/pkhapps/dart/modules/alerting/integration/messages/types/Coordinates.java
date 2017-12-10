package net.pkhapps.dart.modules.alerting.integration.messages.types;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Coordinates {

    @JsonProperty("lat")
    public BigDecimal latitude;

    @JsonProperty("lon")
    public BigDecimal longitude;
}
