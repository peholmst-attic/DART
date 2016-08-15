package net.pkhapps.dart.map.importer.taustakartta;

import com.vividsolutions.jts.geom.Envelope;
import net.pkhapps.dart.map.importer.CSR;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.JTS;
import org.opengis.coverage.processing.GridCoverageProcessor;

import java.io.File;
import java.util.Arrays;

/**
 * TODO Implement me
 */
public class NlsRasterImporter {

    public static void main(String[] args) throws Exception {
        //"/Users/petterprivate/Google Drive/Maps/taustakartta_1_5000/";
        //"L3321E.png" "L3321E.pgw"
        //System.out.println(GridFormatFinder.getAvailableFormats());
        File file = new File("/Users/petterprivate/Google Drive/Maps/taustakartta_1_5000/L3321E.png");
        AbstractGridFormat format = GridFormatFinder.findFormat(file);

        GridCoverage2DReader reader = format.getReader(file);

        GridCoverage2D coverage = reader.read(null);

        CoverageProcessor coverageProcessor = CoverageProcessor.getInstance();

        //coverage.

        System.out.println(coverage);

//        GeneralEnvelope envelope = reader.getOriginalEnvelope();
//        envelope.setCoordinateReferenceSystem(CSR.ETRS_TM35FIN);

    }
}
