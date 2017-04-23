package net.pkhapps.dart.modules.resources.integration;

import net.pkhapps.dart.modules.resources.integration.xsd.ObjectFactory;
import org.eclipse.persistence.jaxb.JAXBContextFactory;

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
        // By using Moxy, we can support both XML and JSON using the same JAXB API.
        return JAXBContextFactory
                .createContext(ObjectFactory.class.getPackage().getName(), getClass().getClassLoader());
    }
}
