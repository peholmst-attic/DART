package net.pkhapps.dart.map.importer.nimisto;

import net.pkhapps.dart.map.database.tables.NlsMaakunta;
import net.pkhapps.dart.map.importer.AbstractJaxJooqImporter;
import net.pkhapps.dart.map.importer.xsdenums.NlsXsdEnumHandler;
import org.jooq.DSLContext;
import org.xml.sax.ContentHandler;

/**
 * TODO Document me!
 */
public class NlsMaakuntaImporter extends AbstractJaxJooqImporter {

    @Override
    protected ContentHandler createContentHandler(DSLContext dslContext) {
        return new NlsXsdEnumHandler<>(dslContext, NlsMaakunta.NLS_MAAKUNTA);
    }

    public static void main(String[] args) throws Exception {
        new NlsMaakuntaImporter().importData();
    }
}
