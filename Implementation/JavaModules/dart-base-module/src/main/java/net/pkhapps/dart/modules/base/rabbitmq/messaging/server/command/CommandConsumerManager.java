package net.pkhapps.dart.modules.base.rabbitmq.messaging.server.command;

import com.rabbitmq.client.Channel;
import net.pkhapps.dart.modules.base.rabbitmq.RabbitMQChannelManager;
import net.pkhapps.dart.modules.base.rabbitmq.RabbitMQProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by peholmst on 09/05/2017.
 */
@ApplicationScoped
public class CommandConsumerManager extends RabbitMQChannelManager {

    @Inject
    public CommandConsumerManager(ScheduledExecutorService executorService,
                                  RabbitMQProperties rabbitMQProperties) {
        super(executorService, rabbitMQProperties);
    }

    @Override
    protected void setUp(Channel channel) throws Exception {

    }
}
