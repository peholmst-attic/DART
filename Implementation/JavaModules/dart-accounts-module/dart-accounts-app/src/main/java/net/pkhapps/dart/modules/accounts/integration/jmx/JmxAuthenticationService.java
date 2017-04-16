package net.pkhapps.dart.modules.accounts.integration.jmx;

import net.pkhapps.dart.modules.accounts.domain.AuthenticationService;
import net.pkhapps.dart.modules.accounts.domain.ResourcePermission;
import net.pkhapps.dart.modules.accounts.domain.ResourceType;

/**
 * Default implementation of {@link JmxAuthenticationServiceMBean} that delegates all the operations to an
 * {@link AuthenticationService}.
 */
class JmxAuthenticationService implements JmxAuthenticationServiceMBean {

    private final AuthenticationService delegate;

    public JmxAuthenticationService(AuthenticationService delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean login(String name, String password) {
        try {
            return delegate.login(name, password);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public boolean checkResource(String name, String resourceName, String resourceType,
                                 String permission) {
        try {
            return delegate.checkResource(name, resourceName, ResourceType.valueOf(resourceType),
                    ResourcePermission.valueOf(permission));
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public boolean checkTopic(String name, String resourceName, String resourceType,
                              String permission, String routingKey) {
        try {
            return delegate.checkTopic(name, resourceName, ResourceType.valueOf(resourceType),
                    ResourcePermission.valueOf(permission), routingKey);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public boolean setPassword(String name, String password) {
        try {
            return delegate.setPassword(name, password);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public boolean enableAccount(String name) {
        try {
            return delegate.enableAccount(name);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public boolean disableAccount(String name) {
        try {
            return delegate.disableAccount(name);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
