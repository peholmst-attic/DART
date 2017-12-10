package net.pkhapps.dart.modules.dispatch.messages.v1.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketDto {

    @JsonProperty
    public String id;
    @JsonProperty
    public Long version;
    @JsonProperty
    public Instant created;
    @JsonProperty
    public String createdByUserId;
    @JsonProperty
    public Instant lastModified;
    @JsonProperty
    public String lastModifiedByUserId;
    @JsonProperty
    public TicketTypeDto type;
    @JsonProperty
    public String urgency;
    @JsonProperty
    public String state;
    @JsonProperty
    public String details;
    @JsonProperty
    public String reporter;
    @JsonProperty
    public String reporterPhone;

    // TODO Resources
    // TODO Address
}
