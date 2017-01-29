package net.pkhapps.dart.tickets;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import net.pkhapps.dart.platform.amqp.MessageConsumer;
import net.pkhapps.dart.tickets.messages.Routing;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * Created by peholmst on 21/01/2017.
 */
public class TicketsApp {

    // Let's first build this without dependency injection and then see what parts we can turn into platform code.

    private ConnectionFactory connectionFactory;
    private ExecutorService threadPool;
    private Connection connection;
    private Channel channel;

    public void start() throws IOException, TimeoutException {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        threadPool = Executors.newCachedThreadPool();

        connection = connectionFactory.newConnection(threadPool);
        channel = connection.createChannel();

        // This queue will be used for receiving commands and queries to process
        channel.queueDeclare(Routing.MESSAGE_QUEUE, true, false, false, null);
        // This exchange will be used for broadcasting information to interested subscribers
        channel.exchangeDeclare(Routing.BROADCAST_EXCHANGE, "topic");

        // Start listening for commands and queries to process
        MessageConsumer messageConsumer = new MessageConsumer(channel, threadPool);
        channel.basicConsume(Routing.MESSAGE_QUEUE, false, messageConsumer);
    }

    public void stop() throws IOException, TimeoutException {
        try {
            channel.close();
        } finally {
            connection.close();
            threadPool.shutdown();
        }
    }

    public static void main(String[] args) throws Exception {
        TicketsApp app = new TicketsApp();
        app.start();
        while(true) {

        }
    }
}
