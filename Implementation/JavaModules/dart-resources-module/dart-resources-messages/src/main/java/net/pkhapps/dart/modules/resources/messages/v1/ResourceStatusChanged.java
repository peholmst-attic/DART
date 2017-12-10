package net.pkhapps.dart.modules.resources.messages.v1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class ResourceStatusChanged {

    @JsonProperty
    private final String callSign;

    @JsonProperty
    private final Instant timestamp;

    @JsonProperty
    private final String statusCode;

    @JsonCreator
    public ResourceStatusChanged(String callSign, Instant timestamp, String statusCode) {
        this.callSign = callSign;
        this.timestamp = timestamp;
        this.statusCode = statusCode;
    }

    public String getCallSign() {
        return callSign;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getStatusCode() {
        return statusCode;
    }
}
