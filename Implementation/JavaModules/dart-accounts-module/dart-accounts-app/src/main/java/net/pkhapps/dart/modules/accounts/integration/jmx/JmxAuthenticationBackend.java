package net.pkhapps.dart.modules.accounts.integration.jmx;

import net.pkhapps.dart.modules.accounts.domain.AuthenticationBackend;
import net.pkhapps.dart.modules.accounts.domain.ResourcePermission;
import net.pkhapps.dart.modules.accounts.domain.ResourceType;

/**
 * Default implementation of {@link JmxAuthenticationBackendMBean} that delegates all the operations to an
 * {@link AuthenticationBackend}.
 */
class JmxAuthenticationBackend implements JmxAuthenticationBackendMBean {

    private final AuthenticationBackend delegate;

    public JmxAuthenticationBackend(AuthenticationBackend delegate) {
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
    public void setPassword(String name, String password) {
        try {
            delegate.setPassword(name, password);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void enableAccount(String name) {
        try {
            delegate.enableAccount(name);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void disableAccount(String name) {
        try {
            delegate.disableAccount(name);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
