package net.pkhapps.dart.statuspanel.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.List;

/**
 * Created by peholmst on 31/03/2017.
 */
@JsonTypeName("get-status-descriptors-response")
public class JsonGetStatusDescriptorsResponse extends JsonMessage {

    @JsonProperty
    private List<JsonStatusDescriptor> descriptors;

    public List<JsonStatusDescriptor> getDescriptors() {
        return descriptors;
    }
}
