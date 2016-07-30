package net.pkhapps.dart.map.importer.xsdenums;

import net.pkhapps.dart.map.database.enums.NlsRoadSurface;

/**
 * TODO Document me!
 */
public class NlsRoadSurfaceMapper extends EnumMapper<NlsRoadSurface> {

    public NlsRoadSurfaceMapper() {
        registerEnumConstant(NlsRoadSurface.unknown, 0);
        registerEnumConstant(NlsRoadSurface.none, 1);
        registerEnumConstant(NlsRoadSurface.durable, 2);
    }
}
