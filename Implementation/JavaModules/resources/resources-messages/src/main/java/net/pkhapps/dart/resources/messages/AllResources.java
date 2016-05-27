package net.pkhapps.dart.resources.messages;

import net.pkhapps.dart.common.CollectionsUtil;
import net.pkhapps.dart.messaging.messages.Response;

import java.time.Instant;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class AllResources extends Response {

    private final Set<Resource> resources;

    public AllResources(Instant timestamp, Set<Resource> resources) {
        super(timestamp, null);
        this.resources = CollectionsUtil.unmodifiableCopy(resources);
    }

    public Collection<Resource> getResources() {
        return resources;
    }

    public static class Resource {

        private final String resource;
        private final Set<String> capabilities;
        private final boolean disabled;

        public Resource(String resource, Set<String> capabilities, boolean disabled) {
            this.resource = Objects.requireNonNull(resource, "resource must not be null");
            this.capabilities = CollectionsUtil.unmodifiableCopy(capabilities);
            this.disabled = disabled;
        }

        public Resource(String resource, Set<String> capabilities) {
            this(resource, capabilities, false);
        }

        public String getResource() {
            return resource;
        }

        public Set<String> getCapabilities() {
            return capabilities;
        }

        public boolean isDisabled() {
            return disabled;
        }
    }
}
