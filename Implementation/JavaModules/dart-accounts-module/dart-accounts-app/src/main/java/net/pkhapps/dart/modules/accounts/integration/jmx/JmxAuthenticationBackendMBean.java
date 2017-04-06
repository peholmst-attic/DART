package net.pkhapps.dart.modules.accounts.integration.jmx;

/**
 * TODO Document me
 */
public interface JmxAuthenticationBackendMBean {

    boolean login(String name, String password);

    boolean checkResource(String name, String resourceName, String resourceType,
                          String permission);

    boolean checkTopic(String name, String resourceName, String resourceType, String permission,
                       String routingKey);

    void setPassword(String name, String password);

    void enableAccount(String name);

    void disableAccount(String name);
}
