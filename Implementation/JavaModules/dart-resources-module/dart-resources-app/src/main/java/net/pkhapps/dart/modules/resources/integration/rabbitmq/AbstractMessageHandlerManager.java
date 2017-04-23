package net.pkhapps.dart.modules.resources.integration.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import net.pkhapps.dart.modules.base.rabbitmq.RabbitMQChannelManager;

/**
 * TODO Document me
 */
abstract class AbstractMessageHandlerManager<H extends AbstractMessageHandler> extends RabbitMQChannelManager {

    private final String exchange;
    private final String queueName;
    private final String routingKey;

    AbstractMessageHandlerManager(String exchange, String queueName, String routingKey) {
        this.exchange = exchange;
        this.queueName = queueName;
        this.routingKey = routingKey;
    }

    @Override
    protected void setUp(Channel channel) throws Exception {
        channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT);
        logger.info("Declared RabbitMQ exchange [{}]", exchange);

        declareAndBind(channel, queueName, routingKey);

        H messageHandler = createHandler(channel);
        channel.basicConsume(queueName, false, messageHandler);
    }

    abstract H createHandler(Channel channel) throws Exception;

    private void declareAndBind(Channel channel, String queueName, String routingKey) throws Exception {
        channel.queueDeclare(queueName, false, false, false, null);
        logger.info("Declared RabbitMQ queue [{}]", queueName);

        channel.queueBind(queueName, Routing.EXCHANGE, routingKey);
        logger.info("Bound queue [{}] to exchange [{}] using routing key [{}]", queueName, Routing.EXCHANGE,
                routingKey);
    }
}
