package net.pkhapps.dart.map.importer.xsdenums;

import net.pkhapps.dart.map.database.tables.NlsMunicipality;
import net.pkhapps.dart.map.importer.AbstractJaxJooqImporter;
import org.jooq.DSLContext;
import org.xml.sax.ContentHandler;

import java.net.URI;

/**
 * Importer that imports the Finnish municipalities from the NLS XSD schema.
 *
 * @see <a href="http://www.maanmittauslaitos.fi/en/">NLS</a>
 */
public class NlsMunicipalityImporter extends AbstractJaxJooqImporter {

    public NlsMunicipalityImporter() {
        super(URI.create("http://xml.nls.fi/Nimisto/Nimistorekisteri/kunta.xsd"));
    }

    @Override
    protected ContentHandler createContentHandler(DSLContext dslContext) {
        return new NlsXsdEnumHandler<>(dslContext, NlsMunicipality.NLS_MUNICIPALITY);
    }

    public static void main(String[] args) throws Exception {
        // TODO Remove
        System.setProperty("jdbc.url", "jdbc:postgresql:dart_map");
        System.setProperty("jdbc.user", "dart");
        System.setProperty("jdbc.password", "dart");

        new NlsMunicipalityImporter().importData();
    }
}
