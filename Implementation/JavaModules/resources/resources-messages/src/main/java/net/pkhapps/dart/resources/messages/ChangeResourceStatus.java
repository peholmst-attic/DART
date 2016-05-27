package net.pkhapps.dart.resources.messages;

import net.pkhapps.dart.common.Coordinates;
import net.pkhapps.dart.messaging.messages.FireAndForgetCommand;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public class ChangeResourceStatus extends FireAndForgetCommand {

    private final String resource;
    private final String state;
    private final Coordinates location;

    public ChangeResourceStatus(Instant timestamp, String resource, String state, Coordinates location) {
        super(timestamp, null);
        this.resource = Objects.requireNonNull(resource);
        this.state = state;
        this.location = location;
    }

    public String getResource() {
        return resource;
    }

    public Optional<String> getState() {
        return Optional.ofNullable(state);
    }

    public Optional<Coordinates> getLocation() {
        return Optional.ofNullable(location);
    }
}
