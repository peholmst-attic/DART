package net.pkhapps.dart.map.importer.maastotiedot;

import net.pkhapps.dart.map.database.Sequences;
import net.pkhapps.dart.map.database.enums.NlsLanguage;
import net.pkhapps.dart.map.database.tables.NlsAddressPoint;
import net.pkhapps.dart.map.database.tables.NlsAddressPointName;
import net.pkhapps.dart.map.database.tables.records.NlsAddressPointNameRecord;
import net.pkhapps.dart.map.database.tables.records.NlsAddressPointRecord;
import net.pkhapps.dart.map.importer.xsdenums.NlsAddressPointClassMapper;
import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;

import javax.xml.namespace.QName;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static net.pkhapps.dart.map.importer.CSR.fromTM35FINtoWGS84;

/**
 * TODO Implement me
 */
public class NlsAddressPointImporter extends AbstractNlsMaastotiedotImporter {

    private final NlsAddressPointClassMapper addressPointClassMapper = new NlsAddressPointClassMapper();

    public NlsAddressPointImporter() throws Exception {
    }

    @Override
    protected QName getFeatureQName() {
        return new QName(NAMESPACE_URI, "Osoitepiste", "");
    }

    @Override
    protected List<UpdatableRecord<?>> createRecord(Map<String, Object> feature, DSLContext dslContext) throws Exception {
        List<UpdatableRecord<?>> records = new LinkedList<>();

        NlsAddressPointRecord record = dslContext.newRecord(NlsAddressPoint.NLS_ADDRESS_POINT);
        record.setId(dslContext.nextval(Sequences.NLS_ADDRESS_POINT_ID_SEQ));
        record.setGid((Long) feature.get("gid"));
        record.setLocationAccuracy(toInteger(feature.get("sijaintitarkkuus")));
        record.setStartDate((Date) feature.get("alkupvm"));
        record.setEndDate((Date) feature.get("loppupvm"));
        record.setNumber(addressDataToString(feature.get("numero")));
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
        records.add(record);

        String nameFi = addressDataToString(feature.get("nimi_suomi"));
        String nameSv = addressDataToString(feature.get("nimi_ruotsi"));

        if (nameFi != null && nameFi.length() > 0) {
            NlsAddressPointNameRecord nameRecord = dslContext.newRecord(NlsAddressPointName.NLS_ADDRESS_POINT_NAME);
            nameRecord.setAddressPointId(record.getId());
            nameRecord.setName(nameFi);
            nameRecord.setLanguage(NlsLanguage.fin);
            records.add(nameRecord);
        }
        if (nameSv != null && nameSv.length() > 0) {
            NlsAddressPointNameRecord nameRecord = dslContext.newRecord(NlsAddressPointName.NLS_ADDRESS_POINT_NAME);
            nameRecord.setAddressPointId(record.getId());
            nameRecord.setName(nameSv);
            nameRecord.setLanguage(NlsLanguage.swe);
            records.add(nameRecord);
        }
        return records;
    }

    @Override
    protected BatchMode getBatchMode() {
        return GroupAndReorder;
    }

    public static void main(String[] args) throws Exception {
        // TODO Remove
        System.setProperty("jdbc.url", "jdbc:postgresql:dart_map");
        System.setProperty("jdbc.user", "dart");
        System.setProperty("jdbc.password", "dart");

        new NlsAddressPointImporter().importData();
    }
}
