package net.pkhapps.dart.resources.messages;

import net.pkhapps.dart.messaging.messages.Request;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface GetCurrentResourceStatus extends Request<CurrentResourceStatus> {

    @NotNull
    Set<String> getResources();
}
