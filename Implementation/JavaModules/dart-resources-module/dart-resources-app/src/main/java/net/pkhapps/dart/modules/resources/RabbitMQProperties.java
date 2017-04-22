package net.pkhapps.dart.modules.resources;

import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

/**
 * TODO Document me!
 */
@ApplicationScoped
@Alternative
public class RabbitMQProperties extends net.pkhapps.dart.modules.base.rabbitmq.RabbitMQProperties {

    private static final String RABBITMQ_MESSAGE_EXPIRATION = "rabbitmq.message.expiration";
    private static final String RABBITMQ_MESSAGE_APP_ID = "rabbitmq.message.appId";

    public DynamicStringProperty getMessageExpiration() {
        return DynamicPropertyFactory.getInstance().getStringProperty(RABBITMQ_MESSAGE_EXPIRATION, "30000");
    }

    public DynamicStringProperty getMessageAppId() {
        return DynamicPropertyFactory.getInstance()
                .getStringProperty(RABBITMQ_MESSAGE_APP_ID, getClass().getPackage().getName());
    }
}
