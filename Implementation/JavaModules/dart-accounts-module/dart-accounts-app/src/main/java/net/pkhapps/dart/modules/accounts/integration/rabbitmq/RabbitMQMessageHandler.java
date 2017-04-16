package net.pkhapps.dart.modules.accounts.integration.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import net.pkhapps.dart.modules.accounts.RabbitMQProperties;
import net.pkhapps.dart.modules.accounts.domain.AuthenticationService;
import net.pkhapps.dart.modules.accounts.domain.ResourcePermission;
import net.pkhapps.dart.modules.accounts.domain.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * A RabbitMQ message consumer that receives authentication requests from the <b>rabbitmq-auth-backend-amqp</b>
 * authentication backend, delegates the actual authentication to an {@link AuthenticationService} and sends a reply
 * message.
 *
 * @see <a href="https://github.com/rabbitmq/rabbitmq-auth-backend-amqp">rabbitmq-auth-backend-amqp</a>
 */
class RabbitMQMessageHandler extends DefaultConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQMessageHandler.class);

    private static final String HEADER_USERNAME = "username";
    private static final String HEADER_PASSWORD = "password";
    private static final String HEADER_VHOST = "vhost";
    private static final String HEADER_NAME = "name";
    private static final String HEADER_RESOURCE = "resource";
    private static final String HEADER_PERMISSION = "permission";

    private final AuthenticationService authenticationService;
    private final RabbitMQProperties rabbitMQProperties;

    RabbitMQMessageHandler(Channel channel, AuthenticationService authenticationService,
                           RabbitMQProperties rabbitMQProperties) {
        super(channel);
        this.authenticationService = authenticationService;
        this.rabbitMQProperties = rabbitMQProperties;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
            throws IOException {
        final String action = properties.getHeaders().getOrDefault("action", "").toString();
        LOGGER.debug("Received action \"{}\"", action);
        switch (action) {
            case "login":
                login(properties);
                break;
            case "check_vhost":
                checkVirtualHost(properties);
                break;
            case "check_resource":
                checkResource(properties);
                break;
            case "check_topic":
                checkTopic(properties);
                break;
            default:
                LOGGER.warn("Action \"{}\" is unknown, ignoring", action);
        }
        LOGGER.debug("Acknowledging message [{}]", envelope.getDeliveryTag());
        getChannel().basicAck(envelope.getDeliveryTag(), false);
    }

    private void login(AMQP.BasicProperties properties) throws IOException {
        final String username = getHeader(HEADER_USERNAME, properties);
        final String password = getHeader(HEADER_PASSWORD, properties);
        if (username != null && password != null && authenticationService.login(username, password)) {
            reply(properties, "".getBytes(StandardCharsets.UTF_8));
        } else {
            reply(properties, "refused".getBytes(StandardCharsets.UTF_8));
        }
    }

    private void checkVirtualHost(AMQP.BasicProperties properties) throws IOException {
        reply(properties, toReplyBody(isValidVirtualHost(getHeader(HEADER_VHOST, properties))));
    }

    private void checkResource(AMQP.BasicProperties properties) throws IOException {
        final String username = getHeader(HEADER_USERNAME, properties);
        final String vhost = getHeader(HEADER_VHOST, properties);
        final String resourceName = getHeader(HEADER_NAME, properties);
        final ResourceType resourceType = getHeader(HEADER_RESOURCE, properties, ResourceType.class);
        final ResourcePermission permission = getHeader(HEADER_PERMISSION, properties, ResourcePermission.class);
        final boolean result =
                username != null && isValidVirtualHost(vhost) && resourceName != null && resourceType != null &&
                permission != null &&
                authenticationService.checkResource(username, resourceName, resourceType, permission);
        reply(properties, toReplyBody(result));
    }

    private void checkTopic(AMQP.BasicProperties properties) throws IOException {
        final String username = getHeader(HEADER_USERNAME, properties);
        final String vhost = getHeader(HEADER_VHOST, properties);
        final String resourceName = getHeader(HEADER_NAME, properties);
        final ResourceType resourceType = getHeader(HEADER_RESOURCE, properties, ResourceType.class);
        final ResourcePermission permission = getHeader(HEADER_PERMISSION, properties, ResourcePermission.class);
        final String routingKey = getHeader("routing_key", properties);
        final boolean result =
                username != null && isValidVirtualHost(vhost) && resourceName != null && resourceType != null &&
                permission != null && routingKey != null &&
                authenticationService.checkTopic(username, resourceName, resourceType, permission, routingKey);
        reply(properties, toReplyBody(result));
    }

    private boolean isValidVirtualHost(String vhost) {
        return vhost != null && vhost.equals(rabbitMQProperties.getVirtualHost().get());
    }

    private static byte[] toReplyBody(boolean b) {
        return (b ? "allow" : "deny").getBytes(StandardCharsets.UTF_8);
    }

    private static String getHeader(String key, AMQP.BasicProperties properties) {
        Object o = properties.getHeaders().get(key);
        return o == null ? null : o.toString();
    }

    private static <E extends Enum<E>> E getHeader(String key, AMQP.BasicProperties properties, Class<E> enumClass) {
        String header = getHeader(key, properties);
        if (header != null) {
            try {
                return Enum.valueOf(enumClass, header.toUpperCase());
            } catch (IllegalArgumentException ex) {
                return null;
            }
        } else {
            return null;
        }
    }

    private void reply(AMQP.BasicProperties properties, byte[] body) throws IOException {
        String correlationId = properties.getCorrelationId();
        String replyTo = properties.getReplyTo();
        LOGGER.debug("Sending reply to [{}] using correlationId [{}]", replyTo, correlationId);
        if (correlationId != null && replyTo != null) {
            AMQP.BasicProperties replyProperties
                    = new AMQP.BasicProperties.Builder().correlationId(correlationId).build();
            getChannel().basicPublish("", replyTo, replyProperties, body);
        } else {
            LOGGER.warn("No reply sent - replyTo [{}] and/or correlationId [{}] was null", replyTo, correlationId);
        }
    }
}
