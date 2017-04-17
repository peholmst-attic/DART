package net.pkhapps.dart.modules.accounts.domain;

import org.jetbrains.annotations.NotNull;

/**
 * Main service interface for authentication (and some authorization actually - maybe the name should be changed in
 * the future). Clients normally access this service either through JMX or RabbitMQ.
 */
public interface AuthenticationService {

    /**
     * Tries to login a user.
     *
     * @param name     the user/account name.
     * @param password the unencrypted password.
     * @return true if login was successful, false if not.
     */
    boolean login(@NotNull String name, @NotNull String password);

    /**
     * Checks if a user has permission to a resource.
     *
     * @param name         the user/account name.
     * @param resourceName the name of the resource.
     * @param resourceType the type of the resource.
     * @param permission   the requested permission.
     * @return true if the user has permission, false if not.
     */
    boolean checkResource(@NotNull String name, @NotNull String resourceName, @NotNull ResourceType resourceType,
                          @NotNull ResourcePermission permission);

    /**
     * Checks if a user has permission to a topic.
     *
     * @param name         the user/account name.
     * @param resourceName the name of the resource.
     * @param resourceType the type of the resource.
     * @param permission   the requested permission.
     * @param routingKey   the routing key.
     * @return true if the user has permission, false if not.
     */
    boolean checkTopic(@NotNull String name, @NotNull String resourceName, @NotNull ResourceType resourceType,
                       @NotNull ResourcePermission permission,
                       @NotNull String routingKey);

    /**
     * Sets the password for a user account.
     *
     * @param name     the user/account name.
     * @param password the new unencrypted password to set.
     * @return true if the password was successfully set, false if not (e.g. if the account did not exist).
     */
    boolean setPassword(@NotNull String name, @NotNull String password);

    /**
     * Enables a user account.
     *
     * @param name the user/account name.
     * @return true if the account was enabled, false if not (e.g. if the account did not exist or was already enabled).
     */
    boolean enableAccount(@NotNull String name);

    /**
     * Disables a user account.
     *
     * @param name the user/account name.
     * @return true if the account was disabled, false if not (e.g. if the account did not exist or was already
     * disabled).
     */
    boolean disableAccount(@NotNull String name);

    // TODO Methods for adding and changing accounts and account types
}
