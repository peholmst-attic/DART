package net.pkhapps.dart.modules.resources.integration.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.StatusCodes;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.util.MessageConverter;
import net.pkhapps.dart.modules.resources.RabbitMQProperties;
import net.pkhapps.dart.modules.resources.integration.xsd.Command;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.Clock;
import java.util.concurrent.ExecutorService;

/**
 * TODO Document me!
 */
class CommandMessageHandler extends AbstractMessageHandler<Command> {

    private final CommandBroker commandBroker;

    CommandMessageHandler(Channel channel, RabbitMQProperties rabbitMQProperties, Clock clock,
                          ExecutorService executorService, MessageConverter messageConverter,
                          CommandBroker commandBroker) {
        super(channel, Command.class, rabbitMQProperties, clock, executorService, messageConverter);
        this.commandBroker = commandBroker;
    }

    @Override
    protected void handleMessage(@NotNull Command message, @NotNull AMQP.@NotNull BasicProperties properties)
            throws IOException {
        try {
            logger.debug("Received command [{}]", message);
            commandBroker.handleCommand(message, properties.getUserId());
            sendOk(null, properties);
        } catch (HandlerNotFoundException ex) {
            logger.warn("Found no handler for command [{}], ignoring", message);
        } catch (AccessDeniedException ex) {
            sendError(StatusCodes.FORBIDDEN, null, properties);
        } catch (HandlerException ex) {
            sendError(ex.getErrorCode(), ex.getMessage(), properties);
        }
    }
}
