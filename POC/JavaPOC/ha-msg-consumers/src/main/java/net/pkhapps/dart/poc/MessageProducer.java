package net.pkhapps.dart.poc;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

public class MessageProducer implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProducer.class);

    private final UUID producerUUID = UUID.randomUUID();
    private final ConnectionFactory connectionFactory;
    private Connection connection;
    private Channel channel;

    private final AtomicLong nextMessageId = new AtomicLong();

    public MessageProducer() throws IOException, TimeoutException {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(Constants.HOST);
        connectionFactory.setAutomaticRecoveryEnabled(true);

        connection = connectionFactory.newConnection();
        ((Recoverable) connection).addRecoveryListener(recoverable -> LOGGER.info("Recovering {}", recoverable));
        channel = connection.createChannel();
        ((Recoverable) connection).addRecoveryListener(recoverable -> LOGGER.info("Recovering {}", recoverable));
        channel.confirmSelect();
        channel.queueDeclare(Constants.QUEUE_NAME, false, false, false, null);
    }

    @Override
    public void close() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }

    public void sendMessage() throws IOException, TimeoutException, InterruptedException {
        sendMessage(1);
    }

    public void sendMessage(int sendTimes) throws IOException, TimeoutException, InterruptedException {
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .appId("dart-ha-poc")
                .type("poc-message")
                .timestamp(new Date())
                .messageId(String.format("%s:%d", producerUUID.toString(), nextMessageId.getAndIncrement()))
                .expiration(Constants.MESSAGE_EXPIRATION)
                .build();
        LOGGER.info("Publishing message");
        for (int i = 0; i < sendTimes; ++i) {
            channel.basicPublish("", Constants.QUEUE_NAME, properties, "Hello World".getBytes(StandardCharsets.UTF_8));
        }
        // Remember that this does not mean the message has been processed by a consumer. It only means the message
        // has been received by the message broker.
        if (!channel.waitForConfirms(Constants.CONFIRMATION_TIMEOUT_MS)) {
            LOGGER.warn("Message not confirmed by the broker");
        } else {
            LOGGER.info("Message confirmed by the broker");
        }
    }
}
