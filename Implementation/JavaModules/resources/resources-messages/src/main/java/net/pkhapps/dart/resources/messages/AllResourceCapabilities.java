package net.pkhapps.dart.resources.messages;

import net.pkhapps.dart.common.CollectionsUtil;
import net.pkhapps.dart.common.LocalizedString;
import net.pkhapps.dart.messaging.messages.Response;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.*;

public class AllResourceCapabilities extends Response {

    private final Set<ResourceCapability> capabilities;

    public AllResourceCapabilities(@NotNull Instant timestamp, @NotNull Set<ResourceCapability> capabilities) {
        super(timestamp, null);
        this.capabilities = CollectionsUtil.unmodifiableCopy(capabilities);
    }

    @NotNull
    public Collection<ResourceCapability> getCapabilities() {
        return capabilities;
    }

    public static class ResourceCapability {

        private final String capability;
        private final LocalizedString description;

        public ResourceCapability(@NotNull String capability, @NotNull LocalizedString description) {
            this.capability = Objects.requireNonNull(capability, "capability must not be null");
            this.description = Objects.requireNonNull(description, "description must not be null");
        }

        @NotNull
        public String getCapability() {
            return capability;
        }

        @NotNull
        public LocalizedString getDescription() {
            return description;
        }
    }
}
