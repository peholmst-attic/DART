package net.pkhapps.dart.map.importer.maastotiedot;

import net.pkhapps.dart.map.importer.AbstractJooqImporter;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.xml.AppSchemaConfiguration;
import org.geotools.xml.PullParser;
import org.geotools.xml.resolver.SchemaCache;
import org.geotools.xml.resolver.SchemaResolver;
import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import org.opengis.feature.simple.SimpleFeature;

import javax.xml.namespace.QName;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Document me!
 */
public abstract class AbstractNlsMaastotiedotImporter<R extends UpdatableRecord<R>> extends AbstractJooqImporter {

    protected static final String NAMESPACE_URI = "http://xml.nls.fi/XML/Namespace/Maastotietojarjestelma/SiirtotiedostonMalli/2011-02";
    private final AppSchemaConfiguration configuration;

    public AbstractNlsMaastotiedotImporter() throws IOException {
        File tempFile = File.createTempFile("DART-NLS-IMPORTER", ".txt");
        SchemaCache schemaCache = new SchemaCache(tempFile.getParentFile(), true);
        SchemaResolver resolver = new SchemaResolver(schemaCache);
        configuration = new AppSchemaConfiguration(NAMESPACE_URI,
                "http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Maastotiedot.xsd",
                resolver);
        configuration.addDependency(new GMLConfiguration());
    }

    @Override
    protected void importData(DSLContext dslContext) throws Exception {
        // TODO Read files from a directory instead
        File dataFile = new File("/Users/petterprivate/Google Drive/Maps/maastotietokanta_tiesto/L33.xml");
        try (InputStream is = new FileInputStream(dataFile)) {
            importData(is, dslContext);
        }
    }

    protected void importData(InputStream inputStream, DSLContext dslContext) throws Exception {
        PullParser parser = new PullParser(configuration, inputStream, getFeatureQName());
        List<R> records = new ArrayList<>();
        int count = 0;
        SimpleFeature feature = (SimpleFeature) parser.parse();
        while (feature != null) {
            records.add(createRecord(feature, dslContext));
            feature = (SimpleFeature) parser.parse();
            ++count;

            if (count % 100 == 0) {
                runBatch(records, dslContext);
            }
        }
        if (records.size() > 0) {
            runBatch(records, dslContext);
        }
    }

    protected abstract QName getFeatureQName();

    protected abstract R createRecord(SimpleFeature feature, DSLContext dslContext);
}
