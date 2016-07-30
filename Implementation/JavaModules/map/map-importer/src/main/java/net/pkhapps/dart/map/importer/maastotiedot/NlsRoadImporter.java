package net.pkhapps.dart.map.importer.maastotiedot;

import net.pkhapps.dart.map.database.tables.NlsRoad;
import net.pkhapps.dart.map.database.tables.records.NlsRoadRecord;
import net.pkhapps.dart.map.importer.xsdenums.NlsRoadDirectionMapper;
import net.pkhapps.dart.map.importer.xsdenums.NlsRoadReadinessMapper;
import net.pkhapps.dart.map.importer.xsdenums.NlsRoadSurfaceMapper;
import net.pkhapps.dart.map.importer.xsdenums.NlsRoadVerticalLevelMapper;
import org.jooq.DSLContext;
import org.opengis.feature.simple.SimpleFeature;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.sql.Date;

/**
 * TODO Document me!
 */
public class NlsRoadImporter extends AbstractNlsMaastotiedotImporter<NlsRoadRecord> {

    private final NlsRoadVerticalLevelMapper verticalLevelMapper = new NlsRoadVerticalLevelMapper();
    private final NlsRoadReadinessMapper readinessMapper = new NlsRoadReadinessMapper();
    private final NlsRoadSurfaceMapper surfaceMapper = new NlsRoadSurfaceMapper();
    private final NlsRoadDirectionMapper directionMapper = new NlsRoadDirectionMapper();

    public NlsRoadImporter() throws IOException {
    }

    @Override
    protected QName getFeatureQName() {
        return new QName(NAMESPACE_URI, "Tieviiva", "");
    }

    @Override
    protected NlsRoadRecord createRecord(SimpleFeature feature, DSLContext dslContext) {
        NlsRoadRecord record = dslContext.newRecord(NlsRoad.NLS_ROAD);
        // TODO GID
        record.setLocationAccuracy(attributeValueToInteger(feature.getAttribute("sijaintitarkkuus")));
        record.setAltitudeAccuracy(attributeValueToInteger(feature.getAttribute("korkeustarkkuus")));
        record.setStartDate((Date) feature.getAttribute("alkupvm"));
        record.setEndDate((Date) feature.getAttribute("loppupvm"));
        // TODO Location
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
        record.setNameFi((String) feature.getAttribute("nimi_suomi"));
        record.setNameSv((String) feature.getAttribute("nimi_ruotsi"));
        record.setMunicipalityId(attributeValueToLong(feature.getAttribute("kuntatunnus")));
        /*
			<sijainti>
				<Murtoviiva>
					<gml:posList srsDimension="3">290150.120 6713782.733 71.474 290158.592 6713794.035 71.839 290183.029 6713828.221 73.132 290215.379 6713873.777 73.969 290247.590 6713919.192 73.862 290260.222 6713937.160 73.518 290269.592 6713951.126 73.274 290278.661 6713968.587 72.953 290291.036 6713995.857 72.104 290292.582 6714000.000 72.009</gml:posList>
				</Murtoviiva>
			</sijainti>
         */
        return record;
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("jdbc.url", "jdbc:postgresql:dart_map");
        System.setProperty("jdbc.user", "dart");
        System.setProperty("jdbc.password", "dart");

        new NlsRoadImporter().importData();
    }
}
