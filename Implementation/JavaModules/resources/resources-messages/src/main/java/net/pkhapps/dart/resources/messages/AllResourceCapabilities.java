package net.pkhapps.dart.resources.messages;

import net.pkhapps.dart.common.CollectionsUtil;
import net.pkhapps.dart.common.LocalizedString;
import net.pkhapps.dart.messaging.messages.Response;

import java.time.Instant;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class AllResourceCapabilities extends Response {

    private final Set<ResourceCapability> capabilities;

    public AllResourceCapabilities(Instant timestamp, Set<ResourceCapability> capabilities) {
        super(timestamp, null);
        this.capabilities = CollectionsUtil.unmodifiableCopy(capabilities);
    }

    public Collection<ResourceCapability> getCapabilities() {
        return capabilities;
    }

    public static class ResourceCapability {

        private final String capability;
        private final LocalizedString description;

        public ResourceCapability(String capability, LocalizedString description) {
            this.capability = Objects.requireNonNull(capability, "capability must not be null");
            this.description = Objects.requireNonNull(description, "description must not be null");
        }

        public String getCapability() {
            return capability;
        }

        public LocalizedString getDescription() {
            return description;
        }
    }
}
