package net.pkhapps.dart.map.importer.taustakartta;

import net.pkhapps.dart.map.importer.AbstractJooqImporter;
import net.pkhapps.dart.map.importer.CSR;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.UpdatableRecord;

import java.awt.*;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

import static net.pkhapps.dart.map.database.tables.NlsMap_1_10000.NLS_MAP_1_10000;
import static net.pkhapps.dart.map.database.tables.NlsMap_1_160000.NLS_MAP_1_160000;
import static net.pkhapps.dart.map.database.tables.NlsMap_1_20000.NLS_MAP_1_20000;
import static net.pkhapps.dart.map.database.tables.NlsMap_1_40000.NLS_MAP_1_40000;
import static net.pkhapps.dart.map.database.tables.NlsMap_1_80000.NLS_MAP_1_80000;

/**
 * TODO Implement me
 */
public class NlsRasterMapImporter extends AbstractJooqImporter {

    private static final int TILE_WIDTH_PX = 100;
    private static final int TILE_HEIGHT_PX = 100;


    @Override
    protected void importData(DSLContext dslContext) throws Exception {
        // TODO Replace with system property or similar
        File dataDirectory = new File("/Users/petterprivate/Google Drive/Maps/");

        if (!dataDirectory.isDirectory()) {
            throw new IllegalArgumentException("Not a directory: " + dataDirectory.getCanonicalPath());
        }

        //importDirectory(new File(dataDirectory, "taustakartta_1_5000"), dslContext, NLS_MAP_1_5000);
        importDirectory(new File(dataDirectory, "taustakartta_1_10000"), dslContext, NLS_MAP_1_10000);
        importDirectory(new File(dataDirectory, "taustakartta_1_20000"), dslContext, NLS_MAP_1_20000);
        importDirectory(new File(dataDirectory, "taustakartta_1_40000"), dslContext, NLS_MAP_1_40000);
        importDirectory(new File(dataDirectory, "taustakartta_1_80000"), dslContext, NLS_MAP_1_80000);
        importDirectory(new File(dataDirectory, "taustakartta_1_160000"), dslContext, NLS_MAP_1_160000);
        //importDirectory(new File(dataDirectory, "taustakartta_1_320000"), dslContext, NLS_MAP_1_320000);
    }

    private <R extends UpdatableRecord<R>> void importDirectory(File directory, DSLContext dslContext, Table<R> table) throws Exception {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Not a directory: " + directory.getCanonicalPath());
        }
        try {
            Files.list(directory.toPath())
                    .filter(path -> path.toString().toLowerCase().endsWith(".png"))
                    .forEach(path -> {
                        try {
                            importFile(path.toFile(), dslContext, table);
                        } catch (Exception ex) {
                            throw new ImportException(ex);
                        }
                    });
        } catch (ImportException ex) {
            throw ex.getCause();
        }
    }

    private static class ImportException extends RuntimeException {

        public ImportException(Exception cause) {
            super(cause);
        }

        @Override
        public synchronized Exception getCause() {
            return (Exception) super.getCause();
        }
    }


    private <R extends UpdatableRecord<R>> void importFile(File file, DSLContext dslContext, Table<R> table) throws Exception {
        System.out.println("Importing tiles from " + file.getAbsolutePath());

        // Check if the file has already been imported
        if (dslContext.selectCount().from(table).where(table.field("filename", String.class).upper().eq(file.getName().toUpperCase())).fetchOne().value1() != 0) {
            System.out.println("File " + file.getName() + " has already been imported, skipping");
            return;
        }

        AbstractGridFormat format = GridFormatFinder.findFormat(file);

        GridCoverage2DReader reader = format.getReader(file);
        GridCoverage2D coverage = reader.read(null);
        ReferencedEnvelope envelope = new ReferencedEnvelope(coverage.getEnvelope2D(), CSR.ETRS_TM35FIN);
        RenderedImage rasterImage = coverage.getRenderedImage();
        IndexColorModel colorModel = (IndexColorModel) rasterImage.getColorModel();
        Raster raster = rasterImage.getData();

        if (raster.getHeight() % TILE_HEIGHT_PX != 0 || raster.getWidth() % TILE_WIDTH_PX != 0) {
            throw new IllegalArgumentException("Image " + file.getName() + " has invalid size");
        }

        int rows = (raster.getHeight() / TILE_HEIGHT_PX);
        int cols = (raster.getWidth() / TILE_WIDTH_PX);

        double geoWidth = envelope.getWidth() / cols;
        double geoHeight = envelope.getHeight() / rows;

        List<R> records = new LinkedList<>();

        System.out.printf("Image is %d x %d, will generate %d tiles%n", raster.getHeight(), raster.getWidth(), rows * cols);

        for (int y = 0; y < rows; ++y) {
            for (int x = 0; x < rows; ++x) {
                Rectangle r = new Rectangle(x * TILE_WIDTH_PX, y * TILE_HEIGHT_PX, TILE_WIDTH_PX, TILE_HEIGHT_PX);
                double envStartX = (x * geoWidth) + envelope.getMinX();
                double envEndX = envStartX + geoWidth;
                double envStartY = (y * geoHeight) + envelope.getMinY();
                double envEndY = envStartY + geoHeight;
                ReferencedEnvelope tileEnvelope = new ReferencedEnvelope(envStartX, envEndX, envStartY, envEndY, CSR.ETRS_TM35FIN);
                ReferencedEnvelope tileEnvelopeWGS84 = tileEnvelope.transform(CSR.WGS84, false);
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
                        .withRaster(raster, r, 0, colorModel)
                        .build();

                R record = dslContext.newRecord(table);
                record.setValue(table.field("rast", net.pkhapps.dart.common.Raster.class), dbRaster);
                record.setValue(table.field("filename", String.class), file.getName().toUpperCase());
                record.setValue(table.field("tile_x", Integer.class), x);
                record.setValue(table.field("tile_y", Integer.class), y);
                records.add(record);

                if (records.size() >= 100) {
                    runBatch(records, dslContext, NoGrouping);
                }
            }
        }

        if (records.size() > 0) {
            runBatch(records, dslContext, NoGrouping);
        }

        coverage.dispose(false);
        reader.dispose();
        System.out.println("Done importing tiles from " + file.getAbsolutePath());
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("jdbc.url", "jdbc:postgresql:dart_map");
        System.setProperty("jdbc.user", "dart");
        System.setProperty("jdbc.password", "dart");

        new NlsRasterMapImporter().importData();
    }

}
