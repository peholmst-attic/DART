package net.pkhapps.dart.map.importer.xsdenums;

import net.pkhapps.dart.map.database.enums.NlsRoadReadiness;

/**
 * TODO Document me!
 */
public class NlsRoadReadinessMapper extends EnumMapper<NlsRoadReadiness> {

    public NlsRoadReadinessMapper() {
        registerEnumConstant(NlsRoadReadiness.in_use, 0);
        registerEnumConstant(NlsRoadReadiness.under_construction, 1);
        registerEnumConstant(NlsRoadReadiness.in_planning, 3);
    }
}
