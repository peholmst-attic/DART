package net.pkhapps.dart.map.importer.nimisto;

import net.pkhapps.dart.map.database.tables.NlsKunta;
import net.pkhapps.dart.map.importer.AbstractJaxJooqImporter;
import org.jooq.DSLContext;
import org.xml.sax.ContentHandler;

/**
 * TODO Document me!
 */
public class NlsKuntaImporter extends AbstractJaxJooqImporter {

    @Override
    protected ContentHandler createContentHandler(DSLContext dslContext) {
        return new NlsXsdEnumHandler<>(dslContext, NlsKunta.NLS_KUNTA);
    }

    public static void main(String[] args) throws Exception {
        new NlsKuntaImporter().importData();
    }
}
