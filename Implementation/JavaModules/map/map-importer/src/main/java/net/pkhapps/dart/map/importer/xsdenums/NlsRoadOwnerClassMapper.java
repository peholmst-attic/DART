package net.pkhapps.dart.map.importer.xsdenums;

import net.pkhapps.dart.map.database.enums.NlsRoadOwnerClass;

/**
 * TODO Document me!
 */
public class NlsRoadOwnerClassMapper extends EnumMapper<NlsRoadOwnerClass> {

    public NlsRoadOwnerClassMapper() {
        registerEnumConstant(NlsRoadOwnerClass.state, 1);
        registerEnumConstant(NlsRoadOwnerClass.municipality, 2);
        registerEnumConstant(NlsRoadOwnerClass.private_, 3);
    }
}
