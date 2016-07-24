package net.pkhapps.dart.map.importer.nimisto;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Objects;

/**
 * TODO Document me!
 */
class NlsXsdEnumHandler<R extends TableRecord> extends DefaultHandler {

    private final DSLContext dslContext;
    private final Table<R> table;

    private Field<String> currentField;
    private R currentRecord;

    public NlsXsdEnumHandler(DSLContext dslContext, Table<R> table) {
        this.dslContext = Objects.requireNonNull(dslContext);
        this.table = Objects.requireNonNull(table);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("enumeration".equals(localName)) {
            currentRecord = dslContext.newRecord(table);
            setId(currentRecord, table, attributes);
        } else if ("documentation".equals(localName) && currentRecord != null) {
            currentField = null;
            switch (attributes.getValue("xml:lang")) {
                case "fin":
                    currentField = table.field("name_fi", String.class);
                    break;
                case "swe":
                    currentField = table.field("name_sv", String.class);
                    break;
                case "eng":
                    currentField = table.field("name_en", String.class);
                    break;
            }
        }
    }

    protected void setId(R record, Table<R> table, Attributes attributes) {
        record.setValue(table.field("id", Long.class), Long.valueOf(attributes.getValue("value")));
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (currentField != null && currentRecord != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(ch, start, length);
            currentRecord.setValue(currentField, sb.toString());
            currentField = null;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("enumeration".equals(localName) && currentRecord != null) {
            currentRecord.insert();
            currentRecord = null;
        }
    }
}
