package net.pkhapps.dart.modules.resources.integration.rabbitmq;

import com.rabbitmq.client.Channel;
import net.pkhapps.dart.modules.resources.RabbitMQProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import java.time.Clock;

/**
 * Channel manager bean that sets up the {@link RequestMessageHandler}.
 */
@ApplicationScoped
class RequestMessageHandlerManager extends AbstractMessageHandlerManager<RequestMessageHandler> {

    @Inject
    Clock clock;

    @Inject
    RequestBroker requestBroker;

    @Inject
    RabbitMQProperties rabbitMQProperties;

    @Inject
    JAXBContext jaxbContext;

    RequestMessageHandlerManager() {
        super(Queues.EXCHANGE, Queues.REQUESTS_QUEUE, Queues.REQUESTS_ROUTING_KEY);
    }

    @Override
    RequestMessageHandler createHandler(Channel channel) throws Exception {
        return new RequestMessageHandler(channel, rabbitMQProperties, clock, jaxbContext, requestBroker);
    }
}
