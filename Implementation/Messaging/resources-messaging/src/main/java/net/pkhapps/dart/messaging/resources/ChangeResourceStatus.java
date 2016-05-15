package net.pkhapps.dart.messaging.resources;

import net.pkhapps.dart.messaging.common.messages.FireAndForgetCommand;
import net.pkhapps.dart.messaging.common.types.Coordinates;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface ChangeResourceStatus extends FireAndForgetCommand {

    @NotNull
    String getResource();

    @NotNull
    Optional<String> getState();

    @NotNull
    Optional<Coordinates> getLocation();
}
