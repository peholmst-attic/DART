package net.pkhapps.dart.map.importer;

import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * TODO Document me!
 */
public final class CSR {
    /**
     * The ETRS-TM35FIN coordinate reference system. All the material from the NLS use it.
     */
    public static final CoordinateReferenceSystem ETRS_TM35FIN;
    /**
     * The WGS84 coordinate reference system. This is used by DART everywhere.
     */
    public static final CoordinateReferenceSystem WGS84;
    /**
     * Transform for transforming from the ETRS-TM35FIN coordinate reference system to WGS84.
     */
    public static final MathTransform transform;

    static {
        try {
            ETRS_TM35FIN = CRS.parseWKT("PROJCS[\"ETRS89 / ETRS-TM35FIN\",GEOGCS[\"ETRS89\",DATUM[\"European_Terrestrial_Reference_System_1989\",SPHEROID[\"GRS 1980\",6378137,298.257222101,AUTHORITY[\"EPSG\",\"7019\"]],AUTHORITY[\"EPSG\",\"6258\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.01745329251994328,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4258\"]],UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],PROJECTION[\"Transverse_Mercator\"],PARAMETER[\"latitude_of_origin\",0],PARAMETER[\"central_meridian\",27],PARAMETER[\"scale_factor\",0.9996],PARAMETER[\"false_easting\",500000],PARAMETER[\"false_northing\",0],AUTHORITY[\"EPSG\",\"3067\"],AXIS[\"Easting\",EAST],AXIS[\"Northing\",NORTH]]");
            WGS84 = CRS.parseWKT("GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.01745329251994328,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]]");
            transform = CRS.findMathTransform(ETRS_TM35FIN, WGS84, true);
        } catch (Exception ex) {
            throw new InternalError("Could not create CoordinateReferenceSystems", ex);
        }
    }

    private CSR() {
    }
}
