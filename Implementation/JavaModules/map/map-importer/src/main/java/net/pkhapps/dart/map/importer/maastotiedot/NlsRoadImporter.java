package net.pkhapps.dart.map.importer.maastotiedot;

import net.pkhapps.dart.common.Coordinates;
import net.pkhapps.dart.common.CoordinatesList;
import net.pkhapps.dart.map.database.tables.NlsRoad;
import net.pkhapps.dart.map.database.tables.records.NlsRoadRecord;
import net.pkhapps.dart.map.importer.xsdenums.NlsRoadDirectionMapper;
import net.pkhapps.dart.map.importer.xsdenums.NlsRoadReadinessMapper;
import net.pkhapps.dart.map.importer.xsdenums.NlsRoadSurfaceMapper;
import net.pkhapps.dart.map.importer.xsdenums.NlsRoadVerticalLevelMapper;
import org.jooq.DSLContext;

import javax.xml.namespace.QName;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * TODO Document me!
 */
public class NlsRoadImporter extends AbstractNlsMaastotiedotImporter<NlsRoadRecord> {

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

    @Override
    protected NlsRoadRecord createRecord(Map<String, Object> feature, DSLContext dslContext) throws Exception {
        NlsRoadRecord record = dslContext.newRecord(NlsRoad.NLS_ROAD);
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
        record.setNameFi(addressDataToString(feature.get("nimi_suomi")));
        record.setNameSv(addressDataToString(feature.get("nimi_ruotsi")));
        record.setMunicipalityId(toLong(feature.get("kuntatunnus")));


        @SuppressWarnings("unchecked")
        List<Double> location = (List<Double>) feature.get("sijainti");
        Long dimension = (Long) feature.get("dimension");

        List<Coordinates> coordinatesList = new LinkedList<>();
        for (int i = 0; i < location.size(); i += dimension) {
            // The coordinates are in ETRS-TM35FIN (EPSG:3067), we want to store them as WGS84
            coordinatesList.add(fromTM35FINtoWGS84(location.get(i), location.get(i + 1)));
        }
        record.setLocation(new CoordinatesList(coordinatesList));

        return record;
    }

    public static void main(String[] args) throws Exception {
        // TODO Remove
        System.setProperty("jdbc.url", "jdbc:postgresql:dart_map");
        System.setProperty("jdbc.user", "dart");
        System.setProperty("jdbc.password", "dart");

        new NlsRoadImporter().importData();
    }
}
