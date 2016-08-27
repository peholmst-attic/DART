package net.pkhapps.dart.map.importer.xsdenums;

import net.pkhapps.dart.map.database.tables.NlsRoadClass;
import net.pkhapps.dart.map.importer.AbstractJaxJooqImporter;
import org.jooq.DSLContext;
import org.xml.sax.ContentHandler;

import java.net.URI;

/**
 * Importer that imports the Finnish road classes from the NLS XSD schema.
 *
 * @see <a href="http://www.maanmittauslaitos.fi/en/">NLS</a>
 */
public class NlsRoadClassImporter extends AbstractJaxJooqImporter {

    public NlsRoadClassImporter() {
        super(URI.create("http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Koodistot/Tieluokka.xsd"));
    }

    @Override
    protected ContentHandler createContentHandler(DSLContext dslContext) {
        return new NlsXsdEnumHandler<>(dslContext, NlsRoadClass.NLS_ROAD_CLASS);
    }

    public static void main(String[] args) throws Exception {
        // TODO Remove
        System.setProperty("jdbc.url", "jdbc:postgresql:dart_map");
        System.setProperty("jdbc.user", "dart");
        System.setProperty("jdbc.password", "dart");

        new NlsRoadClassImporter().importData();
    }
}
