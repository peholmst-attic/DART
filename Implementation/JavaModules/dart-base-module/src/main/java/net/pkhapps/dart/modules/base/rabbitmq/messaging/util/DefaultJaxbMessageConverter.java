package net.pkhapps.dart.modules.base.rabbitmq.messaging.util;

import com.rabbitmq.client.AMQP;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * TODO Document me!
 */
public class DefaultJaxbMessageConverter implements JaxbMessageConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultJaxbMessageConverter.class);
    private static final String CONTENT_TYPE_XML = "application/xml";

    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;
    private final XMLInputFactory xmlInputFactory;

    /**
     * @param jaxbContext
     * @throws JAXBException
     */
    public DefaultJaxbMessageConverter(@NotNull JAXBContext jaxbContext, @NotNull XMLInputFactory xmlInputFactory)
            throws JAXBException {
        Objects.requireNonNull(jaxbContext, "jaxbContext must not be null");
        marshaller = jaxbContext.createMarshaller();
        unmarshaller = jaxbContext.createUnmarshaller();
        this.xmlInputFactory = Objects.requireNonNull(xmlInputFactory, "xmlInputFactory must not be null");
    }

    @Override
    public <T> JAXBElement<T> toJAXBElement(@NotNull Class<T> pojoClass, @NotNull byte[] body,
                                            @NotNull AMQP.BasicProperties properties)
            throws JAXBException {
        Objects.requireNonNull(body, "body must not be null");
        Objects.requireNonNull(properties, "properties must not be null");
        final String contentType = properties.getContentType();

        if (contentType != null && !contentType.equals(CONTENT_TYPE_XML)) {
            throw new JAXBException("Unsupported content type: " + contentType);
        }

        QName qname;
        try {
            qname = QName.valueOf(properties.getType());
        } catch (IllegalArgumentException ex) {
            throw new JAXBException("Invalid QName: " + properties.getType());
        }

        LOGGER.trace("Creating [{}] from XML", pojoClass.getName());
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(body);
        try {
            final XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(inputStream);
            return unmarshaller.unmarshal(reader, pojoClass);
        } catch (XMLStreamException ex) {
            throw new JAXBException("Error reading from input stream", ex);
        }
    }

    @Override
    public <H extends Handle<?>> byte[] fromPojo(@NotNull H handle,
                                                 @NotNull AMQP.BasicProperties.Builder propertyBuilder)
            throws MessageConvertionException {
        Objects.requireNonNull(handle, "pojo must not be null");
        Objects.requireNonNull(propertyBuilder, "propertyBuilder must not be null");
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        if (handle instanceof JaxbHandle) {
            JaxbHandle<?> jaxbHandle = (JaxbHandle) handle;
            JAXBElement<?> element = handle.get();
            LOGGER.trace("Converting [{}] to XML", pojo);
            try {
                marshaller.marshal(pojo, outputStream);
                propertyBuilder.contentType(CONTENT_TYPE_XML).contentEncoding(StandardCharsets.UTF_8.name());
                return outputStream.toByteArray();
            } catch (JAXBException ex) {
                throw new MessageConvertionException(ex);
            }
        } else {
            throw new MessageConvertionException("Unsupported handle");
        }
    }

    public static class JaxbHandle<T> implements Handle<JAXBElement<T>> {

        public Class<T> getPojoClass() {
            return null;
        }

        @Override
        public JAXBElement<T> get() {
            return null;
        }
    }
}
