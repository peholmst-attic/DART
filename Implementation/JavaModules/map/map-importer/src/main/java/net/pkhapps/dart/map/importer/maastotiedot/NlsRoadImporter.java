package net.pkhapps.dart.map.importer.maastotiedot;

import net.pkhapps.dart.common.Coordinates;
import net.pkhapps.dart.common.CoordinatesList;
import net.pkhapps.dart.map.database.Sequences;
import net.pkhapps.dart.map.database.enums.NlsLanguage;
import net.pkhapps.dart.map.database.tables.NlsRoadSegment;
import net.pkhapps.dart.map.database.tables.records.NlsRoadNameRecord;
import net.pkhapps.dart.map.database.tables.records.NlsRoadRecord;
import net.pkhapps.dart.map.database.tables.records.NlsRoadSegmentRecord;
import net.pkhapps.dart.map.importer.AbstractJooqImporter;
import net.pkhapps.dart.map.importer.xsdenums.NlsRoadDirectionMapper;
import net.pkhapps.dart.map.importer.xsdenums.NlsRoadReadinessMapper;
import net.pkhapps.dart.map.importer.xsdenums.NlsRoadSurfaceMapper;
import net.pkhapps.dart.map.importer.xsdenums.NlsRoadVerticalLevelMapper;
import org.javatuples.Triplet;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import org.jooq.impl.DSL;

import javax.xml.namespace.QName;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static net.pkhapps.dart.map.database.tables.NlsRoad.NLS_ROAD;
import static net.pkhapps.dart.map.database.tables.NlsRoadName.NLS_ROAD_NAME;
import static net.pkhapps.dart.map.importer.CSR.fromTM35FINtoWGS84;

/**
 * TODO Document me!
 */
public class NlsRoadImporter extends AbstractNlsMaastotiedotImporter {

    private final NlsRoadVerticalLevelMapper verticalLevelMapper = new NlsRoadVerticalLevelMapper();
    private final NlsRoadReadinessMapper readinessMapper = new NlsRoadReadinessMapper();
    private final NlsRoadSurfaceMapper surfaceMapper = new NlsRoadSurfaceMapper();
    private final NlsRoadDirectionMapper directionMapper = new NlsRoadDirectionMapper();

    public NlsRoadImporter() throws Exception {
    }

    @Override
    protected QName getFeatureQName() {
        return new QName(NAMESPACE_URI, "Tieviiva", "");
    }

    private final AtomicLong roadId = new AtomicLong();

    @Override
    protected void importData(DSLContext dslContext) throws Exception {
        // Let's see if we can speed things up by using an atomic long to generate new IDs
        // and update the database sequence afterwards as opposed to hitting the database
        // every time.
        roadId.set(dslContext.nextval(Sequences.NLS_ROAD_ID_SEQ));
        try {
            super.importData(dslContext);
        } finally {
            dslContext.alterSequence(Sequences.NLS_ROAD_ID_SEQ).restartWith(roadId.get());
        }
    }

    @Override
    protected List<UpdatableRecord<?>> createRecord(Map<String, Object> feature, DSLContext dslContext) throws Exception {
        List<UpdatableRecord<?>> records = new LinkedList<>();

        String nameFi = addressDataToString(feature.get("nimi_suomi"));
        String nameSv = addressDataToString(feature.get("nimi_ruotsi"));
        Long municipalityId = toLong(feature.get("kuntatunnus"));

        Long roadId = getOrCreateRoadId(nameFi, nameSv, municipalityId, records, dslContext);

        NlsRoadSegmentRecord record = dslContext.newRecord(NlsRoadSegment.NLS_ROAD_SEGMENT);
        record.setRoadId(roadId);
        record.setGid((Long) feature.get("gid"));
        record.setLocationAccuracy(toInteger(feature.get("sijaintitarkkuus")));
        record.setAltitudeAccuracy(toInteger(feature.get("korkeustarkkuus")));
        record.setStartDate((Date) feature.get("alkupvm"));
        record.setEndDate((Date) feature.get("loppupvm"));
        record.setRoadClassId(toLong(feature.get("kohdeluokka")));
        record.setVerticalLocation(verticalLevelMapper.toEnum(toInteger(feature.get("tasosijainti"))));
        record.setReadiness(readinessMapper.toEnum(toInteger(feature.get("valmiusaste"))));
        record.setSurface(surfaceMapper.toEnum(toInteger(feature.get("paallyste"))));
        record.setDirection(directionMapper.toEnum(toInteger(feature.get("yksisuuntaisuus"))));
        record.setNumber(toInteger(feature.get("tienumero")));
        record.setPartNumber(toInteger(feature.get("tieosanumero")));
        record.setMinAddressNumberLeft(toInteger(feature.get("minOsoitenumeroVasen")));
        record.setMaxAddressNumberLeft(toInteger(feature.get("maxOsoitenumeroVasen")));
        record.setMinAddressNumberRight(toInteger(feature.get("minOsoitenumeroOikea")));
        record.setMaxAddressNumberRight(toInteger(feature.get("maxOsoitenumeroOikea")));
        record.setMunicipalityId(municipalityId);

        @SuppressWarnings("unchecked")
        List<Double> location = (List<Double>) feature.get("sijainti");
        Long dimension = (Long) feature.get("dimension");

        List<Coordinates> coordinatesList = new LinkedList<>();
        for (int i = 0; i < location.size(); i += dimension) {
            // The coordinates are in ETRS-TM35FIN (EPSG:3067), we want to store them as WGS84
            coordinatesList.add(fromTM35FINtoWGS84(location.get(i), location.get(i + 1)));
        }
        record.setLocation(new CoordinatesList(coordinatesList));
        records.add(record);

        return records;
    }

    private final Map<Triplet<String, String, Long>, Long> roadNameToIdMap = new ConcurrentHashMap<>();

    private Long getOrCreateRoadId(String nameFi, String nameSv, Long municipalityId,
                                   List<UpdatableRecord<?>> recordsToAdd, DSLContext dslContext) {
        if ((nameFi == null || nameFi.isEmpty()) && (nameSv == null || nameSv.isEmpty())) {
            return null; // No need to create a separate road record since you won't be able to look for it anyway
        }
        Triplet<String, String, Long> mapKey = Triplet.with(nameFi, nameSv, municipalityId);
        Long roadId = roadNameToIdMap.get(mapKey);
        if (roadId == null) {
            // Try to find the record from the database
            roadId = findRoadIdFromDatabase(nameFi, nameSv, municipalityId, dslContext);
            if (roadId == null) {
                // No record? Create it.
                roadId = this.roadId.getAndIncrement();
                NlsRoadRecord record = dslContext.newRecord(NLS_ROAD);
                record.setId(roadId);
                record.setMunicipalityId(municipalityId);
                recordsToAdd.add(record);

                if (nameFi != null && nameFi.length() > 0) {
                    NlsRoadNameRecord nameRecord = dslContext.newRecord(NLS_ROAD_NAME);
                    nameRecord.setRoadId(roadId);
                    nameRecord.setName(nameFi);
                    nameRecord.setLanguage(NlsLanguage.fin);
                    recordsToAdd.add(nameRecord);
                }
                if (nameSv != null && nameSv.length() > 0) {
                    NlsRoadNameRecord nameRecord = dslContext.newRecord(NLS_ROAD_NAME);
                    nameRecord.setRoadId(roadId);
                    nameRecord.setName(nameSv);
                    nameRecord.setLanguage(NlsLanguage.swe);
                    recordsToAdd.add(nameRecord);
                }
            }
            roadNameToIdMap.put(mapKey, roadId);
        }
        return roadId;
    }

    private Long findRoadIdFromDatabase(String nameFi, String nameSv, Long municipalityId, DSLContext dslContext) {
        Condition condition = NLS_ROAD.MUNICIPALITY_ID.eq(municipalityId);

        Set<Condition> nameConditions = new HashSet<>();
        if (nameFi != null && nameFi.length() > 0) {
            nameConditions.add(NLS_ROAD_NAME.NAME.eq(nameFi).and(NLS_ROAD_NAME.LANGUAGE.eq(NlsLanguage.fin)));
        }
        if (nameSv != null && nameSv.length() > 0) {
            nameConditions.add(NLS_ROAD_NAME.NAME.eq(nameSv).and(NLS_ROAD_NAME.LANGUAGE.eq(NlsLanguage.swe)));
        }
        condition = condition.and(DSL.or(nameConditions));

        return dslContext.selectDistinct(NLS_ROAD.ID).from(NLS_ROAD)
                .join(NLS_ROAD_NAME).on(NLS_ROAD_NAME.ROAD_ID.eq(NLS_ROAD.ID))
                .where(condition).limit(1).fetchOne(NLS_ROAD.ID);
    }

    @Override
    protected void runBatch(List<? extends UpdatableRecord<?>> records, DSLContext dslContext, BatchMode mode) {
        super.runBatch(records, dslContext, mode);
        roadNameToIdMap.clear();
    }

    @Override
    protected BatchMode getBatchMode() {
        return GroupAndKeepOrder;
    }

    public static void main(String[] args) throws Exception {
        // TODO Remove
        System.setProperty("jdbc.url", "jdbc:postgresql:dart_map");
        System.setProperty("jdbc.user", "dart");
        System.setProperty("jdbc.password", "dart");

        new NlsRoadImporter().importData();
    }
}
