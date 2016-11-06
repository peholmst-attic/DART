package net.pkhapps.dart.map.importer.xsdenums;

import net.pkhapps.dart.map.database.enums.NlsPlaceGroup;

/**
 * TODO Document me!
 */
public class NlsPlaceGroupMapper extends EnumMapper<NlsPlaceGroup, Integer> {

    public NlsPlaceGroupMapper() {
        registerEnumConstant(NlsPlaceGroup.natural, 1);
        registerEnumConstant(NlsPlaceGroup.cultural, 2);
    }
}
