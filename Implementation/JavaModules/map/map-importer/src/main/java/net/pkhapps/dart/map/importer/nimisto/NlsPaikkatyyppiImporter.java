package net.pkhapps.dart.map.importer.nimisto;

import net.pkhapps.dart.map.database.tables.NlsPaikkatyyppi;
import net.pkhapps.dart.map.importer.AbstractJaxJooqImporter;
import net.pkhapps.dart.map.importer.xsdenums.NlsXsdEnumHandler;
import org.jooq.DSLContext;
import org.xml.sax.ContentHandler;

/**
 * TODO Document me!
 */
public class NlsPaikkatyyppiImporter extends AbstractJaxJooqImporter {

    @Override
    protected ContentHandler createContentHandler(DSLContext dslContext) {
        return new NlsXsdEnumHandler<>(dslContext, NlsPaikkatyyppi.NLS_PAIKKATYYPPI);
    }

    public static void main(String[] args) throws Exception {
        new NlsPaikkatyyppiImporter().importData();
    }
}
