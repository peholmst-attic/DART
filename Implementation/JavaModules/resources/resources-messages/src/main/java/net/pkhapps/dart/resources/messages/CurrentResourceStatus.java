package net.pkhapps.dart.resources.messages;

import net.pkhapps.dart.common.Coordinates;
import net.pkhapps.dart.messaging.messages.Response;
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
