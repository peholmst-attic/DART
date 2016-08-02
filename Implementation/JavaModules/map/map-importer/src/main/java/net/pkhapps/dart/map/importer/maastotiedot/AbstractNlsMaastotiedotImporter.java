package net.pkhapps.dart.map.importer.maastotiedot;

import net.pkhapps.dart.common.Coordinates;
import net.pkhapps.dart.map.importer.AbstractJooqImporter;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.CRS;
import org.geotools.xml.AppSchemaConfiguration;
import org.geotools.xml.PullParser;
import org.geotools.xml.resolver.SchemaCache;
import org.geotools.xml.resolver.SchemaResolver;
import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import javax.xml.namespace.QName;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO Document me!
 */
public abstract class AbstractNlsMaastotiedotImporter<R extends UpdatableRecord<R>> extends AbstractJooqImporter {

    protected static final String NAMESPACE_URI = "http://xml.nls.fi/XML/Namespace/Maastotietojarjestelma/SiirtotiedostonMalli/2011-02";
    protected final CoordinateReferenceSystem ETRS_TM35FIN;
    protected final CoordinateReferenceSystem WGS84;
    protected final MathTransform transform;


    private final AppSchemaConfiguration configuration;

    public AbstractNlsMaastotiedotImporter() throws Exception {
        File tempFile = File.createTempFile("DART-NLS-IMPORTER", ".txt");
        SchemaCache schemaCache = new SchemaCache(tempFile.getParentFile(), true);
        SchemaResolver resolver = new SchemaResolver(schemaCache);
        configuration = new AppSchemaConfiguration(NAMESPACE_URI,
                "http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Maastotiedot.xsd",
                resolver);
        // Intentionally left out the GMLConfiguration, forcing the parser to return
        // maps instead. This is because some important information was left out from
        // the SimpleFeature, but included in the map representation.
        ETRS_TM35FIN = CRS.parseWKT("PROJCS[\"ETRS89 / ETRS-TM35FIN\",GEOGCS[\"ETRS89\",DATUM[\"European_Terrestrial_Reference_System_1989\",SPHEROID[\"GRS 1980\",6378137,298.257222101,AUTHORITY[\"EPSG\",\"7019\"]],AUTHORITY[\"EPSG\",\"6258\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.01745329251994328,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4258\"]],UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],PROJECTION[\"Transverse_Mercator\"],PARAMETER[\"latitude_of_origin\",0],PARAMETER[\"central_meridian\",27],PARAMETER[\"scale_factor\",0.9996],PARAMETER[\"false_easting\",500000],PARAMETER[\"false_northing\",0],AUTHORITY[\"EPSG\",\"3067\"],AXIS[\"Easting\",EAST],AXIS[\"Northing\",NORTH]]");
        WGS84 = CRS.parseWKT("GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.01745329251994328,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]]");
        transform = CRS.findMathTransform(ETRS_TM35FIN, WGS84, true);
    }

    @Override
    protected void importData(DSLContext dslContext) throws Exception {
        File dataDirectory = new File("/Users/petterprivate/Google Drive/Maps/maastotietokanta_tiesto");
        if (!dataDirectory.isDirectory()) {
            throw new IllegalArgumentException("Not a directory: " + dataDirectory.getCanonicalPath());
        }
        try {
            Files.list(dataDirectory.toPath())
                    .filter(path -> path.toString().toLowerCase().endsWith(".xml"))
                    .forEach(path -> {
                        try (InputStream is = Files.newInputStream(path, StandardOpenOption.READ)) {
                            System.out.printf("Importing from %s%n", path.toAbsolutePath());
                            importData(is, dslContext);
                        } catch (Exception ex) {
                            throw new ImportException(ex);
                        }
                    });
        } catch (ImportException ex) {
            throw ex.getCause();
        }
    }

    private static class ImportException extends RuntimeException {

        public ImportException(Exception cause) {
            super(cause);
        }

        @Override
        public synchronized Exception getCause() {
            return (Exception) super.getCause();
        }
    }

    @SuppressWarnings("unchecked")
    protected void importData(InputStream inputStream, DSLContext dslContext) throws Exception {
        PullParser parser = new PullParser(configuration, inputStream, getFeatureQName());
        List<R> records = new ArrayList<>();
        int count = 0;
        Map<String, Object> feature = (Map<String, Object>) parser.parse();
        while (feature != null) {
            records.add(createRecord(feature, dslContext));
            feature = (Map<String, Object>) parser.parse();
            ++count;
            if (count % 100 == 0) {
                runBatch(records, dslContext);
            }
        }
        if (records.size() > 0) {
            runBatch(records, dslContext);
        }
        System.out.printf("Imported %d record(s)%n", count);
    }

    protected abstract QName getFeatureQName();

    protected abstract R createRecord(Map<String, Object> feature, DSLContext dslContext) throws Exception;

    protected static String addressDataToString(Object addressData) {
        if (addressData instanceof Map) {
            return (String) ((Map) addressData).get(null);
        } else if (addressData instanceof String) {
            return (String) addressData;
        } else {
            return "";
        }
    }

    protected Coordinates toWGS84(double x, double y) throws Exception {
        DirectPosition2D source = new DirectPosition2D(x, y);
        DirectPosition2D destination = new DirectPosition2D();
        transform.transform(source, destination);
        return new Coordinates(new BigDecimal(destination.getY()), new BigDecimal(destination.getX()));
    }
}
