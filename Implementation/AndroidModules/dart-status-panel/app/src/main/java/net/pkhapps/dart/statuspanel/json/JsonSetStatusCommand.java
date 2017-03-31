package net.pkhapps.dart.statuspanel.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by peholmst on 31/03/2017.
 */
@JsonTypeName("set-status-command")
public class JsonSetStatusCommand extends JsonMessage {

    @JsonProperty
    private long newStatusId;

    public JsonSetStatusCommand() {
    }

    public JsonSetStatusCommand(long newStatusId) {
        this.newStatusId = newStatusId;
    }

    public long getNewStatusId() {
        return newStatusId;
    }

    public void setNewStatusId(long newStatusId) {
        this.newStatusId = newStatusId;
    }
}
