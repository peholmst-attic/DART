package net.pkhapps.dart.poc;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeoutException;

public class MessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

    private final ConnectionFactory connectionFactory;
    private Connection connection;
    private Channel channel;
    private Consumer consumer;

    private static Set<String> processedMessages = new ConcurrentSkipListSet<>();

    public MessageConsumer(String consumerId) throws IOException, TimeoutException {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(Constants.HOST);
        connectionFactory.setAutomaticRecoveryEnabled(true);

        connection = connectionFactory.newConnection();
        ((Recoverable) connection).addRecoveryListener(recoverable -> LOGGER.info("Recovering {}", recoverable));
        channel = connection.createChannel();
        ((Recoverable) connection).addRecoveryListener(recoverable -> LOGGER.info("Recovering {}", recoverable));
        channel.confirmSelect();
        channel.basicQos(1); // Don't send a message to this worker until the previous one has been processed.
        channel.queueDeclare(Constants.QUEUE_NAME, false, false, false, null);

        consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (!processedMessages.add(properties.getMessageId())) {
                    LOGGER.info("Consumer {} received message {} that has already processed, ignoring", consumerId, properties.getMessageId());
                    // This is not a good approach since we will eventually run out of memory as more and more message IDs are added to the
                    // set. A better approach is to implement the message handlers in such a way that they don't perform actions that have
                    // already been performed.
                } else {
                    String message = new String(body, StandardCharsets.UTF_8);
                    LOGGER.info("Consumer {} received message: messageId='{}', timestamp='{}', redeliver={}, consumerTag='{}', correlationId='{}': {}",
                            consumerId, properties.getMessageId(), properties.getTimestamp(), envelope.isRedeliver(), consumerTag, properties.getCorrelationId(), message);
                    // Manually confirm the message once it has been processed. If you skip this call, the message will be redelivered the next time
                    // a worker connects to the queue. Remember that these acknowledgments have nothing to do with whether the message consumer
                    // managed to e.g. successfully commit a database transaction or not. Even if a transaction fails, the message was still
                    // successfully delivered to the consumer. For application errors, another mechanism must be used.
                }
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        channel.basicConsume(Constants.QUEUE_NAME, false, consumer);
    }
}
