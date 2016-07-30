package net.pkhapps.dart.map.importer.xsdenums;

import net.pkhapps.dart.map.database.enums.NlsRoadDirection;

/**
 * TODO Document me!
 */
public class NlsRoadDirectionMapper extends EnumMapper<NlsRoadDirection> {

    public NlsRoadDirectionMapper() {
        registerEnumConstant(NlsRoadDirection.two_way, 0);
        registerEnumConstant(NlsRoadDirection.one_way, 1);
        registerEnumConstant(NlsRoadDirection.one_way_reversed, 2);
    }
}
