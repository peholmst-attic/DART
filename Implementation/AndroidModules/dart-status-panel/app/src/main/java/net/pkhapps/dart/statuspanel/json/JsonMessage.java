package net.pkhapps.dart.statuspanel.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Created by peholmst on 31/03/2017.
 */
@JsonTypeInfo(include = JsonTypeInfo.As.PROPERTY, use = JsonTypeInfo.Id.NAME, property = "@message-type")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(JsonGetStatusDescriptorsRequest.class),
        @JsonSubTypes.Type(JsonGetStatusDescriptorsResponse.class),
        @JsonSubTypes.Type(JsonSetStatusCommand.class)
})
public abstract class JsonMessage {

    @JsonProperty("@message-version")
    private String version = "1.0";

    public String getVersion() {
        return version;
    }
}
