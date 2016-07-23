package net.pkhapps.dart.map.importer;

import net.pkhapps.dart.map.database.tables.NlsPaikkatyyppiryhma;
import org.jooq.DSLContext;
import org.xml.sax.ContentHandler;

/**
 * TODO Document me!
 */
public class NlsPaikkatyyppiryhmaImporter extends AbstractJaxJooqImporter {

    @Override
    ContentHandler createContentHandler(DSLContext dslContext) {
        return new NlsXsdEnumHandler<>(dslContext, NlsPaikkatyyppiryhma.NLS_PAIKKATYYPPIRYHMA);
    }

    public static void main(String[] args) throws Exception {
        new NlsPaikkatyyppiryhmaImporter().importData();
    }
}
