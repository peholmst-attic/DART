package net.pkhapps.dart.messaging.common.amqp.handlers;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import net.pkhapps.dart.messaging.common.handlers.MessageHandler;
import net.pkhapps.dart.messaging.common.messages.Message;

import java.io.IOException;

public class MessageHandlerConsumer<M extends Message> extends DefaultConsumer {


    public MessageHandlerConsumer(MessageHandler<M> messageHandler, Channel channel, String queueName)
            throws IOException {
        super(channel);
        channel.basicConsume(queueName, this);
    }

}
