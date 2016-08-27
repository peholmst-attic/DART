package net.pkhapps.dart.common;

import java.awt.*;
import java.awt.image.IndexColorModel;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO Document me!
 */
public class Raster implements Serializable {

    private final double scaleX;
    private final double scaleY;
    private final double ipX;
    private final double ipY;
    private final double skewX;
    private final double skewY;
    private final short width;
    private final short height;
    private final Band[] bands;

    // SRID is always 4326 (WGS 84)

    private Raster(double scaleX, double scaleY, double ipX, double ipY, double skewX, double skewY, short width, short height, List<PixelType> pixelTypes, List<byte[]> data) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.ipX = ipX;
        this.ipY = ipY;
        this.skewX = skewX;
        this.skewY = skewY;
        this.width = width;
        this.height = height;
        this.bands = new Band[pixelTypes.size()];
        for (int i = 0; i < bands.length; ++i) {
            this.bands[i] = new Band(pixelTypes.get(i), data.get(i));
        }
    }

    public short getBands() {
        return (short) bands.length;
    }

    public Band getBand(short index) {
        return bands[index];
    }

    public double getUpperLeftLongitude() {
        return ipX;
    }

    public double getUpperLeftLatitude() {
        return ipY;
    }

    public double getScaleLongitude() {
        return scaleX;
    }

    public double getScaleLatitude() {
        return scaleY;
    }

    public double getSkewLongitude() {
        return skewX;
    }

    public double getSkewLatitude() {
        return skewY;
    }

    public short getWidth() {
        return width;
    }

    public short getHeight() {
        return height;
    }

    public class Band implements Serializable {
        private final PixelType pixelType;
        private final byte[] data;

        private Band(PixelType pixelType, byte[] data) {
            this.pixelType = pixelType;
            this.data = data;
        }

        public PixelType getPixelType() {
            return pixelType;
        }

        public byte[] getData() {
            return Arrays.copyOf(data, data.length);
        }
    }

    public enum PixelType {
        BOOLEAN_1(1),
        UNSIGNED_INTEGER_2(1),
        UNSIGNED_INTEGER_4(1),
        SIGNED_INTEGER_8(1),
        UNSIGNED_INTEGER_8(1),
        SIGNED_INTEGER_16(2),
        UNSIGNED_INTEGER_16(2),
        SIGNED_INTEGER_32(4),
        UNSIGNED_INTEGER_32(4),
        FLOAT_32(4),
        FLOAT_64(8);

        private final int byteLength;

        PixelType(int length) {
            this.byteLength = length;
        }

        public int getByteLength() {
            return byteLength;
        }
    }

    public static class Builder {

        private final double scaleX;
        private final double scaleY;
        private final double ipX;
        private final double ipY;
        private short width = -1;
        private short height = -1;
        private double skewX = 0;
        private double skewY = 0;
        private final List<PixelType> pixelTypes = new LinkedList<>();
        private final List<byte[]> dataArrays = new LinkedList<>();

        public Builder(double scaleX, double scaleY, double ipX, double ipY) {
            this.scaleX = scaleX;
            this.scaleY = scaleY;
            this.ipX = ipX;
            this.ipY = ipY;
        }

        public Builder withSize(short width, short height) {
            if (this.width != -1 || this.height != -1) {
                throw new IllegalArgumentException("Size has already been set on this Builder instance");
            }
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder withSkew(double skewX, double skewY) {
            this.skewX = skewX;
            this.skewY = skewY;
            return this;
        }

        public Builder withRaster(java.awt.image.Raster raster, Rectangle r, int band, IndexColorModel colorModel) {
            if (width != -1 || height != -1) {
                throw new IllegalArgumentException("Size has already been set, use withBand instead");
            }
            width = (short) r.width;
            height = (short) r.height;

            for (PixelComponent component : PixelComponent.values()) {
                byte[] data = new byte[width * height * Short.BYTES];
                ByteBuffer buf = ByteBuffer.wrap(data);
                for (int x = 0; x < width; ++x) {
                    for (int y = 0; y < height; ++y) {
                        int sample = raster.getSample(x + r.x, y + r.y, band);
                        short pixelValue = 0x00;
                        switch (component) {
                            case RED:
                                pixelValue = (short) (colorModel.getRed(sample) & 0xFFFF);
                                break;
                            case GREEN:
                                pixelValue = (short) (colorModel.getGreen(sample) & 0xFFFF);
                                break;
                            case BLUE:
                                pixelValue = (short) (colorModel.getBlue(sample) & 0xFFFF);
                                break;
                        }
                        buf.putShort(pixelValue);
                    }
                }
                pixelTypes.add(PixelType.UNSIGNED_INTEGER_16);
                dataArrays.add(data);
            }
            return this;
        }

        public Builder withBand(PixelType pixelType, byte[] data) {
            if (width == -1 || height == -1) {
                throw new IllegalArgumentException("Size has not been set yet");
            }
            if (data.length != width * height * pixelType.getByteLength()) {
                throw new IllegalArgumentException("Incorrect size of data array");
            }
            pixelTypes.add(pixelType);
            dataArrays.add(Arrays.copyOf(data, data.length));
            return this;
        }

        public Raster build() {
            return new Raster(scaleX, scaleY, ipX, ipY, skewX, skewY, width, height, pixelTypes, dataArrays);
        }

        enum PixelComponent {
            RED, GREEN, BLUE;
        }
    }
}
