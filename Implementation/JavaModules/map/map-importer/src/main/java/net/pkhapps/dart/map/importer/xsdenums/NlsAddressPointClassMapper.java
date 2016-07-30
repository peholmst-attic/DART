package net.pkhapps.dart.map.importer.xsdenums;

import net.pkhapps.dart.map.database.enums.NlsAddressPointClass;

/**
 * TODO Document me!
 */
public class NlsAddressPointClassMapper extends EnumMapper<NlsAddressPointClass> {

    public NlsAddressPointClassMapper() {
        registerEnumConstant(NlsAddressPointClass.address, 96001);
        registerEnumConstant(NlsAddressPointClass.entry_point, 96002);
    }
}
