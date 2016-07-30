package net.pkhapps.dart.map.importer.nimisto;

import net.pkhapps.dart.map.database.tables.NlsKieli;
import net.pkhapps.dart.map.database.tables.records.NlsKieliRecord;
import net.pkhapps.dart.map.importer.AbstractJaxJooqImporter;
import net.pkhapps.dart.map.importer.xsdenums.NlsXsdEnumHandler;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;

/**
 * TODO Document me!
 */
public class NlsKieliImporter extends AbstractJaxJooqImporter {

    @Override
    protected ContentHandler createContentHandler(DSLContext dslContext) {
        return new NlsXsdEnumHandler<NlsKieliRecord>(dslContext, NlsKieli.NLS_KIELI) {
            @Override
            protected void setId(NlsKieliRecord record, Table<NlsKieliRecord> table, Attributes attributes) {
                record.setValue(table.field("id", String.class), attributes.getValue("value"));
            }
        };
    }

    public static void main(String[] args) throws Exception {
        new NlsKieliImporter().importData();
    }
}
