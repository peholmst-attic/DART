package net.pkhapps.dart.modules.resources.integration.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import net.pkhapps.dart.modules.resources.RabbitMQProperties;
import net.pkhapps.dart.modules.resources.integration.xsd.Message;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.Collections;

/**
 * TODO Document me!
 */
class RequestMessageHandler extends AbstractMessageHandler {

    private static final String CONTENT_TYPE = "application/xml";

    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;
    private final JAXBIntrospector introspector;
    private final RequestBroker requestBroker;

    RequestMessageHandler(Channel channel, RabbitMQProperties rabbitMQProperties, Clock clock, JAXBContext jaxbContext,
                          RequestBroker requestBroker) throws JAXBException {
        super(channel, rabbitMQProperties, clock);
        this.marshaller = jaxbContext.createMarshaller();
        this.unmarshaller = jaxbContext.createUnmarshaller();
        this.introspector = jaxbContext.createJAXBIntrospector();
        this.requestBroker = requestBroker;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
            throws IOException {
        Message message = null;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(body)) {
            message = (Message) unmarshaller.unmarshal(inputStream);
        } catch (Exception ex) {
            logger.warn("Received unknown message, ignoring", ex);
        }
        logger.trace("Acknowledging message [{}]", envelope.getDeliveryTag());
        getChannel().basicAck(envelope.getDeliveryTag(), false);
        if (message != null && properties.getReplyTo() != null) {
            try {
                logger.debug("Received message [{}]", message);
                Object response = requestBroker.handleRequest(message, properties.getUserId());
                sendResponse(response, properties);
            } catch (HandlerNotFoundException ex) {
                logger.warn("Found no handler for message [{}], ignoring", message);
            } catch (AccessDeniedException ex) {
                sendError(StatusCodes.FORBIDDEN, null, properties);
            } catch (HandlerException ex) {
                sendError(ex.getErrorCode(), ex.getMessage(), properties);
            } catch (JAXBException ex) {
                logger.error("Could not convert response to XML, ignoring", ex);
            }
        }
    }

    private void sendResponse(@NotNull Object response, @NotNull AMQP.BasicProperties properties)
            throws IOException, JAXBException {
        logger.debug("Sending response [{}] to [{}]", response, properties.getReplyTo());
        QName elementName = introspector.getElementName(response);
        AMQP.BasicProperties.Builder replyProperties = buildReplyProperties(properties).contentEncoding(
                StandardCharsets.UTF_8.name()).contentType(CONTENT_TYPE)
                .headers(Collections.singletonMap(StatusCodes.STATUS_CODE_HEADER, StatusCodes.OK));
        if (elementName != null) {
            replyProperties.type(elementName.getNamespaceURI());
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        marshaller.marshal(response, outputStream);
        getChannel().basicPublish("", properties.getReplyTo(), replyProperties.build(), outputStream.toByteArray());
    }
}
