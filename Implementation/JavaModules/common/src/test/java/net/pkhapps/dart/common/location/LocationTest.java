package net.pkhapps.dart.common.location;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.pkhapps.dart.common.Coordinates;
import net.pkhapps.dart.common.i18n.LocalizedString;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link Location}.
 */
public class LocationTest {

    @Test
    public void testJsonSerialization_noFeature() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        Location original = new Location(Coordinates.builder()
                .withLatitude(60.291746)
                .withLongitude(22.307109).build(),
                "a description", new Municipality(123, LocalizedString.builder().withDefault("Pargas").build()),
                false, null);

        String json = objectMapper.writeValueAsString(original);
        System.out.println(json);

        Location read = objectMapper.readValue(json, Location.class);
        assertThat(read).isEqualTo(original);
    }
}
