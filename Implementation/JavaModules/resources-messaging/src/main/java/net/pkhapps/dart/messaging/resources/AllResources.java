package net.pkhapps.dart.messaging.resources;

import net.pkhapps.dart.messaging.common.messages.Response;
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
