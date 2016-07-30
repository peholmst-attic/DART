package net.pkhapps.dart.map.importer.nimisto;

import net.pkhapps.dart.map.database.tables.NlsSeutukunta;
import net.pkhapps.dart.map.importer.AbstractJaxJooqImporter;
import net.pkhapps.dart.map.importer.xsdenums.NlsXsdEnumHandler;
import org.jooq.DSLContext;
import org.xml.sax.ContentHandler;

/**
 * TODO Document me!
 */
public class NlsSeutukuntaImporter extends AbstractJaxJooqImporter {

    @Override
    protected ContentHandler createContentHandler(DSLContext dslContext) {
        return new NlsXsdEnumHandler<>(dslContext, NlsSeutukunta.NLS_SEUTUKUNTA);
    }

    public static void main(String[] args) throws Exception {
        new NlsSeutukuntaImporter().importData();
    }
}
