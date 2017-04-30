package net.pkhapps.dart.modules.resources.integration;

import net.pkhapps.dart.modules.base.rabbitmq.messaging.util.JaxbMessageConverter;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.util.MessageConverter;
import net.pkhapps.dart.modules.resources.integration.xsd.ObjectFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 * TODO Document me!
 */
@ApplicationScoped
class JAXBProvider {

    @Produces
    @ApplicationScoped
    JAXBContext getJAXBContext() throws JAXBException {
        return JAXBContext.newInstance(ObjectFactory.class.getPackage().getName(), getClass().getClassLoader());
    }

    @Produces
    @ApplicationScoped
    MessageConverter getMessageConverter(JAXBContext jaxbContext) throws JAXBException {
        return new JaxbMessageConverter(jaxbContext);
    }
}
