package net.pkhapps.dart.map.importer.maastotiedot;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import net.pkhapps.dart.common.Coordinates;
import net.pkhapps.dart.common.CoordinatesList;
import net.pkhapps.dart.map.database.tables.NlsRoad;
import net.pkhapps.dart.map.database.tables.records.NlsRoadRecord;
import net.pkhapps.dart.map.importer.xsdenums.*;
import org.jooq.DSLContext;
import org.opengis.feature.simple.SimpleFeature;

import javax.xml.namespace.QName;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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
    protected NlsRoadRecord createRecord(SimpleFeature feature, DSLContext dslContext) throws Exception {
        NlsRoadRecord record = dslContext.newRecord(NlsRoad.NLS_ROAD);
        record.setLocationAccuracy(attributeValueToInteger(feature.getAttribute("sijaintitarkkuus")));
        record.setAltitudeAccuracy(attributeValueToInteger(feature.getAttribute("korkeustarkkuus")));
        record.setStartDate((Date) feature.getAttribute("alkupvm"));
        record.setEndDate((Date) feature.getAttribute("loppupvm"));

        LineString location = (LineString) feature.getAttribute("sijainti");
        List<Coordinates> coordinatesList = new ArrayList<>(location.getNumPoints());
        for (Coordinate c : location.getCoordinates()) {
            // The coordinates are in ETRS-TM35FIN (EPSG:3067), we want to store them as WGS84
            coordinatesList.add(toWGS84(c.x, c.y));
        }
        record.setLocation(new CoordinatesList(coordinatesList));

        record.setRoadClassId(attributeValueToLong(feature.getAttribute("kohdeluokka")));
        record.setVerticalLocation(verticalLevelMapper.toEnum(attributeValueToInteger(feature.getAttribute("tasosijainti"))));
        record.setReadiness(readinessMapper.toEnum(attributeValueToInteger(feature.getAttribute("valmiusaste"))));
        record.setSurface(surfaceMapper.toEnum(attributeValueToInteger(feature.getAttribute("paallyste"))));
        record.setDirection(directionMapper.toEnum(attributeValueToInteger(feature.getAttribute("yksisuuntaisuus"))));
        record.setNumber(attributeValueToInteger(feature.getAttribute("tienumero")));
        record.setPartNumber(attributeValueToInteger(feature.getAttribute("tieosanumero")));
        record.setMinAddressNumberLeft(attributeValueToInteger(feature.getAttribute("minOsoitenumeroVasen")));
        record.setMaxAddressNumberLeft(attributeValueToInteger(feature.getAttribute("maxOsoitenumeroVasen")));
        record.setMinAddressNumberRight(attributeValueToInteger(feature.getAttribute("minOsoitenumeroOikea")));
        record.setMaxAddressNumberRight(attributeValueToInteger(feature.getAttribute("maxOsoitenumeroOikea")));
        record.setNameFi(addressDataToString(feature.getAttribute("nimi_suomi")));
        record.setNameSv(addressDataToString(feature.getAttribute("nimi_ruotsi")));
        record.setMunicipalityId(attributeValueToLong(feature.getAttribute("kuntatunnus")));
        return record;
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("jdbc.url", "jdbc:postgresql:dart_map");
        System.setProperty("jdbc.user", "dart");
        System.setProperty("jdbc.password", "dart");

        new NlsRoadImporter().importData();
    }
}
