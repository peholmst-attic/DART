package net.pkhapps.dart.modules.base.rabbitmq.messaging.server.request;

import com.rabbitmq.client.Channel;
import net.pkhapps.dart.modules.base.rabbitmq.RabbitMQChannelManager;
import net.pkhapps.dart.modules.base.rabbitmq.RabbitMQProperties;

import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by peholmst on 09/05/2017.
 */
public class RequestConsumerManager extends RabbitMQChannelManager {

    public RequestConsumerManager(ScheduledExecutorService executorService,
                                  RabbitMQProperties rabbitMQProperties) {
        super(executorService, rabbitMQProperties);
    }

    @Override
    protected void setUp(Channel channel) throws Exception {

    }
}
