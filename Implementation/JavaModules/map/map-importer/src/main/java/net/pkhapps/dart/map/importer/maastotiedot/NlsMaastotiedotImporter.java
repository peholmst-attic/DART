package net.pkhapps.dart.map.importer.maastotiedot;

import net.pkhapps.dart.map.importer.AbstractJooqImporter;
import org.geotools.gml3.ApplicationSchemaXSD;
import org.jooq.DSLContext;

/**
 * TODO Document me!
 */
public class NlsMaastotiedotImporter extends AbstractJooqImporter {

    @Override
    protected void importData(DSLContext dslContext) throws Exception {
        System.out.println("Parsing schema");
        ApplicationSchemaXSD xsd = new ApplicationSchemaXSD(
                "http://xml.nls.fi/XML/Namespace/Maastotietojarjestelma/SiirtotiedostonMalli/2011-02",
                "http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Maastotiedot.xsd");

        //System.out.println(xsd.getSchema().);

        /*
        URI dataLocation = new File("/Users/petterprivate/Google Drive/Maps/maastotietokanta_tiesto/L33.xml").toURI();

        Configuration configuration = new Configuration(xsd) {
            {
                addDependency(new XSConfiguration());
                addDependency(new GMLConfiguration());
            }
        };

        System.out.println("Parsing XML");
        try (InputStream is = dataLocation.toURL().openStream()) {
            PullParser parser = new PullParser(configuration, is,
                    new QName("http://xml.nls.fi/XML/Namespace/Maastotietojarjestelma/SiirtotiedostonMalli/2011-02", "Maastotiedot"));
            SimpleFeature feature = (SimpleFeature) parser.parse();
            while (feature != null) {
                System.out.println(feature);
                feature = (SimpleFeature) parser.parse();
            }
        }*/
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("jdbc.url", "jdbc:postgresql:dart_map");
        System.setProperty("jdbc.user", "dart");
        System.setProperty("jdbc.password", "dart");

        new NlsMaastotiedotImporter().importData();
    }
}
