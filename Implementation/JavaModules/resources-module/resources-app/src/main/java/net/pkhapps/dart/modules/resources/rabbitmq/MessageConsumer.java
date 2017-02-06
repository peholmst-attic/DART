package net.pkhapps.dart.modules.resources.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import javax.xml.bind.JAXB;
import java.io.IOException;

/**
 * Created by petterprivate on 03/02/2017.
 */
public class MessageConsumer extends DefaultConsumer {

    public MessageConsumer(Channel channel) {
        super(channel);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        // TODO One Consumer for all messages or only one consumer per message type?
    }
}
