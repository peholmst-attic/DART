package net.pkhapps.dart.map.importer;

import net.pkhapps.dart.map.database.tables.NlsSuuralue;
import org.jooq.DSLContext;
import org.xml.sax.ContentHandler;

/**
 * TODO Document me!
 */
public class NlsSuuralueImporter extends AbstractJaxJooqImporter {

    @Override
    ContentHandler createContentHandler(DSLContext dslContext) {
        return new NlsXsdEnumHandler<>(dslContext, NlsSuuralue.NLS_SUURALUE);
    }

    public static void main(String[] args) throws Exception {
        new NlsSuuralueImporter().importData();
    }
}
