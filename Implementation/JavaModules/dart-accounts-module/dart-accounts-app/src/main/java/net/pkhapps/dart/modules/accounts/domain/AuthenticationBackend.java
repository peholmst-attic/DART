package net.pkhapps.dart.modules.accounts.domain;

import org.jetbrains.annotations.NotNull;

/**
 * TODO document me
 */
public interface AuthenticationBackend {

    boolean login(@NotNull String name, @NotNull String password);

    boolean checkResource(@NotNull String name, @NotNull String resourceName, @NotNull ResourceType resourceType,
                          @NotNull ResourcePermission permission);

    boolean checkTopic(@NotNull String name, @NotNull String resourceName, @NotNull ResourceType resourceType,
                       @NotNull ResourcePermission permission,
                       @NotNull String routingKey);

    boolean setPassword(@NotNull String name, @NotNull String password);

    boolean enableAccount(@NotNull String name);

    boolean disableAccount(@NotNull String name);
    
    // TODO Methods for adding and changing accounts and account types
}
