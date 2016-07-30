package net.pkhapps.dart.map.importer.nimisto;

import net.pkhapps.dart.map.database.tables.NlsSuuralue;
import net.pkhapps.dart.map.importer.AbstractJaxJooqImporter;
import net.pkhapps.dart.map.importer.xsdenums.NlsXsdEnumHandler;
import org.jooq.DSLContext;
import org.xml.sax.ContentHandler;

/**
 * TODO Document me!
 */
public class NlsSuuralueImporter extends AbstractJaxJooqImporter {

    @Override
    protected ContentHandler createContentHandler(DSLContext dslContext) {
        return new NlsXsdEnumHandler<>(dslContext, NlsSuuralue.NLS_SUURALUE);
    }

    public static void main(String[] args) throws Exception {
        new NlsSuuralueImporter().importData();
    }
}
