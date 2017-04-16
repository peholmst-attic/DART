package net.pkhapps.dart.modules.accounts.integration.jmx;

import net.pkhapps.dart.modules.accounts.domain.AuthenticationService;
import net.pkhapps.dart.modules.accounts.domain.ResourcePermission;
import net.pkhapps.dart.modules.accounts.domain.ResourceType;

/**
 * MBean interface for exposing parts of {@link AuthenticationService} over
 * JMX. This makes it easier to test things and perform simple administrative tasks through JConsole instead of having
 * to manipulate the database manually or build a dedicated user interface.
 */
public interface JmxAuthenticationServiceMBean {

    /**
     * @see AuthenticationService#login(String, String)
     */
    boolean login(String name, String password);

    /**
     * @see AuthenticationService#checkResource(String, String,
     * ResourceType, ResourcePermission)
     */
    boolean checkResource(String name, String resourceName, String resourceType,
                          String permission);

    /**
     * @see AuthenticationService#checkTopic(String, String, ResourceType,
     * ResourcePermission, String)
     */
    boolean checkTopic(String name, String resourceName, String resourceType, String permission,
                       String routingKey);

    /**
     * @see AuthenticationService#setPassword(String, String)
     */
    boolean setPassword(String name, String password);

    /**
     * @see AuthenticationService#enableAccount(String)
     */
    boolean enableAccount(String name);

    /**
     * @see AuthenticationService#disableAccount(String)
     */
    boolean disableAccount(String name);
}
