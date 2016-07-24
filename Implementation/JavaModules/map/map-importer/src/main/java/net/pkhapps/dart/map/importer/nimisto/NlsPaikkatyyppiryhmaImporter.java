package net.pkhapps.dart.map.importer.nimisto;

import net.pkhapps.dart.map.database.tables.NlsPaikkatyyppiryhma;
import net.pkhapps.dart.map.importer.AbstractJaxJooqImporter;
import org.jooq.DSLContext;
import org.xml.sax.ContentHandler;

/**
 * TODO Document me!
 */
public class NlsPaikkatyyppiryhmaImporter extends AbstractJaxJooqImporter {

    @Override
    protected ContentHandler createContentHandler(DSLContext dslContext) {
        return new NlsXsdEnumHandler<>(dslContext, NlsPaikkatyyppiryhma.NLS_PAIKKATYYPPIRYHMA);
    }

    public static void main(String[] args) throws Exception {
        new NlsPaikkatyyppiryhmaImporter().importData();
    }
}
