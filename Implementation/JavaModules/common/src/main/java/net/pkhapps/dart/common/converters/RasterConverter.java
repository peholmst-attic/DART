package net.pkhapps.dart.common.converters;

import net.pkhapps.dart.common.Raster;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.jooq.Converter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

/**
 * TODO Document me!
 */
public class RasterConverter implements Converter<Object, Raster> {

    @Override
    public Raster from(Object databaseObject) {
        if (databaseObject != null) {
            try {
                final String wkbhex = Objects.toString(databaseObject);
                final byte[] wkb = Hex.decodeHex(wkbhex.toCharArray());

                final ByteOrder byteOrder = wkb[0] == 0 ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
                ByteBuffer buf = ByteBuffer.wrap(wkb, 1, wkb.length - 1).order(byteOrder);
                final int version = buf.getShort(); // Ignored for now
                final short nBands = buf.getShort();
                final double scaleX = buf.getDouble();
                final double scaleY = buf.getDouble();
                final double ipX = buf.getDouble();
                final double ipY = buf.getDouble();
                final double skewX = buf.getDouble();
                final double skewY = buf.getDouble();
                final int srid = buf.getInt();
                if (srid != 4326) {
                    throw new IllegalArgumentException("Expected SRID 4326, got " + srid);
                }
                final short width = buf.getShort();
                final short height = buf.getShort();

                Raster.Builder builder = new Raster.Builder(scaleX, scaleY, ipX, ipY).withSkew(skewX, skewY).withSize(width, height);

                for (int b = 0; b < nBands; ++b) {
                    byte header = buf.get();
                    boolean isOffline = (header & 0x01) == 0x01;
                    if (isOffline) {
                        throw new IllegalArgumentException("Offline rasters not supported");
                    }
                    boolean hasNodataValue = (header & 0x2) == 0x02; // Ignored for now
                    boolean isNodataValue = (header & 0x04) == 0x04; // Ignored for now
                    short pixtype = (short) ((header >> 4) & 0xf);
                    Raster.PixelType rasterPixType;
                    switch (pixtype) {
                        case 0:
                            rasterPixType = Raster.PixelType.BOOLEAN_1;
                            break;
                        case 1:
                            rasterPixType = Raster.PixelType.UNSIGNED_INTEGER_2;
                            break;
                        case 2:
                            rasterPixType = Raster.PixelType.UNSIGNED_INTEGER_4;
                            break;
                        case 3:
                            rasterPixType = Raster.PixelType.SIGNED_INTEGER_8;
                            break;
                        case 4:
                            rasterPixType = Raster.PixelType.UNSIGNED_INTEGER_8;
                            break;
                        case 5:
                            rasterPixType = Raster.PixelType.SIGNED_INTEGER_16;
                            break;
                        case 6:
                            rasterPixType = Raster.PixelType.UNSIGNED_INTEGER_16;
                            break;
                        case 7:
                            rasterPixType = Raster.PixelType.SIGNED_INTEGER_32;
                            break;
                        case 8:
                            rasterPixType = Raster.PixelType.UNSIGNED_INTEGER_32;
                            break;
                        case 9:
                            rasterPixType = Raster.PixelType.FLOAT_32;
                            break;
                        case 10:
                            rasterPixType = Raster.PixelType.FLOAT_64;
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid pixtype: " + pixtype);
                    }
                    byte[] nodata = new byte[rasterPixType.getByteLength()];
                    buf.get(nodata); // Ignored for now

                    byte[] pix = new byte[height * width * rasterPixType.getByteLength()];
                    ByteBuffer pixBuf = ByteBuffer.wrap(pix);

                    for (int p = 0; p < width * height; ++p) {
                        switch (rasterPixType) {
                            case BOOLEAN_1:
                            case UNSIGNED_INTEGER_2:
                            case UNSIGNED_INTEGER_4:
                            case UNSIGNED_INTEGER_8:
                            case SIGNED_INTEGER_8:
                                pixBuf.put(buf.get());
                                break;
                            case UNSIGNED_INTEGER_16:
                            case SIGNED_INTEGER_16:
                                pixBuf.putShort(buf.getShort());
                                break;
                            case UNSIGNED_INTEGER_32:
                            case SIGNED_INTEGER_32:
                                pixBuf.putInt(buf.getInt());
                                break;
                            case FLOAT_32:
                                pixBuf.putFloat(buf.getFloat());
                                break;
                            case FLOAT_64:
                                pixBuf.putDouble(buf.getDouble());
                                break;
                        }
                    }
                    builder.withBand(rasterPixType, pix);
                }
                return builder.build();
            } catch (DecoderException ex) {
                throw new IllegalArgumentException("Could not decode WKBHEX data", ex);
            }
        }
        return null;
    }

    @Override
    public Object to(Raster userObject) {
        if (userObject == null) {
            return null;
        } else {
            // Calculate size of byte buffer
            int bufSize = Byte.BYTES + Short.BYTES * 4 + Integer.BYTES + Double.BYTES * 6;
            for (short bandIx = 0; bandIx < userObject.getBands(); ++bandIx) {
                Raster.Band band = userObject.getBand(bandIx);
                bufSize += Byte.BYTES; // header
                bufSize += Byte.BYTES * band.getPixelType().getByteLength(); // nodata
                bufSize += band.getPixelType().getByteLength() * userObject.getHeight() * userObject.getWidth(); // data
            }

            // Populate byte buffer
            final ByteBuffer wkb = ByteBuffer.allocate(bufSize).order(ByteOrder.LITTLE_ENDIAN);
            wkb.put((byte) 0x01); // byte order, little endian
            wkb.putShort((short) 0); // version
            wkb.putShort(userObject.getBands()); // nBands
            wkb.putDouble(userObject.getScaleLongitude()); // scaleX
            wkb.putDouble(userObject.getScaleLatitude()); // scaleY
            wkb.putDouble(userObject.getUpperLeftLongitude()); // ipX
            wkb.putDouble(userObject.getUpperLeftLatitude()); // ipY
            wkb.putDouble(userObject.getSkewLongitude()); // skewX
            wkb.putDouble(userObject.getSkewLatitude()); // skewY
            wkb.putInt(4326); // SRID
            wkb.putShort(userObject.getWidth()); // width
            wkb.putShort(userObject.getHeight()); // height

            for (short bandIx = 0; bandIx < userObject.getBands(); ++bandIx) {
                Raster.Band band = userObject.getBand(bandIx);
                byte header = 0x00;
                switch (band.getPixelType()) {
                    case BOOLEAN_1:
                        header = 0x0;
                        break;
                    case UNSIGNED_INTEGER_2:
                        header = 0x1;
                        break;
                    case UNSIGNED_INTEGER_4:
                        header = 0x2;
                        break;
                    case SIGNED_INTEGER_8:
                        header = 0x3;
                        break;
                    case UNSIGNED_INTEGER_8:
                        header = 0x4;
                        break;
                    case SIGNED_INTEGER_16:
                        header = 0x5;
                        break;
                    case UNSIGNED_INTEGER_16:
                        header = 0x6;
                        break;
                    case SIGNED_INTEGER_32:
                        header = 0x7;
                        break;
                    case UNSIGNED_INTEGER_32:
                        header = (byte) 0x8;
                        break;
                    case FLOAT_32:
                        header = (byte) 0x9;
                        break;
                    case FLOAT_64:
                        header = (byte) 0xA;
                        break;
                }
                wkb.put(header); // isOffline, hasNodataValue, isNodataValue, reserved, pixtype
                for (int i = 0; i < band.getPixelType().getByteLength(); ++i) {
                    wkb.put((byte) 0x0); // nodata
                }
                // raster data
                ByteBuffer data = ByteBuffer.wrap(band.getData());
                for (int p = 0; p < userObject.getWidth() * userObject.getHeight(); ++p) {
                    switch (band.getPixelType()) {
                        case BOOLEAN_1:
                        case UNSIGNED_INTEGER_2:
                        case UNSIGNED_INTEGER_4:
                        case UNSIGNED_INTEGER_8:
                        case SIGNED_INTEGER_8:
                            wkb.put(data.get());
                            break;
                        case UNSIGNED_INTEGER_16:
                        case SIGNED_INTEGER_16:
                            wkb.putShort(data.getShort());
                            break;
                        case UNSIGNED_INTEGER_32:
                        case SIGNED_INTEGER_32:
                            wkb.putInt(data.getInt());
                            break;
                        case FLOAT_32:
                            wkb.putFloat(data.getFloat());
                            break;
                        case FLOAT_64:
                            wkb.putDouble(data.getDouble());
                            break;
                    }
                }
            }
            return wkb.array();
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
}
