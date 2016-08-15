package net.pkhapps.dart.common.converters;

import net.pkhapps.dart.common.Raster;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.jooq.Converter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Objects;

/**
 * TODO Document (and implement) me!
 */
public class RasterConverter implements Converter<Object, Raster> {

    @Override
    public Raster from(Object databaseObject) {
        if (databaseObject != null) {
            try {
                final String ewkbhex = Objects.toString(databaseObject);
                final byte[] ewkb = Hex.decodeHex(ewkbhex.toCharArray());

                final ByteOrder byteOrder = ewkb[0] == 0 ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
                ByteBuffer buf = ByteBuffer.wrap(ewkb, 1, ewkb.length - 1).order(byteOrder);
                final int version = buf.getShort();
                final int nBands = buf.getShort();
                final double scaleX = buf.getDouble();
                final double scaleY = buf.getDouble();
                final double ipX = buf.getDouble();
                final double ipY = buf.getDouble();
                final double skewX = buf.getDouble();
                final double skewY = buf.getDouble();
                final int srid = buf.getInt();
                final short width = buf.getShort();
                final short height = buf.getShort();

                for (int b = 0; b < nBands; ++b) {
                    byte header = buf.get();
                    boolean isOffline = (header & 0x01) == 0x01;
                    boolean hasNodataValue = (header & 0x2) == 0x02;
                    boolean isNodataValue = (header & 0x04) == 0x04;
                    short pixtype = (short) ((header >> 4) & 0xf);

                    short pixLen;
                    switch (pixtype) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                            pixLen = 1;
                            break;
                        case 5:
                        case 6:
                            pixLen = 2;
                            break;
                        case 7:
                        case 8:
                        case 9:
                            pixLen = 4;
                            break;
                        case 10:
                            pixLen = 8;
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid pixtype: " + pixtype);
                    }
                    byte[] nodata = new byte[pixLen];
                    buf.get(nodata);

                    if (isOffline) {
                        throw new UnsupportedOperationException("Offline rasters are not supported yet");
                    } else {
                        byte[] pix = new byte[height * width * pixLen];
                        buf.get(pix);
                    }
                }
                return new Raster();
            } catch (DecoderException ex) {
                throw new IllegalArgumentException("Could not decode EWKBHEX data", ex);
            }
        }
        return null;
    }

    @Override
    public Object to(Raster userObject) {
        if (userObject == null) {
            return null;
        } else {

            return null;
        }
    }

    @Override
    public Class<Object> fromType() {
        return Object.class;
    }

    @Override
    public Class<Raster> toType() {
        return Raster.class;
    }

    public static void main(String[] args) throws Exception {
        // TODO Remove this method when no longer needed
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql:gisdb", "petterprivate", "")) {
            try (PreparedStatement stmt = conn.prepareStatement("SELECT rast FROM taustakartta_1_20000 WHERE rid=2")) {
                stmt.execute();
                try (ResultSet resultSet = stmt.getResultSet()) {
                    resultSet.next();
                    Object databaseObject = resultSet.getObject(1);
                    Raster raster = new RasterConverter().from(databaseObject);
                    System.out.println(raster);
                }
            }
        }
    }
}
