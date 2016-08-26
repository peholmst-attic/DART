package net.pkhapps.dart.map.importer.taustakartta;

import net.pkhapps.dart.map.database.tables.NlsMap_1_5000;
import net.pkhapps.dart.map.database.tables.records.NlsMap_1_5000Record;
import net.pkhapps.dart.map.importer.AbstractJooqImporter;
import net.pkhapps.dart.map.importer.CSR;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.jooq.DSLContext;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;

/**
 * TODO Implement me
 */
public class NlsRasterImporter extends AbstractJooqImporter {

    private static final int TILE_WIDTH_PX = 100;
    private static final int TILE_HEIGHT_PX = 100;


    @Override
    protected void importData(DSLContext dslContext) throws Exception {
        File file = new File("/Users/petterprivate/Google Drive/Maps/taustakartta_1_5000/L3314B.png");
        importFile(file, dslContext);
    }

    private void importFile(File file, DSLContext dslContext) throws Exception {
        AbstractGridFormat format = GridFormatFinder.findFormat(file);

        GridCoverage2DReader reader = format.getReader(file);
        GridCoverage2D coverage = reader.read(null);
        ReferencedEnvelope envelope = new ReferencedEnvelope(coverage.getEnvelope2D(), CSR.ETRS_TM35FIN);
        RenderedImage raster = coverage.getRenderedImage();

        int rows = (raster.getHeight() / TILE_HEIGHT_PX);
        int cols = (raster.getWidth() / TILE_WIDTH_PX);

        double geoWidth = envelope.getWidth() / cols;
        double geoHeight = envelope.getHeight() / rows;

        int count = 0;

        for (int y = 0; y < rows; ++y) {
            for (int x = 0; x < rows; ++x) {
                Rectangle r = new Rectangle(x * TILE_WIDTH_PX, y * TILE_HEIGHT_PX, TILE_WIDTH_PX, TILE_HEIGHT_PX);
                double envStartX = (x * geoWidth) + envelope.getMinX();
                double envEndX = envStartX + geoWidth;
                double envStartY = (y * geoHeight) + envelope.getMinY();
                double envEndY = envStartY + geoHeight;
                ReferencedEnvelope tileEnvelope = new ReferencedEnvelope(envStartX, envEndX, envStartY, envEndY, CSR.ETRS_TM35FIN);
                ReferencedEnvelope tileEnvelopeWGS84 = tileEnvelope.transform(CSR.WGS84, false);
                Raster tileRaster = raster.getData(r).createTranslatedChild(0, 0);

                double scaleX = tileEnvelopeWGS84.getWidth() / TILE_WIDTH_PX;
                double scaleY = tileEnvelopeWGS84.getHeight() / TILE_HEIGHT_PX;

                // TODO If the Raster.Builder can convert the image to RGB, we don't need these conversions here
                //BufferedImage indexedImage = new BufferedImage(tileRaster.getWidth(), tileRaster.getHeight(), BufferedImage.TYPE_BYTE_INDEXED, (IndexColorModel) raster.getColorModel());
                //indexedImage.setData(tileRaster);

                // TODO Can remove this too (see above)
                //BufferedImage rgbImage = new BufferedImage(tileRaster.getWidth(), tileRaster.getHeight(), BufferedImage.TYPE_INT_RGB);
                //rgbImage.createGraphics().drawImage(indexedImage, 0, 0, null);

                //ImageIO.write(indexedImage, "png", new File("/tmp/map_index_" + x + y + ".png"));
                //ImageIO.write(rgbImage, "png", new File("/tmp/map_rgb_" + x + y + ".png"));

                net.pkhapps.dart.common.Raster dbRaster = new net.pkhapps.dart.common.Raster
                        .Builder(scaleX, scaleY, tileEnvelopeWGS84.getMinX(), tileEnvelopeWGS84.getMinY())
                        .withRaster(tileRaster, 0, (IndexColorModel) raster.getColorModel())
                        .build();

                NlsMap_1_5000Record record = dslContext.newRecord(NlsMap_1_5000.NLS_MAP_1_5000);
                record.setRast(dbRaster);
                record.insert();

                count++;
                if (count == 10) {
                    return;
                }
            }
        }

        coverage.dispose(false);
        reader.dispose();
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("jdbc.url", "jdbc:postgresql:dart_map");
        System.setProperty("jdbc.user", "dart");
        System.setProperty("jdbc.password", "dart");

        new NlsRasterImporter().importData();
    }

}
