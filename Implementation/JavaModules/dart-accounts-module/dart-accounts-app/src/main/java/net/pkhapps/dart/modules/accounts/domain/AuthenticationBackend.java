package net.pkhapps.dart.modules.accounts.domain;

/**
 * TODO document me
 */
public interface AuthenticationBackend {

    boolean login(String name, String password);

    boolean checkResource(String name, String resourceName, ResourceType resourceType,
                          ResourcePermission permission);

    boolean checkTopic(String name, String resourceName, ResourceType resourceType, ResourcePermission permission,
                       String routingKey);

    void setPassword(String name, String password);

    void enableAccount(String name);

    void disableAccount(String name);

    // TODO Methods for adding and changing accounts and account types
}
