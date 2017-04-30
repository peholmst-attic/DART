package net.pkhapps.dart.modules.resources.integration.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.StatusCodes;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.util.MessageConverter;
import net.pkhapps.dart.modules.resources.RabbitMQProperties;
import net.pkhapps.dart.modules.resources.integration.xsd.Request;
import net.pkhapps.dart.modules.resources.integration.xsd.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.Clock;
import java.util.concurrent.ExecutorService;

/**
 * TODO Document me!
 */
class RequestMessageHandler extends AbstractMessageHandler<Request> {

    private final RequestBroker requestBroker;

    RequestMessageHandler(Channel channel, RabbitMQProperties rabbitMQProperties, Clock clock,
                          ExecutorService executorService, MessageConverter messageConverter,
                          RequestBroker requestBroker) {
        super(channel, Request.class, rabbitMQProperties, clock, executorService, messageConverter);
        this.requestBroker = requestBroker;
    }

    @Override
    protected void handleMessage(@NotNull Request message, @NotNull AMQP.BasicProperties properties)
            throws IOException {
        try {
            logger.debug("Received request [{}]", message);
            Response response = requestBroker.handleRequest(message, properties.getUserId());
            sendOk(response, properties);
        } catch (HandlerNotFoundException ex) {
            logger.warn("Found no handler for request [{}], ignoring", message);
        } catch (AccessDeniedException ex) {
            sendError(StatusCodes.FORBIDDEN, null, properties);
        } catch (HandlerException ex) {
            sendError(ex.getErrorCode(), ex.getMessage(), properties);
        }
    }
}
