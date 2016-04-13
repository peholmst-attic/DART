package net.pkhapps.dart.types;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.jooq.Converter;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

/**
 * A converter that converts between {@link Coordinates} and PostGIS {@code geometry(Point,4326)}.
 * The converter reads and decodes EWKBHEX from the database and writes byte arrays to the database. It will
 * not work with any other WKB type than Point and any other SRID than 4326 (WGS 84).
 */
public class CoordinatesConverter implements Converter<Object, Coordinates> {

    @Override
    public Coordinates from(Object databaseObject) {
        if (databaseObject != null) {
            try {
                final String ewkbhex = Objects.toString(databaseObject);
                final byte[] ewkb = Hex.decodeHex(ewkbhex.toCharArray());

                final ByteOrder byteOrder = ewkb[0] == 0 ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
                final int wkbType = ByteBuffer.wrap(ewkb, 1, 4).order(byteOrder).getInt();
                final boolean sridPresent = (wkbType & 0x20000000) == 0x20000000;
                final boolean isPoint = (wkbType & 0x1) == 0x1;

                if (!sridPresent) {
                    throw new IllegalArgumentException("SRID is not present");
                }

                if (!isPoint) {
                    throw new IllegalArgumentException("EWKB does not represent a point");
                }

                final int srid = ByteBuffer.wrap(ewkb, 5, 4).order(byteOrder).getInt();
                if (srid != 4326) {
                    throw new IllegalArgumentException("Expected SRID 4326, got " + srid);
                }

                final double x = ByteBuffer.wrap(ewkb, 9, 8).order(byteOrder).getDouble();
                final double y = ByteBuffer.wrap(ewkb, 17, 8).order(byteOrder).getDouble();
                return new Coordinates(new BigDecimal(y), new BigDecimal(x));
            } catch (DecoderException ex) {
                throw new IllegalArgumentException("Could not decode EWKBHEX data", ex);
            }
        }
        return null;
    }

    @Override
    public Object to(Coordinates userObject) {
        if (userObject == null) {
            return null;
        } else {
            final ByteBuffer ewkb = ByteBuffer.allocate(25).order(ByteOrder.LITTLE_ENDIAN);
            ewkb.put((byte) 0x01); // byte order
            ewkb.putInt(0x20000001); // wkbType (SRID and point)
            ewkb.putInt(4326); // SRID
            ewkb.putDouble(userObject.getLongitude().doubleValue()); // x
            ewkb.putDouble(userObject.getLatitude().doubleValue()); // y
            return ewkb.array();
        }
    }

    @Override
    public Class<Object> fromType() {
        return Object.class;
    }

    @Override
    public Class<Coordinates> toType() {
        return Coordinates.class;
    }
}
