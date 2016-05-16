package net.pkhapps.dart.messaging.resources;

import net.pkhapps.dart.messaging.common.messages.Response;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

public interface AllResourceCapabilities extends Response {

    @NotNull
    Collection<ResourceCapability> getCapabilities();

    interface ResourceCapability {
        @NotNull
        String getCapability();

        @NotNull
        Map<Locale, String> getDescription();
    }
}
