package net.pkhapps.dart.modules.accounts.messaging;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import net.pkhapps.dart.modules.accounts.domain.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Created by peholmst on 01/04/2017.
 */
public class MessageConsumer extends DefaultConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);
    private final AccountService accountService;

    public MessageConsumer(Channel channel, AccountService accountService) {
        super(channel);
        this.accountService = accountService;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
            throws IOException {
        final Map<String, Object> headers = properties.getHeaders();
        final String action = headers.getOrDefault("action", "").toString();

        switch (action) {
            case "login":
                break;
            case "check_vhost":
                break;
            case "check_resource":
                break;
            case "check_topic":
                break;
            default:
                LOGGER.warn("Received unknown action \"{}\", ignoring", action);
        }
    }
}
