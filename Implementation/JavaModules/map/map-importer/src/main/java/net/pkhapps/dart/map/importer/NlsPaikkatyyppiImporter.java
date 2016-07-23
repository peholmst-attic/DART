package net.pkhapps.dart.map.importer;

import net.pkhapps.dart.map.database.tables.NlsPaikkatyyppi;
import org.jooq.DSLContext;
import org.xml.sax.ContentHandler;

/**
 * TODO Document me!
 */
public class NlsPaikkatyyppiImporter extends AbstractJaxJooqImporter {

    @Override
    ContentHandler createContentHandler(DSLContext dslContext) {
        return new NlsXsdEnumHandler<>(dslContext, NlsPaikkatyyppi.NLS_PAIKKATYYPPI);
    }

    public static void main(String[] args) throws Exception {
        new NlsPaikkatyyppiImporter().importData();
    }
}
