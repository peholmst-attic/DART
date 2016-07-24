package net.pkhapps.dart.map.importer.nimisto;

import net.pkhapps.dart.map.database.tables.NlsKieliEnemmisto;
import net.pkhapps.dart.map.importer.AbstractJaxJooqImporter;
import org.jooq.DSLContext;
import org.xml.sax.ContentHandler;

/**
 * TODO Document me!
 */
public class NlsKieliEnemmistoImporter extends AbstractJaxJooqImporter {

    @Override
    protected ContentHandler createContentHandler(DSLContext dslContext) {
        return new NlsXsdEnumHandler<>(dslContext, NlsKieliEnemmisto.NLS_KIELI_ENEMMISTO);
    }

    public static void main(String[] args) throws Exception {
        new NlsKieliEnemmistoImporter().importData();
    }
}
