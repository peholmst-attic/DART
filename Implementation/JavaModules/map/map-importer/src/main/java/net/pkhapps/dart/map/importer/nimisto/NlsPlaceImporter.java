package net.pkhapps.dart.map.importer.nimisto;

import com.vividsolutions.jts.geom.Point;
import net.pkhapps.dart.map.database.tables.NlsPlace;
import net.pkhapps.dart.map.database.tables.NlsPlaceName;
import net.pkhapps.dart.map.database.tables.records.NlsPlaceNameRecord;
import net.pkhapps.dart.map.database.tables.records.NlsPlaceRecord;
import net.pkhapps.dart.map.importer.AbstractJooqImporter;
import net.pkhapps.dart.map.importer.CSR;
import net.pkhapps.dart.map.importer.xsdenums.NlsPlaceGroupMapper;
import net.pkhapps.dart.map.importer.xsdenums.NlsLanguageMapper;
import net.pkhapps.dart.map.importer.xsdenums.NlsPlaceSubgroupMapper;
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
import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.pkhapps.dart.map.database.tables.NlsMunicipality.NLS_MUNICIPALITY;

/**
 * TODO Document me
 */
public class NlsPlaceImporter extends AbstractJooqImporter {

    private final NlsPlaceGroupMapper groupMapper = new NlsPlaceGroupMapper();
    private final NlsPlaceSubgroupMapper subgroupMapper = new NlsPlaceSubgroupMapper();
    private final NlsLanguageMapper nameLanguageMapper = new NlsLanguageMapper();

    @Override
    protected void importData(DSLContext dslContext) throws Exception {

        // TODO Replace with system properties
        URI dataLocation = new File("/tmp/paikannimi.xml").toURI();
        ApplicationSchemaXSD xsd = new ApplicationSchemaXSD("http://xml.nls.fi/Nimisto/Nimistorekisteri/2009/02",
                "http://xml.nls.fi/Nimisto/Nimistorekisteri/nimisto.xsd");

        Configuration configuration = new Configuration(xsd) {
            {
                addDependency(new XSConfiguration());
                addDependency(new GMLConfiguration());
            }
        };

        Set<Long> validMunicipalityIds = new HashSet<>(dslContext.select(NLS_MUNICIPALITY.ID)
                .from(NLS_MUNICIPALITY)
                .fetch(NLS_MUNICIPALITY.ID));

        try (InputStream is = dataLocation.toURL().openStream()) {
            PullParser parser = new PullParser(configuration, is, new QName("http://xml.nls.fi/Nimisto/Nimistorekisteri/2009/02", "Paikannimi"));
            Set<Long> usedNameIds = new HashSet<>();
            Set<Long> usedPlaceIds = new HashSet<>();
            List<UpdatableRecord<?>> placeRecords = new ArrayList<>();
            List<UpdatableRecord<?>> nameRecords = new ArrayList<>();
            long totalNameCount = 0;
            long totalPlaceCount = 0;
            long totalIgnoredCount = 0;
            int i = 0;
            SimpleFeature feature = (SimpleFeature) parser.parse();
            while (feature != null) {
                Long nameId = toLong(feature.getAttribute("paikannimiID"));
                Long placeId = toLong(feature.getAttribute("paikkaID"));
                Long municipalityId = toLong(feature.getAttribute("kuntaKoodi"));

                if (!validMunicipalityIds.contains(municipalityId)) {
                    // Invalid municipality ID, silently ignore
                    totalIgnoredCount++;
                } else {
                    if (usedPlaceIds.add(placeId)) {
                        NlsPlaceRecord record = dslContext.newRecord(NlsPlace.NLS_PLACE);
                        record.setId(placeId);
                        record.setPlaceTypeId(toLong(feature.getAttribute("paikkatyyppiKoodi")));
                        record.setPlaceGroup(groupMapper.toEnum(toInteger(feature.getAttribute("paikkatyyppiryhmaKoodi"))));
                        record.setPlaceSubgroup(subgroupMapper.toEnum(toInteger(feature.getAttribute("paikkatyyppialaryhmaKoodi"))));

                        Point location = (Point) feature.getAttribute("paikkaSijainti");
                        record.setLocation(CSR.fromTM35FINtoWGS84(location.getX(), location.getY()));

                        record.setAltitude(toInteger(feature.getAttribute("paikkaKorkeus")));
                        record.setMunicipalityId(municipalityId);
                        record.setCreated(((Timestamp) feature.getAttribute("paikkaLuontiAika")).toInstant());
                        record.setModified(((Timestamp) feature.getAttribute("paikkaMuutosAika")).toInstant());
                        placeRecords.add(record);
                        totalPlaceCount++;
                    }

                    if (usedNameIds.add(nameId)) {
                        NlsPlaceNameRecord record = dslContext.newRecord(NlsPlaceName.NLS_PLACE_NAME);
                        record.setId(nameId);
                        record.setPlaceId(placeId);
                        record.setCreated(((Timestamp) feature.getAttribute("paikannimiLuontiAika")).toInstant());
                        record.setModified(((Timestamp) feature.getAttribute("paikannimiMuutosAika")).toInstant());
                        record.setName((String) feature.getAttribute("kirjoitusasu"));
                        record.setLanguage(nameLanguageMapper.toEnum((String) feature.getAttribute("kieliKoodi")));
                        nameRecords.add(record);
                        totalNameCount++;
                    }
                }

                if (i % 100 == 0) {
                    runBatch(placeRecords, dslContext, NoGrouping);
                    runBatch(nameRecords, dslContext, NoGrouping);
                }

                feature = (SimpleFeature) parser.parse();
                ++i;
            }
            if (placeRecords.size() > 0) {
                runBatch(placeRecords, dslContext, NoGrouping);
            }
            if (nameRecords.size() > 0) {
                runBatch(nameRecords, dslContext, NoGrouping);
            }
            System.out.println("Inserted a total of " + totalNameCount + " names and " + totalPlaceCount
                    + " places, ignored " + totalIgnoredCount + " place entries");
        }
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("jdbc.url", "jdbc:postgresql:dart_map");
        System.setProperty("jdbc.user", "dart");
        System.setProperty("jdbc.password", "dart");

        new NlsPlaceImporter().importData();
    }
}
