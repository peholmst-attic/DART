package net.pkhapps.dart.modules.resources.integration;

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
        return JAXBContext.newInstance(ObjectFactory.class.getPackage().getName());
    }
}
