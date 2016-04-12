package net.pkhapps.dart.types;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.jooq.Converter;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

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
        return userObject == null ? null : String.format("ST_SetSRID(ST_Point(%f, %f), 4326)",
                userObject.getLongitude(), userObject.getLatitude());
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
