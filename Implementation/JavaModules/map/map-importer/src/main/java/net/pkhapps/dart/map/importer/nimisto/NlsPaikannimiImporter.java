package net.pkhapps.dart.map.importer.nimisto;

import com.vividsolutions.jts.geom.Point;
import net.pkhapps.dart.common.Coordinates;
import net.pkhapps.dart.map.database.tables.NlsPaikannimi;
import net.pkhapps.dart.map.database.tables.NlsPaikka;
import net.pkhapps.dart.map.database.tables.records.NlsPaikannimiRecord;
import net.pkhapps.dart.map.database.tables.records.NlsPaikkaRecord;
import net.pkhapps.dart.map.importer.AbstractJooqImporter;
import org.geotools.gml3.ApplicationSchemaXSD;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.PullParser;
import org.geotools.xs.XSConfiguration;
import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import org.opengis.feature.simple.SimpleFeature;

import javax.xml.namespace.QName;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TODO Document me
 */
public class NlsPaikannimiImporter extends AbstractJooqImporter {

    @Override
    protected void importData(DSLContext dslContext) throws Exception {

        // TODO Replace with system properties
        URI dataLocation = new File("/tmp/paikannimet/paikannimi.xml").toURI();
        ApplicationSchemaXSD xsd = new ApplicationSchemaXSD("http://xml.nls.fi/Nimisto/Nimistorekisteri/2009/02",
                "file:/tmp/paikannimet/nimisto.xsd");

        Configuration configuration = new Configuration(xsd) {
            {
                addDependency(new XSConfiguration());
                addDependency(new GMLConfiguration());
            }
        };

        try (InputStream is = dataLocation.toURL().openStream()) {
            PullParser parser = new PullParser(configuration, is, new QName("http://xml.nls.fi/Nimisto/Nimistorekisteri/2009/02", "Paikannimi"));
            Set<Long> usedNameIds = new HashSet<>();
            Set<Long> usedPlaceIds = new HashSet<>();
            List<UpdatableRecord<?>> records = new ArrayList<>();
            long totalNameCount = 0;
            long totalPlaceCount = 0;
            int i = 0;
            SimpleFeature feature = (SimpleFeature) parser.parse();
            while (feature != null) {
                Long nameId = toLong(feature.getAttribute("paikannimiID"));
                Long placeId = toLong(feature.getAttribute("paikannimiID"));

                if (usedPlaceIds.add(placeId)) {
                    NlsPaikkaRecord record = dslContext.newRecord(NlsPaikka.NLS_PAIKKA);
                    // TODO boundedBy
                    record.setId(placeId);
                    record.setNlsPaikkatyyppiId(toLong(feature.getAttribute("paikkatyyppiKoodi")));
                    record.setNlsPaikkatyyppiryhmaId(toLong(feature.getAttribute("paikkatyyppiryhmaKoodi")));
                    record.setNlsPaikkatyyppialaryhmaId(toLong(feature.getAttribute("paikkatyyppialaryhmaKoodi")));
                    Point location = (Point) feature.getAttribute("paikkaSijainti");
                    record.setLocation(new Coordinates(new BigDecimal(location.getY()), new BigDecimal(location.getX())));
                    record.setAltitude(((BigInteger) feature.getAttribute("paikkaKorkeus")).intValue());
                    record.setTm35fin7Code((String) feature.getAttribute("tm35Fin7Koodi"));
                    record.setYlj7Code((String) feature.getAttribute("ylj7Koodi"));
                    record.setPp6Code((String) feature.getAttribute("pp6Koodi"));
                    record.setNlsMunicipalityId(toLong(feature.getAttribute("kuntaKoodi")));
                    record.setNlsSeutukuntaId(toLong(feature.getAttribute("seutukuntaKoodi")));
                    record.setNlsMaakuntaId(toLong(feature.getAttribute("maakuntaKoodi")));
                    record.setNlsSuuralueId(toLong(feature.getAttribute("suuralueKoodi")));
                    // TODO mittakaavarelevanssiKoodi
                    record.setCreated(((Timestamp) feature.getAttribute("paikkaLuontiAika")).toInstant());
                    record.setModified(((Timestamp) feature.getAttribute("paikkaMuutosAika")).toInstant());
                    records.add(record);
                    totalPlaceCount++;
                }

                if (usedNameIds.add(nameId)) {
                    NlsPaikannimiRecord record = dslContext.newRecord(NlsPaikannimi.NLS_PAIKANNIMI);
                    record.setId(nameId);
                    record.setCreated(((Timestamp) feature.getAttribute("paikannimiLuontiAika")).toInstant());
                    record.setModified(((Timestamp) feature.getAttribute("paikannimiMuutosAika")).toInstant());
                    record.setName((String) feature.getAttribute("kirjoitusasu"));
                    record.setNlsKieliId((String) feature.getAttribute("kieliKoodi"));
                    record.setNlsKieliVirallisuusId(toLong(feature.getAttribute("kieliVirallisuusKoodi")));
                    record.setNlsKieliEnemmistoId(toLong(feature.getAttribute("kieliEnemmistoKoodi")));
                    record.setNlsPaikkaId(placeId);
                    // TODO Status, lähde, rinnakkaisnimi
                    records.add(record);
                    totalNameCount++;
                }

                if (i % 100 == 0) {
                    runBatch(records, dslContext);
                }

                feature = (SimpleFeature) parser.parse();
                ++i;
            }
            if (records.size() > 0) {
                runBatch(records, dslContext);
            }
            System.out.println("Inserted a total of " + totalNameCount + " names and " + totalPlaceCount + " places");
        }
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("jdbc.url", "jdbc:postgresql:dart_map");
        System.setProperty("jdbc.user", "dart");
        System.setProperty("jdbc.password", "dart");

        new NlsPaikannimiImporter().importData();
    }
}
