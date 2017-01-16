package net.pkhapps.dart.common.i18n;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link LocalizedString}.
 */
public class LocalizedStringTest {

    @Test
    public void testJsonSerialization() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        LocalizedString original = LocalizedString.builder()
                .with(Locales.SWEDISH, "Hej")
                .with(Locales.FINNISH, "Terve")
                .withDefault("Hello").build();

        String json = objectMapper.writeValueAsString(original);
        System.out.println(json);

        LocalizedString read = objectMapper.readValue(json, LocalizedString.class);
        assertThat(read).isEqualTo(original);
    }
}
