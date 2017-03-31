package net.pkhapps.dart.statuspanel.json;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.pkhapps.dart.statuspanel.model.IStatusDescriptor;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by peholmst on 31/03/2017.
 */

public class JsonStatusDescriptor implements IStatusDescriptor {

    @JsonProperty
    private long id;

    @JsonProperty
    private String caption;

    @JsonProperty
    private int backgroundColor;

    @JsonProperty
    private boolean locationBroadcastEnabled;

    @JsonProperty
    private boolean userSelectable;

    @JsonProperty
    private Set<Long> validTransitions = new HashSet<>();

    @Override
    public long getId() {
        return id;
    }

    @NonNull
    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public boolean isLocationBroadcastEnabled() {
        return locationBroadcastEnabled;
    }

    @Override
    public boolean isUserSelectable() {
        return userSelectable;
    }

    @NonNull
    @Override
    public Set<Long> getValidTransitions() {
        return validTransitions;
    }
}
