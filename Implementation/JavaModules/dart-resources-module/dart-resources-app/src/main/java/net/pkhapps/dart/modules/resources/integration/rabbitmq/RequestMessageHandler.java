package net.pkhapps.dart.modules.resources.integration.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import net.pkhapps.dart.modules.resources.RabbitMQProperties;
import net.pkhapps.dart.modules.resources.integration.xsd.Message;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.namespace.QName;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.Collections;

/**
 * TODO Document me!
 */
class RequestMessageHandler extends AbstractMessageHandler {

    private final JAXBIntrospector introspector;
    private final RequestBroker requestBroker;

    RequestMessageHandler(Channel channel, RabbitMQProperties rabbitMQProperties, Clock clock, JAXBContext jaxbContext,
                          RequestBroker requestBroker) throws JAXBException {
        super(channel, rabbitMQProperties, clock, jaxbContext);
        this.introspector = jaxbContext.createJAXBIntrospector();
        this.requestBroker = requestBroker;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
            throws IOException {
        Message message = readMessage(properties, body);
        logger.trace("Acknowledging message [{}]", envelope.getDeliveryTag());
        getChannel().basicAck(envelope.getDeliveryTag(), false);
        // No use in processing the request if there is no replyTo destination
        if (message != null && properties.getReplyTo() != null) {
            try {
                logger.debug("Received message [{}]", message);
                Message response = requestBroker.handleRequest(message, properties.getUserId());
                sendResponse(response, properties);
            } catch (HandlerNotFoundException ex) {
                logger.warn("Found no handler for message [{}], ignoring", message);
            } catch (AccessDeniedException ex) {
                sendError(StatusCodes.FORBIDDEN, null, properties);
            } catch (HandlerException ex) {
                sendError(ex.getErrorCode(), ex.getMessage(), properties);
            } catch (JAXBException ex) {
                logger.error("Could not convert response to XML or JSON, ignoring", ex);
            }
        }
    }

    private void sendResponse(@NotNull Message response, @NotNull AMQP.BasicProperties properties)
            throws IOException, JAXBException {

        String contentType;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (CONTENT_TYPE_JSON.equals(properties.getContentType())) {
            jsonMarshaller.marshal(response, outputStream);
            contentType = CONTENT_TYPE_JSON;
        } else {
            xmlMarshaller.marshal(response, outputStream);
            contentType = CONTENT_TYPE_XML;
        }

        logger.debug("Sending response [{}] to [{}] using content type [{}]", response, properties.getReplyTo(),
                contentType);

        QName elementName = introspector.getElementName(response);
        AMQP.BasicProperties.Builder replyProperties = buildReplyProperties(properties).contentEncoding(
                StandardCharsets.UTF_8.name()).contentType(contentType)
                .headers(Collections.singletonMap(StatusCodes.STATUS_CODE_HEADER, StatusCodes.OK));
        if (elementName != null) {
            replyProperties.type(elementName.getNamespaceURI());
        }
        getChannel().basicPublish("", properties.getReplyTo(), replyProperties.build(), outputStream.toByteArray());
    }
}
