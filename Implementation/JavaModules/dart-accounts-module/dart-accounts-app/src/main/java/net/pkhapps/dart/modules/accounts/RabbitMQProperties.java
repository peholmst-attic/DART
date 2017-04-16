package net.pkhapps.dart.modules.accounts;

import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

/**
 * Bean for getting easy access to the RabbitMQ configuration properties.
 */
@ApplicationScoped
@Alternative
public class RabbitMQProperties extends net.pkhapps.dart.modules.base.rabbitmq.RabbitMQProperties {

    private static final String RABBITMQ_AUTHENTICATION_EXCHANGE = "rabbitmq.authentication.exchange";

    /**
     * The authentication exchange used by <b>rabbitmq-auth-backend-amqp</b>.
     */
    public DynamicStringProperty getAuthenticationExchange() {
        return DynamicPropertyFactory.getInstance()
                .getStringProperty(RABBITMQ_AUTHENTICATION_EXCHANGE, "authentication");
    }
}
