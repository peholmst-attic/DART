package net.pkhapps.dart.map.importer.xsdenums;

import net.pkhapps.dart.map.database.tables.NlsPlaceType;
import net.pkhapps.dart.map.importer.AbstractJaxJooqImporter;
import org.jooq.DSLContext;
import org.xml.sax.ContentHandler;

import java.net.URI;

/**
 * TODO Document me!
 */
public class NlsPlaceTypeImporter extends AbstractJaxJooqImporter {

    public NlsPlaceTypeImporter() {
        super(URI.create("http://xml.nls.fi/Nimisto/Nimistorekisteri/paikkatyyppi.xsd"));
    }

    @Override
    protected ContentHandler createContentHandler(DSLContext dslContext) {
        return new NlsXsdEnumHandler<>(dslContext, NlsPlaceType.NLS_PLACE_TYPE);
    }

    public static void main(String[] args) throws Exception {
        // TODO Remove
        System.setProperty("jdbc.url", "jdbc:postgresql:dart_map");
        System.setProperty("jdbc.user", "dart");
        System.setProperty("jdbc.password", "dart");

        new NlsPlaceTypeImporter().importData();
    }
}
