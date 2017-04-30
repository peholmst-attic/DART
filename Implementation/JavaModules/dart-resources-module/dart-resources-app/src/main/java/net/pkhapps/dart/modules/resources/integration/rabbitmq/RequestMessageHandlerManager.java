package net.pkhapps.dart.modules.resources.integration.rabbitmq;

import com.rabbitmq.client.Channel;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.util.MessageConverter;
import net.pkhapps.dart.modules.resources.RabbitMQProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Clock;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Channel manager bean that sets up the {@link RequestMessageHandler}.
 */
@ApplicationScoped
class RequestMessageHandlerManager extends AbstractMessageHandlerManager<RequestMessageHandler> {

    private final Clock clock;
    private final MessageConverter messageConverter;
    private final RequestBroker requestBroker;

    @Inject
    RequestMessageHandlerManager(ScheduledExecutorService executorService, RabbitMQProperties rabbitMQProperties,
                                 Clock clock, MessageConverter messageConverter, RequestBroker requestBroker) {
        super(executorService, rabbitMQProperties, Routing.EXCHANGE, Routing.REQUESTS_QUEUE,
                Routing.REQUESTS_ROUTING_KEY);
        this.clock = clock;
        this.messageConverter = messageConverter;
        this.requestBroker = requestBroker;
    }

    @Override
    RequestMessageHandler createHandler(Channel channel) throws Exception {
        return new RequestMessageHandler(channel, (RabbitMQProperties) rabbitMQProperties, clock, executorService,
                messageConverter, requestBroker);
    }
}
