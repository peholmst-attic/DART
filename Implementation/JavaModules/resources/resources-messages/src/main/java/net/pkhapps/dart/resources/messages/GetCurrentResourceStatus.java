package net.pkhapps.dart.resources.messages;

import net.pkhapps.dart.common.CollectionsUtil;
import net.pkhapps.dart.messaging.messages.Request;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class GetCurrentResourceStatus extends Request<CurrentResourceStatus> {

    private final Set<String> resources;

    public GetCurrentResourceStatus(@NotNull Set<String> resources) {
        super(null, null);
        this.resources = CollectionsUtil.unmodifiableCopy(resources);
    }

    @NotNull
    public Set<String> getResources() {
        return resources;
    }
}
