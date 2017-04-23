package net.pkhapps.dart.modules.resources.integration.rabbitmq;

import com.rabbitmq.client.Channel;
import net.pkhapps.dart.modules.resources.RabbitMQProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import java.time.Clock;

/**
 * Channel manager bean that sets up the {@link CommandMessageHandler}.
 */
@ApplicationScoped
public class CommandMessageHandlerManager extends AbstractMessageHandlerManager<CommandMessageHandler> {

    @Inject
    Clock clock;

    @Inject
    CommandBroker commandBroker;

    @Inject
    RabbitMQProperties rabbitMQProperties;

    @Inject
    JAXBContext jaxbContext;

    CommandMessageHandlerManager() {
        super(Routing.EXCHANGE, Routing.COMMANDS_QUEUE, Routing.COMMANDS_ROUTING_KEY);
    }

    @Override
    CommandMessageHandler createHandler(Channel channel) throws Exception {
        return new CommandMessageHandler(channel, rabbitMQProperties, clock, jaxbContext, commandBroker);
    }
}
