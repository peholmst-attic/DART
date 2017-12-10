package net.pkhapps.dart.statuspanel.server;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by peholmst on 31/03/2017.
 */
public class ServerApp {

    public static final String STATUS_COMMAND_QUEUE = "status/command";


    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("192.168.1.121");
        connectionFactory.setUsername("pettersimac");
        connectionFactory.setPassword("password");
        connectionFactory.setVirtualHost("dart");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.confirmSelect();
        channel.basicQos(1);

        channel.queueDeclare(STATUS_COMMAND_QUEUE, false, false, false, null);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "' from " + properties.getUserId());
            }
        };
        channel.basicConsume(STATUS_COMMAND_QUEUE, true, consumer);
    }
}
