package net.pkhapps.dart.resources.messages;

import net.pkhapps.dart.common.Coordinates;
import net.pkhapps.dart.messaging.messages.FireAndForgetCommand;
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
