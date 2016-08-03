package net.pkhapps.dart.map.importer.maastotiedot;

import com.vividsolutions.jts.geom.Point;
import net.pkhapps.dart.common.Coordinates;
import net.pkhapps.dart.map.database.tables.NlsAddressPoint;
import net.pkhapps.dart.map.database.tables.records.NlsAddressPointRecord;
import net.pkhapps.dart.map.importer.AbstractJooqImporter;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.wfs.v2_0.WFSConfiguration;
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
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO Document me!
 */
public class NlsMaastotiedotImporter extends AbstractJooqImporter {

    @Override
    protected void importData(DSLContext dslContext) throws Exception {
        String namespaceURI = "http://xml.nls.fi/XML/Namespace/Maastotietojarjestelma/SiirtotiedostonMalli/2011-02";
        File dataFile = new File("/Users/petterprivate/Google Drive/Maps/maastotietokanta_tiesto/K32.xml");

        SchemaCache schemaCache = new SchemaCache(new File("/tmp/"), true);
        SchemaResolver resolver = new SchemaResolver(schemaCache);
        AppSchemaConfiguration configuration = new AppSchemaConfiguration(namespaceURI, "http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Maastotiedot.xsd", resolver);
        configuration.addDependency(new GMLConfiguration());
        configuration.addDependency(new WFSConfiguration());


        try (InputStream is = new FileInputStream(dataFile)) {
            PullParser parser = new PullParser(configuration, is,
                    new QName(
                            "http://xml.nls.fi/XML/Namespace/Maastotietojarjestelma/SiirtotiedostonMalli/2011-02",
                            "Osoitepiste", ""));
            List<UpdatableRecord<?>> records = new ArrayList<>();
            int i = 0;
            System.out.println(parser.parse());
            /*SimpleFeature feature = (SimpleFeature) parser.parse();
            while (feature != null) {
                System.out.println(feature.getFeatureType().getAttributeDescriptors());
                NlsAddressPointRecord record = dslContext.newRecord(NlsAddressPoint.NLS_ADDRESS_POINT);
                //record.setId(feature.getID());
                record.setLocationAccuracy(toInteger(feature.getAttribute("sijaintitarkkuus")));
                record.setStartDate((Date) feature.getAttribute("alkupvm"));
                record.setEndDate((Date) feature.getAttribute("loppupvm"));
                Point location = (Point) feature.getAttribute("sijainti");
                record.setLocation(new Coordinates(new BigDecimal(location.getY()), new BigDecimal(location.getX())));
                //record.setSymbolClass(toInteger(feature.getAttribute("kohdeluokka")));
                record.setNumber(addressDataToString(feature.getAttribute("numero")));
                record.setNameFi(addressDataToString(feature.getAttribute("nimi_suomi")));
                record.setNameSv(addressDataToString(feature.getAttribute("nimi_ruotsi")));
                record.setMunicipalityId(toLong(feature.getAttribute("kuntatunnus")));
                records.add(record);

                feature = (SimpleFeature) parser.parse();
                ++i;

                if (i % 100 == 0) {
                 //   runBatch(records, dslContext);
                }
                break;
            }
            if (records.size() > 0) {
                //runBatch(records, dslContext);
            }*/
        }
    }

    private static String addressDataToString(Object addressData) {
        if (addressData instanceof Map) {
            return (String) ((Map) addressData).get(null);
        } else if (addressData instanceof String) {
            return (String) addressData;
        } else {
            return "";
        }
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("jdbc.url", "jdbc:postgresql:dart_map");
        System.setProperty("jdbc.user", "dart");
        System.setProperty("jdbc.password", "dart");

        new NlsMaastotiedotImporter().importData();
    }
}
