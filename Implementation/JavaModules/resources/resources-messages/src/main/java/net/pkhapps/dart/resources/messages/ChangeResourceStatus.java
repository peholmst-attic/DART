package net.pkhapps.dart.resources.messages;

import net.pkhapps.dart.common.Coordinates;
import net.pkhapps.dart.messaging.messages.FireAndForgetCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public class ChangeResourceStatus extends FireAndForgetCommand {

    private final String resource;
    private final String state;
    private final Coordinates location;

    public ChangeResourceStatus(@NotNull Instant timestamp, @NotNull String resource, @Nullable String state, @Nullable Coordinates location) {
        super(timestamp, null);
        this.resource = Objects.requireNonNull(resource, "resource must not be null");
        this.state = state;
        this.location = location;
    }

    @NotNull
    public String getResource() {
        return resource;
    }

    @NotNull
    public Optional<String> getState() {
        return Optional.ofNullable(state);
    }

    @NotNull
    public Optional<Coordinates> getLocation() {
        return Optional.ofNullable(location);
    }
}
