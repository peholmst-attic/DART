package net.pkhapps.dart.modules.accounts;

import com.netflix.config.DynamicPropertyFactory;
import net.pkhapps.dart.modules.base.rabbitmq.DefaultRabbitMQProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.util.function.Supplier;

/**
 * Bean for getting easy access to the RabbitMQ configuration properties.
 */
@ApplicationScoped
@Alternative
public class RabbitMQProperties extends DefaultRabbitMQProperties {

    private static final String RABBITMQ_AUTHENTICATION_EXCHANGE = "rabbitmq.authentication.exchange";

    private static final String RABBITMQ_AUTHENTICATION_INTEGRATION_TEST_ENABLED =
            "rabbitmq.authentication.exchange.integration-test.enabled";

    private static final String RABBITMQ_AUTHENTICATION_INTEGRATION_TEST_USER =
            "rabbitmq.authentication.exchange.integration-test.user";

    private static final String RABBITMQ_AUTHENTICATION_INTEGRATION_TEST_PASSWORD =
            "rabbitmq.authentication.exchange.integration-test.password";

    /**
     * The authentication exchange used by <b>rabbitmq-auth-backend-amqp</b>.
     */
    public Supplier<String> getAuthenticationExchange() {
        return DynamicPropertyFactory.getInstance()
                .getStringProperty(RABBITMQ_AUTHENTICATION_EXCHANGE, "authentication")::getValue;
    }

    /**
     * The username that integration tests are expected to use. Integration test users get full access to everything.
     */
    public Supplier<String> getAuthenticationIntegrationTestUser() {
        return DynamicPropertyFactory.getInstance()
                .getStringProperty(RABBITMQ_AUTHENTICATION_INTEGRATION_TEST_USER, "")::getValue;
    }

    /**
     * Whether the integration test user account is enabled or not.
     */
    public Supplier<Boolean> getAuthenticationIntegrationTestEnabled() {
        return DynamicPropertyFactory.getInstance()
                .getBooleanProperty(RABBITMQ_AUTHENTICATION_INTEGRATION_TEST_ENABLED, false)::getValue;
    }

    /**
     * The password that integration tests are expected to use. Integration test users get full access to everything.
     */
    public Supplier<String> getAuthenticationIntegrationTestPassword() {
        return DynamicPropertyFactory.getInstance()
                .getStringProperty(RABBITMQ_AUTHENTICATION_INTEGRATION_TEST_PASSWORD, "")::getValue;
    }
}
