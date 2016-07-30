package net.pkhapps.dart.map.importer.xsdenums;

import net.pkhapps.dart.map.database.enums.NlsRoadVerticalLevel;

/**
 * TODO Document me!
 */
public class NlsRoadVerticalLevelMapper extends EnumMapper<NlsRoadVerticalLevel> {

    public NlsRoadVerticalLevelMapper() {
        registerEnumConstant(NlsRoadVerticalLevel.tunnel, -11);
        registerEnumConstant(NlsRoadVerticalLevel.below_surface, -1);
        registerEnumConstant(NlsRoadVerticalLevel.on_surface, 0);
        registerEnumConstant(NlsRoadVerticalLevel.above_surface_level_1, 1);
        registerEnumConstant(NlsRoadVerticalLevel.above_surface_level_2, 2);
        registerEnumConstant(NlsRoadVerticalLevel.above_surface_level_3, 3);
        registerEnumConstant(NlsRoadVerticalLevel.above_surface_level_4, 4);
        registerEnumConstant(NlsRoadVerticalLevel.above_surface_level_5, 5);
        registerEnumConstant(NlsRoadVerticalLevel.undefined, 10);
    }
}
