package net.pkhapps.dart.modules.accounts.integration.jmx;

import net.pkhapps.dart.modules.accounts.domain.AuthenticationBackend;
import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * Bean that registers and unregisters the {@link JmxAuthenticationBackend} with the {@code MBeanServer}.
 */
@ApplicationScoped
class MBeanRegistration {

    private static final Logger LOGGER = LoggerFactory.getLogger(MBeanRegistration.class);

    @Inject
    AuthenticationBackend authenticationBackend;

    private ObjectName authenticationBackendName;

    void onContainerInitialized(@Observes ContainerInitialized containerInitialized) {
        try {
            LOGGER.info("Registering authentication backend MBean");
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            authenticationBackendName = new ObjectName("net.pkhapps.dart.modules.accounts:type=AuthenticationBackend");
            mbs.registerMBean(new JmxAuthenticationBackend(authenticationBackend), authenticationBackendName);
        } catch (Exception ex) {
            LOGGER.error("Could not register authentication backend MBean", ex);
            authenticationBackendName = null;
        }
    }

    @PreDestroy
    void destroy() {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        try {
            if (authenticationBackendName != null) {
                LOGGER.info("Unregistering authentication backend MBean");
                mbs.unregisterMBean(authenticationBackendName);
            }
        } catch (Exception ex) {
            LOGGER.error("Could not unregister authentication backend MBean", ex);
        }
    }
}
