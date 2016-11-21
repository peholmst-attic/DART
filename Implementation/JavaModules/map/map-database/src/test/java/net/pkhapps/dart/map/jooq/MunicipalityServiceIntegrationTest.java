package net.pkhapps.dart.map.jooq;

import net.pkhapps.dart.common.Coordinates;
import net.pkhapps.dart.common.i18n.Locales;
import net.pkhapps.dart.common.location.Municipality;
import net.pkhapps.dart.map.api.MunicipalityService;
import net.pkhapps.dart.map.api.NameMatch;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Integration test for {@link JooqMunicipalityService}. This test assumes that the latest data from NLS has been
 * imported (see the {@code map-importer} sibling project).
 */
public class MunicipalityServiceIntegrationTest extends AbstractIntegrationTest {

    private final MunicipalityService municipalityService;

    public MunicipalityServiceIntegrationTest() {
        municipalityService = new JooqMunicipalityService(getDSLContext());
    }

    @Test
    public void findByName_exactName_singleMunicipalityReturned() {
        List<Municipality> result = municipalityService.findByName("Parainen", NameMatch.EXACT);
        assertEquals(1, result.size());
        Municipality municipality = result.get(0);
        assertEquals(445, municipality.getId());
        assertEquals("Pargas", municipality.getName().get(Locales.SWEDISH));
        assertEquals("Parainen", municipality.getName().get(Locales.FINNISH));
    }

    @Test
    public void findByName_nullName_noMunicipalitiesReturned() {
        assertTrue(municipalityService.findByName(null, NameMatch.CONTAINS).isEmpty());
    }

    @Test
    public void findByName_emptyName_noMunicipalitiesReturned() {
        assertTrue(municipalityService.findByName("", NameMatch.CONTAINS).isEmpty());
    }

    @Test
    public void findByName_startsWith_multipleMunicipalitiesReturned() {
        List<Municipality> result = municipalityService.findByName("Par", NameMatch.STARTS_WITH);
        assertEquals(3, result.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void findByCoordinates_validCoordinates_methodNotSupported() {
        // These coordinates are the center of Parainen/Pargas.
        municipalityService.findByCoordinates(new Coordinates(new BigDecimal(60.301082), new BigDecimal(22.302241)));
    }
}
