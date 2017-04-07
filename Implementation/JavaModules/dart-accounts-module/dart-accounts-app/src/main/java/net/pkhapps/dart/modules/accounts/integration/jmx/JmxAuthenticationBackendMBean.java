package net.pkhapps.dart.modules.accounts.integration.jmx;

import net.pkhapps.dart.modules.accounts.domain.ResourcePermission;
import net.pkhapps.dart.modules.accounts.domain.ResourceType;

/**
 * MBean interface for exposing parts of {@link net.pkhapps.dart.modules.accounts.domain.AuthenticationBackend} over
 * JMX. This makes it easier to test things and perform simple administrative tasks through JConsole instead of having
 * to manipulate the database manually or build a dedicated user interface.
 */
public interface JmxAuthenticationBackendMBean {

    /**
     * @see net.pkhapps.dart.modules.accounts.domain.AuthenticationBackend#login(String, String)
     */
    boolean login(String name, String password);

    /**
     * @see net.pkhapps.dart.modules.accounts.domain.AuthenticationBackend#checkResource(String, String,
     * ResourceType, ResourcePermission)
     */
    boolean checkResource(String name, String resourceName, String resourceType,
                          String permission);

    /**
     * @see net.pkhapps.dart.modules.accounts.domain.AuthenticationBackend#checkTopic(String, String, ResourceType,
     * ResourcePermission, String)
     */
    boolean checkTopic(String name, String resourceName, String resourceType, String permission,
                       String routingKey);

    /**
     * @see net.pkhapps.dart.modules.accounts.domain.AuthenticationBackend#setPassword(String, String)
     */
    boolean setPassword(String name, String password);

    /**
     * @see net.pkhapps.dart.modules.accounts.domain.AuthenticationBackend#enableAccount(String)
     */
    boolean enableAccount(String name);

    /**
     * @see net.pkhapps.dart.modules.accounts.domain.AuthenticationBackend#disableAccount(String)
     */
    boolean disableAccount(String name);
}
