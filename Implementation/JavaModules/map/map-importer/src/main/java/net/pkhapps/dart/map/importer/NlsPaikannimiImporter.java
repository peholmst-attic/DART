package net.pkhapps.dart.map.importer;

import org.geotools.GML;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.jooq.DSLContext;
import org.opengis.feature.simple.SimpleFeature;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

/**
 * TODO Document me
 */
public class NlsPaikannimiImporter extends AbstractJooqImporter {

    @Override
    void importData(DSLContext dslContext) throws Exception {

        URI dataLocation = new File("/tmp/paikannimet/paikannimi.xml").toURI();

        GML gml = new GML(GML.Version.GML3);
        gml.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);

        try (InputStream is = dataLocation.toURL().openStream()) {
            try (SimpleFeatureIterator it = gml.decodeFeatureIterator(is)) {
                int i = 0;
                while (it.hasNext() && i < 10) {
                    SimpleFeature feature = it.next();
                    System.out.println(feature.getName());
                    System.out.println("ID:" + feature.getID());
                    System.out.println(feature.getBounds());
                    System.out.println(feature.getAttribute("paikkaID"));
                    System.out.println(feature.getProperty("paikkatyyppiKoodi"));
                    System.out.println(feature.getProperty("paikkatyyppiryhmaKoodi"));
                    System.out.println(feature.getProperty("paikkatyyppialaryhmaKoodi"));
                    System.out.println(feature.getAttribute("paikkaSijainti"));
                    System.out.println(feature.getAttribute("paikkaKorkeus"));
                    System.out.println(feature.getAttribute("tm35Fin7Koodi"));
                    System.out.println(feature.getAttribute("ylj7Koodi"));
                    System.out.println(feature.getAttribute("pp6Koodi"));
                    System.out.println(feature.getProperty("kuntaKoodi"));
                    System.out.println(feature.getProperty("seutukuntaKoodi"));
                    System.out.println(feature.getProperty("maakuntaKoodi"));
                    System.out.println(feature.getProperty("suuralueKoodi"));
                    System.out.println(feature.getProperty("mittakaavarelevanssiKoodi"));
                    System.out.println(feature.getAttribute("paikkaLuontiAika"));
                    System.out.println(feature.getAttribute("paikkaMuutosAika"));
                    System.out.println(feature.getAttribute("paikannimiID"));
                    System.out.println(feature.getAttribute("kirjoitusasu"));
                    System.out.println(feature.getProperty("kieliKoodi"));
                    System.out.println(feature.getProperty("kieliVirallisuusKoodi"));
                    System.out.println(feature.getProperty("kieliEnemmistoKoodi"));
                    System.out.println(feature.getProperty("paikannimiLahdeKoodi"));
                    System.out.println(feature.getProperty("paikannimiStatusKoodi"));
                    System.out.println(feature.getAttribute("paikannimiLuontiAika"));
                    System.out.println(feature.getAttribute("paikannimiMuutosAika"));

                    System.out.println("--");
                    ++i;
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("jdbc.url", "jdbc:postgresql:dart_map");
        System.setProperty("jdbc.user", "dart");
        System.setProperty("jdbc.password", "dart");

        new NlsPaikannimiImporter().importData();
    }

}
