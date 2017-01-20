package net.pkhapps.dart.common.location;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.pkhapps.dart.common.Coordinates;
import net.pkhapps.dart.common.i18n.Locales;
import net.pkhapps.dart.common.i18n.LocalizedString;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link Location}.
 */
public class LocationTest {

    private ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    public void testJsonSerialization_noFeature() throws Exception {
        Location original = new Location(Coordinates.builder()
                .withLatitude(60.291746)
                .withLongitude(22.307109).build(),
                "a description",
                new Municipality(123, LocalizedString.builder().with(Locales.SWEDISH, "Pargas").build()),
                false,
                null);

        String json = objectMapper.writeValueAsString(original);
        System.out.println(json);

        Location read = objectMapper.readValue(json, Location.class);
        assertThat(read).isEqualTo(original);
    }

    @Test
    public void testJsonSerialization_intersection() throws Exception {
        Municipality municipality = new Municipality(123,
                LocalizedString.builder().with(Locales.SWEDISH, "Pargas").build());
        Road road1 = new Road(123, 10, 1,
                LocalizedString.builder().with(Locales.SWEDISH, "Road1").build(), true, municipality);
        Road road2 = new Road(456, 30, 11,
                LocalizedString.builder().with(Locales.SWEDISH, "Road2").build(), true, municipality);
        Location original = new Location(Coordinates.builder()
                .withLatitude(60.291746)
                .withLongitude(22.307109).build(),
                "a description", municipality,
                false, new Intersection(road1, road2));

        String json = objectMapper.writeValueAsString(original);
        System.out.println(json);

        Location read = objectMapper.readValue(json, Location.class);
        assertThat(read).isEqualTo(original);
    }

    @Test
    public void testJsonSerialization_streetAddress() throws Exception {
        Municipality municipality = new Municipality(123,
                LocalizedString.builder().with(Locales.SWEDISH, "Pargas").build());
        Road road = new Road(123, 10, 1,
                LocalizedString.builder().with(Locales.SWEDISH, "Road").build(), true, municipality);
        StreetAddress address = new StreetAddress(road, "1", "B2");
        Location original = new Location(Coordinates.builder()
                .withLatitude(60.291746)
                .withLongitude(22.307109).build(),
                "a description", municipality,
                false, address);

        String json = objectMapper.writeValueAsString(original);
        System.out.println(json);

        Location read = objectMapper.readValue(json, Location.class);
        assertThat(read).isEqualTo(original);
    }
}
