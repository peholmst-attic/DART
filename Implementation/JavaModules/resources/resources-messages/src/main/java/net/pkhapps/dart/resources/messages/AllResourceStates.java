package net.pkhapps.dart.resources.messages;

import net.pkhapps.dart.common.CollectionsUtil;
import net.pkhapps.dart.common.Color;
import net.pkhapps.dart.common.LocalizedString;
import net.pkhapps.dart.messaging.messages.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class AllResourceStates extends Response {

    private final Set<ResourceState> states;

    public AllResourceStates(@NotNull Instant timestamp, @NotNull Set<ResourceState> states) {
        super(timestamp, null);
        this.states = CollectionsUtil.unmodifiableCopy(states);
    }

    @NotNull
    public Collection<ResourceState> getStates() {
        return states;
    }

    public static class ResourceState {

        private final String state;
        private final LocalizedString description;
        private final boolean locationTrackingEnabled;
        private final Color color;

        public ResourceState(@NotNull String state, @NotNull LocalizedString description, boolean locationTrackingEnabled, @Nullable Color color) {
            this.state = Objects.requireNonNull(state, "state must not be null");
            this.description = Objects.requireNonNull(description, "description must not be null");
            this.locationTrackingEnabled = locationTrackingEnabled;
            this.color = color;
        }

        public ResourceState(String state, LocalizedString description, boolean locationTrackingEnabled) {
            this(state, description, locationTrackingEnabled, null);
        }

        @NotNull
        public String getState() {
            return state; // TODO Should this return an enum instead of a String?
        }

        @NotNull
        public LocalizedString getDescription() {
            return description;
        }

        public boolean isLocationTrackingEnabled() {
            return locationTrackingEnabled;
        }

        @NotNull
        public Optional<Color> getColor() {
            return Optional.ofNullable(color);
        }
    }
}
