package net.pkhapps.dart.common.jooq;

import net.pkhapps.dart.common.Coordinates;
import net.pkhapps.dart.common.CoordinatesList;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.jooq.Converter;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A converter that converts between {@link CoordinatesList} and PostGIS {@code geometry(LineString,4326)}.
 * The converter reads and decodes EWKBHEX from the database and writes byte arrays to the database. It will
 * not work with any other WKB type than LineString and any other SRID than 4326 (WGS 84).
 */
public class CoordinatesListConverter implements Converter<Object, CoordinatesList> {

    @Override
    public CoordinatesList from(Object databaseObject) {
        if (databaseObject != null) {
            try {
                final String ewkbhex = Objects.toString(databaseObject);
                final byte[] ewkb = Hex.decodeHex(ewkbhex.toCharArray());

                final ByteOrder byteOrder = ewkb[0] == 0 ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
                final int wkbType = ByteBuffer.wrap(ewkb, 1, 4).order(byteOrder).getInt();
                final boolean sridPresent = (wkbType & 0x20000000) == 0x20000000;
                final boolean isLineString = (wkbType & 0x2) == 0x2;

                if (!sridPresent) {
                    throw new IllegalArgumentException("SRID is not present");
                }

                if (!isLineString) {
                    throw new IllegalArgumentException("EWKB does not represent a line string");
                }

                final int srid = ByteBuffer.wrap(ewkb, 5, 4).order(byteOrder).getInt();
                if (srid != 4326) {
                    throw new IllegalArgumentException("Expected SRID 4326, got " + srid);
                }

                final int numPoints = ByteBuffer.wrap(ewkb, 9, 4).order(byteOrder).getInt();
                int offset = 13;
                List<Coordinates> coordinatesList = new ArrayList<>();
                for (int i = 0; i < numPoints; ++i) {
                    final double x = ByteBuffer.wrap(ewkb, offset, 8).order(byteOrder).getDouble();
                    final double y = ByteBuffer.wrap(ewkb, offset + 8, 8).order(byteOrder).getDouble();
                    coordinatesList.add(new Coordinates(new BigDecimal(y), new BigDecimal(x)));
                    offset += 16;
                }
                return new CoordinatesList(coordinatesList);
            } catch (DecoderException ex) {
                throw new IllegalArgumentException("Could not decode EWKBHEX data", ex);
            }
        }
        return null;
    }

    @Override
    public Object to(CoordinatesList userObject) {
        if (userObject == null) {
            return null;
        } else {
            final ByteBuffer ewkb = ByteBuffer.allocate(13 + 16 * userObject.size()).order(ByteOrder.LITTLE_ENDIAN);
            ewkb.put((byte) 0x01); // byte order
            ewkb.putInt(0x20000002); // wkbType (SRID and line string)
            ewkb.putInt(4326); // SRID
            ewkb.putInt(userObject.size()); // numPoints
            for (Coordinates c : userObject) {
                ewkb.putDouble(c.getLongitude().doubleValue()); // x
                ewkb.putDouble(c.getLatitude().doubleValue()); // y
            }
            return ewkb.array();
        }
    }

    @Override
    public Class<Object> fromType() {
        return Object.class;
    }

    @Override
    public Class<CoordinatesList> toType() {
        return CoordinatesList.class;
    }
}
