package net.pkhapps.dart.resources.messages;

import net.pkhapps.dart.messaging.messages.FireAndForgetCommand;
import net.pkhapps.dart.messaging.types.Coordinates;
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
