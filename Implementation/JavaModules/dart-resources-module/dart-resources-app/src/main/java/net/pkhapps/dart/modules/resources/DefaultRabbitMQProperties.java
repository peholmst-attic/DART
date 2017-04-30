package net.pkhapps.dart.modules.resources;

import com.netflix.config.DynamicPropertyFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.util.function.Supplier;

/**
 * Default implementation of {@link RabbitMQProperties}. This is an {@code @Alternative} bean so remember to activate
 * in {@code beans.xml}.
 */
@ApplicationScoped
@Alternative
public class DefaultRabbitMQProperties extends net.pkhapps.dart.modules.base.rabbitmq.DefaultRabbitMQProperties
        implements RabbitMQProperties {

    private static final String RABBITMQ_MESSAGE_EXPIRATION = "rabbitmq.message.expiration";
    private static final String RABBITMQ_MESSAGE_APP_ID = "rabbitmq.message.appId";

    @Override
    public Supplier<String> getMessageExpiration() {
        return DynamicPropertyFactory.getInstance().getStringProperty(RABBITMQ_MESSAGE_EXPIRATION, "30000")::getValue;
    }

    @Override
    public Supplier<String> getMessageAppId() {
        return DynamicPropertyFactory.getInstance()
                .getStringProperty(RABBITMQ_MESSAGE_APP_ID, getClass().getPackage().getName())::getValue;
    }
}
