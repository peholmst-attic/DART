package net.pkhapps.dart.modules.base.rabbitmq.messaging.util;

import com.rabbitmq.client.AMQP;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

/**
 * TODO Document me!
 */
public interface JaxbMessageConverter {

    <T> JAXBElement<T> toJAXBElement(@NotNull Class<T> pojoClass, @NotNull byte[] body,
                                     @NotNull AMQP.BasicProperties properties)
            throws JAXBException;

    <T> byte[] fromJAXBElement(@NotNull JAXBElement<T> jaxbElement,
                               @NotNull AMQP.BasicProperties.Builder propertyBuilder)
            throws JAXBException;

}
