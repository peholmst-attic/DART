package net.pkhapps.dart.modules.resources.integration.rabbitmq;

import com.rabbitmq.client.Channel;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.util.MessageConverter;
import net.pkhapps.dart.modules.resources.RabbitMQProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Clock;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Channel manager bean that sets up the {@link CommandMessageHandler}.
 */
@ApplicationScoped
public class CommandMessageHandlerManager extends AbstractMessageHandlerManager<CommandMessageHandler> {

    private final Clock clock;
    private final MessageConverter messageConverter;
    private final CommandBroker commandBroker;

    @Inject
    CommandMessageHandlerManager(ScheduledExecutorService executorService, RabbitMQProperties rabbitMQProperties,
                                 Clock clock, MessageConverter messageConverter, CommandBroker commandBroker) {
        super(executorService, rabbitMQProperties, Routing.EXCHANGE, Routing.COMMANDS_QUEUE,
                Routing.COMMANDS_ROUTING_KEY);
        this.clock = clock;
        this.messageConverter = messageConverter;
        this.commandBroker = commandBroker;
    }

    @Override
    CommandMessageHandler createHandler(Channel channel) throws Exception {
        return new CommandMessageHandler(channel, (RabbitMQProperties) rabbitMQProperties, clock, executorService,
                messageConverter, commandBroker);
    }
}
