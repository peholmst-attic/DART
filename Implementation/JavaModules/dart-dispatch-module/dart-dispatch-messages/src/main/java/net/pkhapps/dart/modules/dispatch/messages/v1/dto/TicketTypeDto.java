package net.pkhapps.dart.modules.dispatch.messages.v1.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketTypeDto {

    @JsonProperty
    public String code;
    @JsonProperty
    public String descriptionFi;
    @JsonProperty
    public String descriptionSv;
    @JsonProperty
    public boolean active;
}
