package net.pkhapps.dart.modules.alerting.integration.messages.types;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DispatchInformation {

    @JsonProperty
    public String ticketId;

    @JsonProperty
    public Instant ticketOpened;

    @JsonProperty
    public String ticketTypeCode;

    @JsonProperty
    public String ticketTypeDescription;

    @JsonProperty
    public Coordinates coordinates;

    @JsonProperty
    public String municipality;

    @JsonProperty
    public String streetOrAddressPointName;

    @JsonProperty
    public String intersectingStreetName;

    @JsonProperty
    public String streetOrAddressPointNumber;

    @JsonProperty
    public List<String> resources;

    @JsonProperty
    public String details;
}
