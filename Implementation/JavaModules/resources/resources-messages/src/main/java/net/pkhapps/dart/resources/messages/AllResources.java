package net.pkhapps.dart.resources.messages;

import net.pkhapps.dart.messaging.messages.Response;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

public interface AllResources extends Response {

    @NotNull
    Collection<Resource> getResources();

    interface Resource {
        @NotNull
        String getResource();

        @NotNull
        Set<String> getCapabilities();

        boolean isDisabled();
    }
}
