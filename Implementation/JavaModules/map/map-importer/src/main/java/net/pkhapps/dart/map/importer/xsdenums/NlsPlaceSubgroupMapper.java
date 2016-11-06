package net.pkhapps.dart.map.importer.xsdenums;

import net.pkhapps.dart.map.database.enums.NlsPlaceSubgroup;

/**
 * TODO document me
 */
public class NlsPlaceSubgroupMapper extends EnumMapper<NlsPlaceSubgroup, Integer> {

    public NlsPlaceSubgroupMapper() {
        registerEnumConstant(NlsPlaceSubgroup.terrain, 11);
        registerEnumConstant(NlsPlaceSubgroup.hydrographic, 12);
        registerEnumConstant(NlsPlaceSubgroup.administrative, 21);
        registerEnumConstant(NlsPlaceSubgroup.other, 22);
    }
}
