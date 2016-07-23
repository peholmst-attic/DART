package net.pkhapps.dart.map.importer;

import net.pkhapps.dart.map.database.tables.NlsMaakunta;
import org.jooq.DSLContext;
import org.xml.sax.ContentHandler;

/**
 * TODO Document me!
 */
public class NlsMaakuntaImporter extends AbstractJaxJooqImporter {

    @Override
    ContentHandler createContentHandler(DSLContext dslContext) {
        return new NlsXsdEnumHandler<>(dslContext, NlsMaakunta.NLS_MAAKUNTA);
    }

    public static void main(String[] args) throws Exception {
        new NlsMaakuntaImporter().importData();
    }
}
