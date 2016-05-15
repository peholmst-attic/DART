package net.pkhapps.dart.messaging.resources;

import net.pkhapps.dart.messaging.common.messages.Response;
import net.pkhapps.dart.messaging.common.types.Coordinates;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

public interface CurrentResourceStatus extends Response {

    @NotNull
    Collection<ResourceStatus> getStatus();

    interface ResourceStatus {
        @NotNull
        String getResource();

        @NotNull
        Optional<ResourceState> getState();

        @NotNull
        Optional<ResourceLocation> getLocation();
    }

    interface ResourceState {
        @NotNull
        String getState();

        @NotNull
        Instant getLastChanged();
    }

    interface ResourceLocation {
        @NotNull
        Coordinates getLocation();

        @NotNull
        Instant getLastChanged();
    }
}
