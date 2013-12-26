/*
 * This file is part of JGrasstools (http://www.jgrasstools.org)
 * (C) HydroloGIS - www.hydrologis.com 
 * 
 * JGrasstools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jgrasstools.hortonmachine.models.hm;

import java.util.HashMap;

import org.geotools.coverage.grid.GridCoverage2D;
import org.jgrasstools.gears.utils.coverage.CoverageUtilities;
import org.jgrasstools.hortonmachine.modules.hillslopeanalyses.h2cd.OmsH2cd;
import org.jgrasstools.hortonmachine.utils.HMTestCase;
import org.jgrasstools.hortonmachine.utils.HMTestMaps;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
/**
 * Tests the {@link OmsH2cd} module.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 * @author Daniele Andreis
 */
public class TestH2cd extends HMTestCase {

    @SuppressWarnings("nls")
    public void testH2cd() throws Exception {
        HashMap<String, Double> envelopeParams = HMTestMaps.envelopeParams;
        CoordinateReferenceSystem crs = HMTestMaps.crs;

        double[][] flowData = HMTestMaps.flowData;
        double[][] netData = HMTestMaps.extractNet1Data;

        GridCoverage2D flowRaster = CoverageUtilities.buildCoverage("flow", flowData, envelopeParams, crs, true);
        GridCoverage2D netRaster = CoverageUtilities.buildCoverage("net", netData, envelopeParams, crs, true);

        OmsH2cd h2cd = new OmsH2cd();
        h2cd.inFlow = flowRaster;
        h2cd.inNet = netRaster;
        h2cd.pMode = 0;
        h2cd.process();
        GridCoverage2D outH2cd = h2cd.outH2cd;

        checkMatrixEqual(outH2cd.getRenderedImage(), HMTestMaps.h2cdData);

        h2cd = new OmsH2cd();
        h2cd.inFlow = flowRaster;
        h2cd.inNet = netRaster;
        h2cd.pMode = 1;
        h2cd.process();
        outH2cd = h2cd.outH2cd;

        checkMatrixEqual(outH2cd.getRenderedImage(), HMTestMaps.h2cdTopoData, 0.05);

        // 3d mode
        double[][] elevData = HMTestMaps.pitData;
        GridCoverage2D elevRaster = CoverageUtilities.buildCoverage("pit", elevData, envelopeParams, crs, true);
        h2cd = new OmsH2cd();
        h2cd.inFlow = flowRaster;
        h2cd.inNet = netRaster;
        h2cd.inElev = elevRaster;
        h2cd.pMode = 1;
        h2cd.process();
        outH2cd = h2cd.outH2cd;
        
        checkMatrixEqual(outH2cd.getRenderedImage(), HMTestMaps.h2cd3dData, 0.05);
    }

}
