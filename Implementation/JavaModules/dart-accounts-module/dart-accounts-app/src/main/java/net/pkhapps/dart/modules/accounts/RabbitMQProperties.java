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

    /**
     * The authentication exchange used by <b>rabbitmq-auth-backend-amqp</b>.
     */
    public Supplier<String> getAuthenticationExchange() {
        return DynamicPropertyFactory.getInstance()
                .getStringProperty(RABBITMQ_AUTHENTICATION_EXCHANGE, "authentication")::getValue;
    }
}
