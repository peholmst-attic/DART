package net.pkhapps.dart.resources.messages;

import net.pkhapps.dart.common.CollectionsUtil;
import net.pkhapps.dart.common.Coordinates;
import net.pkhapps.dart.common.TemporalData;
import net.pkhapps.dart.messaging.messages.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class CurrentResourceStatus extends Response {

    private final Set<ResourceStatus> status;

    public CurrentResourceStatus(@NotNull Instant timestamp, @NotNull Set<ResourceStatus> status) {
        super(timestamp, null);
        this.status = CollectionsUtil.unmodifiableCopy(status);
    }

    @NotNull
    public Collection<ResourceStatus> getStatus() {
        return status;
    }

    public static class ResourceStatus {

        private final String resource;
        private final TemporalData<String> state;
        private final TemporalData<Coordinates> location;

        public ResourceStatus(@NotNull String resource, @Nullable TemporalData<String> state, @Nullable TemporalData<Coordinates> location) {
            this.resource = Objects.requireNonNull(resource, "resource must not be null");
            this.state = state;
            this.location = location;
        }

        @NotNull
        public String getResource() {
            return resource;
        }

        @NotNull
        public Optional<TemporalData<String>> getState() {
            return Optional.ofNullable(state);
        }

        @NotNull
        public Optional<TemporalData<Coordinates>> getLocation() {
            return Optional.ofNullable(location);
        }
    }
}
