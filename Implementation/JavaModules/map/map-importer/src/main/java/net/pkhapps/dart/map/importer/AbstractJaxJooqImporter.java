package net.pkhapps.dart.map.importer;

import org.jooq.DSLContext;
import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.net.URI;

/**
 * TODO Document me!
 */
public abstract class AbstractJaxJooqImporter extends AbstractJooqImporter {

    private final URI source;

    public AbstractJaxJooqImporter() {
        source = URI.create(getRequiredProperty("source"));
    }

    protected AbstractJaxJooqImporter(URI source) {
        this.source = source;
    }

    @Override
    protected void importData(DSLContext dslContext) throws Exception {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setNamespaceAware(true);
        SAXParser parser = saxParserFactory.newSAXParser();
        XMLReader xmlReader = parser.getXMLReader();
        xmlReader.setContentHandler(createContentHandler(dslContext));
        xmlReader.parse(source.toString());
    }

    protected abstract ContentHandler createContentHandler(DSLContext dslContext);
}
