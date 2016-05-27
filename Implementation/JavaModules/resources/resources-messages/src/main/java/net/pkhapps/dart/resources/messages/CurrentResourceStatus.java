package net.pkhapps.dart.resources.messages;

import net.pkhapps.dart.common.CollectionsUtil;
import net.pkhapps.dart.common.Coordinates;
import net.pkhapps.dart.common.TemporalData;
import net.pkhapps.dart.messaging.messages.Response;

import java.time.Instant;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class CurrentResourceStatus extends Response {

    private final Set<ResourceStatus> status;

    public CurrentResourceStatus(Instant timestamp, Set<ResourceStatus> status) {
        super(timestamp, null);
        this.status = CollectionsUtil.unmodifiableCopy(status);
    }

    public Collection<ResourceStatus> getStatus() {
        return status;
    }

    public static class ResourceStatus {

        private final String resource;
        private final TemporalData<String> state;
        private final TemporalData<Coordinates> location;

        public ResourceStatus(String resource, TemporalData<String> state, TemporalData<Coordinates> location) {
            this.resource = Objects.requireNonNull(resource);
            this.state = state;
            this.location = location;
        }

        public String getResource() {
            return resource;
        }

        public Optional<TemporalData<String>> getState() {
            return Optional.ofNullable(state);
        }

        public Optional<TemporalData<Coordinates>> getLocation() {
            return Optional.ofNullable(location);
        }
    }
}
