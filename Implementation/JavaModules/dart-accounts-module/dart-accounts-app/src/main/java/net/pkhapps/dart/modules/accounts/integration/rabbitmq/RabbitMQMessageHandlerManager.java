package net.pkhapps.dart.modules.accounts.integration.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import net.pkhapps.dart.modules.accounts.RabbitMQProperties;
import net.pkhapps.dart.modules.accounts.domain.AuthenticationService;
import net.pkhapps.dart.modules.base.rabbitmq.RabbitMQChannelManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Channel manager bean that sets up the {@link RabbitMQMessageHandler}.
 */
@ApplicationScoped
class RabbitMQMessageHandlerManager extends RabbitMQChannelManager {

    @Inject
    AuthenticationService authenticationService;

    @Inject
    RabbitMQProperties rabbitMQProperties;

    @Override
    protected void setUp(Channel channel) throws Exception {
        final String queueName = channel.queueDeclare().getQueue();
        logger.info("Declared RabbitMQ queue [{}] for authentication requests", queueName);

        final String exchange = rabbitMQProperties.getAuthenticationExchange().get();
        channel.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT);
        logger.info("Declared RabbitMQ exchange [{}]", exchange);

        channel.queueBind(queueName, exchange, "");
        logger.info("Bound authentication queue [{}] to exchange [{}]", queueName, exchange);

        RabbitMQMessageHandler messageHandler =
                new RabbitMQMessageHandler(channel, authenticationService, rabbitMQProperties);
        channel.basicConsume(queueName, false, messageHandler);
    }
}
