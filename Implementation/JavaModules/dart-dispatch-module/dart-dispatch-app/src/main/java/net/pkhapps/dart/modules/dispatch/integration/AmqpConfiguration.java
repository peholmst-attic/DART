package net.pkhapps.dart.modules.dispatch.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.inbound.AmqpInboundChannelAdapter;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

@Configuration
public class AmqpConfiguration {

    @Bean
    public MessageChannel amqpInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public AmqpInboundChannelAdapter inbound(SimpleMessageListenerContainer listenerContainer,
                                             @Qualifier("amqpInputChannel") MessageChannel channel,
                                             MessageConverter messageConverter) {
        AmqpInboundChannelAdapter adapter = new AmqpInboundChannelAdapter(listenerContainer);
        adapter.setOutputChannel(channel);
        adapter.setMessageConverter(messageConverter);
        return adapter;
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames("net.pkhapps.dart.modules.dispatch.command");
        // TODO Configure more stuff
        return container;
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
