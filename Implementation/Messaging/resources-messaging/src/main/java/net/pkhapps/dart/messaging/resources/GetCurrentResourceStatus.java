package net.pkhapps.dart.messaging.resources;

import net.pkhapps.dart.messaging.common.messages.Request;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface GetCurrentResourceStatus extends Request<CurrentResourceStatus> {

    @NotNull
    Set<String> getResources();
}
