package net.pkhapps.dart.resources.messages;

import net.pkhapps.dart.common.CollectionsUtil;
import net.pkhapps.dart.messaging.messages.Request;

import java.util.Set;

public class GetCurrentResourceStatus extends Request<CurrentResourceStatus> {

    private final Set<String> resources;

    public GetCurrentResourceStatus(Set<String> resources) {
        super(null, null);
        this.resources = CollectionsUtil.unmodifiableCopy(resources);
    }

    public Set<String> getResources() {
        return resources;
    }
}
