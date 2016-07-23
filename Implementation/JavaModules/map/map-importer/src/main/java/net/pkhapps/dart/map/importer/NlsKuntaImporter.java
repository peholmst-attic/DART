package net.pkhapps.dart.map.importer;

import net.pkhapps.dart.map.database.tables.NlsKunta;
import org.jooq.DSLContext;
import org.xml.sax.ContentHandler;

/**
 * TODO Document me!
 */
public class NlsKuntaImporter extends AbstractJaxJooqImporter {

    @Override
    ContentHandler createContentHandler(DSLContext dslContext) {
        return new NlsXsdEnumHandler<>(dslContext, NlsKunta.NLS_KUNTA);
    }

    public static void main(String[] args) throws Exception {
        new NlsKuntaImporter().importData();
    }
}
