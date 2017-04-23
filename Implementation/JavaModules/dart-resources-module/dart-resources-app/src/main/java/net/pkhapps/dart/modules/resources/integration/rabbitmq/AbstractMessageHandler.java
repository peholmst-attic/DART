package net.pkhapps.dart.modules.resources.integration.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import net.pkhapps.dart.modules.resources.RabbitMQProperties;
import net.pkhapps.dart.modules.resources.integration.xsd.Message;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Clock;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO document me
 */
abstract class AbstractMessageHandler extends DefaultConsumer {

    static final String CONTENT_TYPE_XML = "application/xml";
    static final String CONTENT_TYPE_JSON = "application/json";


    final Logger logger = LoggerFactory.getLogger(getClass());
    private final RabbitMQProperties rabbitMQProperties;
    private final Clock clock;
    final Marshaller xmlMarshaller;
    final Marshaller jsonMarshaller;
    final Unmarshaller xmlUnmarshaller;
    final Unmarshaller jsonUnmarshaller;

    AbstractMessageHandler(Channel channel, RabbitMQProperties rabbitMQProperties, Clock clock, JAXBContext jaxbContext)
            throws JAXBException {
        super(channel);
        this.rabbitMQProperties = rabbitMQProperties;
        this.clock = clock;

        xmlMarshaller = jaxbContext.createMarshaller();
        xmlMarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, CONTENT_TYPE_XML);

        jsonMarshaller = jaxbContext.createMarshaller();
        jsonMarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, CONTENT_TYPE_JSON);

        xmlUnmarshaller = jaxbContext.createUnmarshaller();
        xmlUnmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, CONTENT_TYPE_XML);

        jsonUnmarshaller = jaxbContext.createUnmarshaller();
        jsonUnmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, CONTENT_TYPE_JSON);
    }

    /**
     * @param properties
     * @param body
     * @return
     */
    Message readMessage(@NotNull AMQP.BasicProperties properties, @NotNull byte[] body) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(body)) {
            if (CONTENT_TYPE_JSON.equals(properties.getContentType())) {
                logger.trace("Unmarshalling message as JSON");
                return (Message) jsonUnmarshaller.unmarshal(inputStream);
            } else {
                logger.trace("Unmarshalling message as XML");
                return (Message) xmlUnmarshaller.unmarshal(inputStream);
            }
        } catch (Exception ex) {
            logger.warn("Received unknown message, ignoring", ex);
            return null;
        }
    }

    /**
     * @param properties
     * @return
     */
    AMQP.BasicProperties.Builder buildReplyProperties(@NotNull AMQP.BasicProperties properties) {
        // @formatter:off
        return new AMQP.BasicProperties.Builder()
                .timestamp(Date.from(clock.instant()))
                .appId(rabbitMQProperties.getMessageAppId().get())
                .userId(rabbitMQProperties.getUsername().get())
                .correlationId(properties.getCorrelationId())
                .expiration(rabbitMQProperties.getMessageExpiration().get());
        // @formatter:on
    }

    /**
     * @param code
     * @param message
     * @param properties
     * @throws IOException
     */
    void sendError(int code, @Nullable String message, @NotNull AMQP.BasicProperties properties) throws IOException {
        logger.debug("Sending error [{}: {}] to [{}]", code, message,
                properties.getReplyTo());
        Map<String, Object> headers = new HashMap<>();
        headers.put(StatusCodes.STATUS_CODE_HEADER, code);
        if (message != null) {
            headers.put(StatusCodes.STATUS_MESSAGE_HEADER, message);
        }
        getChannel()
                .basicPublish("", properties.getReplyTo(), buildReplyProperties(properties).headers(headers).build(),
                        null);
    }
}
