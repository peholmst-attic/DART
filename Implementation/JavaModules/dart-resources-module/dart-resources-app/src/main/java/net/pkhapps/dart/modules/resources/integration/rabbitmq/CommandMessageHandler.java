package net.pkhapps.dart.modules.resources.integration.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import net.pkhapps.dart.modules.resources.RabbitMQProperties;
import net.pkhapps.dart.modules.resources.integration.xsd.Message;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.time.Clock;
import java.util.Collections;

/**
 * TODO Document me!
 */
class CommandMessageHandler extends AbstractMessageHandler {

    private final CommandBroker commandBroker;

    CommandMessageHandler(Channel channel, RabbitMQProperties rabbitMQProperties, Clock clock, JAXBContext jaxbContext,
                          CommandBroker commandBroker) throws JAXBException {
        super(channel, rabbitMQProperties, clock, jaxbContext);
        this.commandBroker = commandBroker;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
            throws IOException {
        Message message = readMessage(properties, body);
        logger.trace("Acknowledging message [{}]", envelope.getDeliveryTag());
        getChannel().basicAck(envelope.getDeliveryTag(), false);
        if (message != null && properties.getReplyTo() != null) {
            try {
                logger.debug("Received message [{}]", message);
                commandBroker.handleCommand(message, properties.getUserId());
                sendOk(properties);
            } catch (HandlerNotFoundException ex) {
                logger.warn("Found no handler for message [{}], ignoring", message);
            } catch (AccessDeniedException ex) {
                sendError(StatusCodes.FORBIDDEN, null, properties);
            } catch (HandlerException ex) {
                sendError(ex.getErrorCode(), ex.getMessage(), properties);
            }
        }
    }

    private void sendOk(@NotNull AMQP.BasicProperties properties) throws IOException {
        logger.debug("Sending OK response to [{}]", properties.getReplyTo());
        AMQP.BasicProperties.Builder replyProperties = buildReplyProperties(properties)
                .headers(Collections.singletonMap(StatusCodes.STATUS_CODE_HEADER, StatusCodes.OK));
        getChannel().basicPublish("", properties.getReplyTo(), replyProperties.build(), null);
    }
}
