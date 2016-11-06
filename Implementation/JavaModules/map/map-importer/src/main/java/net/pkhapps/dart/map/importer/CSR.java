package net.pkhapps.dart.map.importer;

import net.pkhapps.dart.common.Coordinates;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import java.math.BigDecimal;

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
            ETRS_TM35FIN = CRS.parseWKT("PROJCS[\"ETRS89 / TM35FIN(E,N)\",\n" +
                    "    GEOGCS[\"ETRS89\",\n" +
                    "        DATUM[\"European_Terrestrial_Reference_System_1989\",\n" +
                    "            SPHEROID[\"GRS 1980\",6378137,298.257222101,\n" +
                    "                AUTHORITY[\"EPSG\",\"7019\"]],\n" +
                    "            TOWGS84[0,0,0,0,0,0,0],\n" +
                    "            AUTHORITY[\"EPSG\",\"6258\"]],\n" +
                    "        PRIMEM[\"Greenwich\",0,\n" +
                    "            AUTHORITY[\"EPSG\",\"8901\"]],\n" +
                    "        UNIT[\"degree\",0.0174532925199433,\n" +
                    "            AUTHORITY[\"EPSG\",\"9122\"]],\n" +
                    "        AUTHORITY[\"EPSG\",\"4258\"]],\n" +
                    "    PROJECTION[\"Transverse_Mercator\"],\n" +
                    "    PARAMETER[\"latitude_of_origin\",0],\n" +
                    "    PARAMETER[\"central_meridian\",27],\n" +
                    "    PARAMETER[\"scale_factor\",0.9996],\n" +
                    "    PARAMETER[\"false_easting\",500000],\n" +
                    "    PARAMETER[\"false_northing\",0],\n" +
                    "    UNIT[\"metre\",1,\n" +
                    "        AUTHORITY[\"EPSG\",\"9001\"]],\n" +
                    "    AXIS[\"Easting\",EAST],\n" +
                    "    AXIS[\"Northing\",NORTH],\n" +
                    "    AUTHORITY[\"EPSG\",\"3067\"]]");
            WGS84 = CRS.parseWKT("GEOGCS[\"WGS 84\",\n" +
                    "    DATUM[\"WGS_1984\",\n" +
                    "        SPHEROID[\"WGS 84\",6378137,298.257223563,\n" +
                    "            AUTHORITY[\"EPSG\",\"7030\"]],\n" +
                    "        AUTHORITY[\"EPSG\",\"6326\"]],\n" +
                    "    PRIMEM[\"Greenwich\",0,\n" +
                    "        AUTHORITY[\"EPSG\",\"8901\"]],\n" +
                    "    UNIT[\"degree\",0.0174532925199433,\n" +
                    "        AUTHORITY[\"EPSG\",\"9122\"]],\n" +
                    "    AUTHORITY[\"EPSG\",\"4326\"]]");
            transform = CRS.findMathTransform(ETRS_TM35FIN, WGS84, true);
        } catch (Exception ex) {
            throw new InternalError("Could not create CoordinateReferenceSystems", ex);
        }
    }

    /**
     * TODO Document me!
     * @param x
     * @param y
     * @return
     * @throws Exception
     */
    public static Coordinates fromTM35FINtoWGS84(double x, double y) throws Exception {
        DirectPosition2D source = new DirectPosition2D(x, y);
        DirectPosition2D destination = new DirectPosition2D();
        CSR.transform.transform(source, destination);
        return new Coordinates(new BigDecimal(destination.getY()), new BigDecimal(destination.getX()));
    }

    private CSR() {
    }
}
