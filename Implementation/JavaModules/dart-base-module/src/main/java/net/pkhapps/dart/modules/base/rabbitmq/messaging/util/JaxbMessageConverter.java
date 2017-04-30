package net.pkhapps.dart.modules.base.rabbitmq.messaging.util;

import com.rabbitmq.client.AMQP;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * TODO Document me!
 */
public class JaxbMessageConverter implements MessageConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JaxbMessageConverter.class);
    private static final String CONTENT_TYPE_XML = "application/xml";

    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;

    /**
     * @param jaxbContext
     * @throws JAXBException
     */
    public JaxbMessageConverter(@NotNull JAXBContext jaxbContext) throws JAXBException {
        Objects.requireNonNull(jaxbContext, "jaxbContext must not be null");
        marshaller = jaxbContext.createMarshaller();
        unmarshaller = jaxbContext.createUnmarshaller();
    }

    @Override
    public <T> T toPojo(@NotNull Class<T> pojoClass, @NotNull byte[] body, @NotNull AMQP.BasicProperties properties)
            throws MessageConvertionException {
        Objects.requireNonNull(pojoClass, "pojoClass must not be null");
        Objects.requireNonNull(body, "body must not be null");
        Objects.requireNonNull(properties, "properties must not be null");
        final String contentType = properties.getContentType();

        if (contentType != null && !contentType.equals(CONTENT_TYPE_XML)) {
            throw new MessageConvertionException("Unsupported content type: " + contentType);
        }

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(body);

        LOGGER.trace("Creating [{}] from XML", pojoClass.getName());
        try {
            return pojoClass.cast(unmarshaller.unmarshal(inputStream));
        } catch (ClassCastException | JAXBException ex) {
            throw new MessageConvertionException(ex);
        }
    }

    @Override
    public <T> byte[] fromPojo(@NotNull T pojo, @NotNull AMQP.BasicProperties.Builder propertyBuilder)
            throws MessageConvertionException {
        Objects.requireNonNull(pojo, "pojo must not be null");
        Objects.requireNonNull(propertyBuilder, "propertyBuilder must not be null");
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        LOGGER.trace("Converting [{}] to XML", pojo);
        try {
            marshaller.marshal(pojo, outputStream);
            propertyBuilder.contentType(CONTENT_TYPE_XML).contentEncoding(StandardCharsets.UTF_8.name());
            return outputStream.toByteArray();
        } catch (JAXBException ex) {
            throw new MessageConvertionException(ex);
        }
    }
}
