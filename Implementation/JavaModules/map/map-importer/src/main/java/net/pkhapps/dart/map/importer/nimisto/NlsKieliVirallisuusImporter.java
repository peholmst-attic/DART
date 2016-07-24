package net.pkhapps.dart.map.importer.nimisto;

import net.pkhapps.dart.map.database.tables.NlsKieliVirallisuus;
import net.pkhapps.dart.map.importer.AbstractJaxJooqImporter;
import org.jooq.DSLContext;
import org.xml.sax.ContentHandler;

/**
 * TODO Document me!
 */
public class NlsKieliVirallisuusImporter extends AbstractJaxJooqImporter {

    @Override
    protected ContentHandler createContentHandler(DSLContext dslContext) {
        return new NlsXsdEnumHandler<>(dslContext, NlsKieliVirallisuus.NLS_KIELI_VIRALLISUUS);
    }

    public static void main(String[] args) throws Exception {
        new NlsKieliVirallisuusImporter().importData();
    }
}
