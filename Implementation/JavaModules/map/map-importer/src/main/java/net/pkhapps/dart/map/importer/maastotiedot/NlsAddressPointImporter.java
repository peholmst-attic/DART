package net.pkhapps.dart.map.importer.maastotiedot;

import net.pkhapps.dart.map.database.tables.NlsAddressPoint;
import net.pkhapps.dart.map.database.tables.records.NlsAddressPointRecord;
import net.pkhapps.dart.map.importer.xsdenums.NlsAddressPointClassMapper;
import org.jooq.DSLContext;

import javax.xml.namespace.QName;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import static net.pkhapps.dart.map.importer.CSR.fromTM35FINtoWGS84;

/**
 * TODO Implement me
 */
public class NlsAddressPointImporter extends AbstractNlsMaastotiedotImporter<NlsAddressPointRecord> {

    private final NlsAddressPointClassMapper addressPointClassMapper = new NlsAddressPointClassMapper();

    public NlsAddressPointImporter() throws Exception {
    }

    @Override
    protected QName getFeatureQName() {
        return new QName(NAMESPACE_URI, "Osoitepiste", "");
    }

    @Override
    protected NlsAddressPointRecord createRecord(Map<String, Object> feature, DSLContext dslContext) throws Exception {
        NlsAddressPointRecord record = dslContext.newRecord(NlsAddressPoint.NLS_ADDRESS_POINT);
        record.setGid((Long) feature.get("gid"));
        record.setLocationAccuracy(toInteger(feature.get("sijaintitarkkuus")));
        record.setStartDate((Date) feature.get("alkupvm"));
        record.setEndDate((Date) feature.get("loppupvm"));
        record.setNumber(addressDataToString(feature.get("numero")));
        record.setNameFi(addressDataToString(feature.get("nimi_suomi")));
        record.setNameSv(addressDataToString(feature.get("nimi_ruotsi")));
        record.setMunicipalityId(toLong(feature.get("kuntatunnus")));
        record.setPointClass(addressPointClassMapper.toEnum(toInteger(feature.get("kohdeluokka"))));

        @SuppressWarnings("unchecked")
        List<Double> location = (List<Double>) feature.get("sijainti");
        Long dimension = (Long) feature.get("dimension");
        if (location.size() < 2) {
            throw new IllegalArgumentException("Invalid coordinates: " + location);
        }
        // The coordinates are in ETRS-TM35FIN (EPSG:3067), we want to store them as WGS84
        record.setLocation(fromTM35FINtoWGS84(location.get(0), location.get(1)));

        return record;
    }

    public static void main(String[] args) throws Exception {
        // TODO Remove
        System.setProperty("jdbc.url", "jdbc:postgresql:dart_map");
        System.setProperty("jdbc.user", "dart");
        System.setProperty("jdbc.password", "dart");

        new NlsAddressPointImporter().importData();
    }
}
