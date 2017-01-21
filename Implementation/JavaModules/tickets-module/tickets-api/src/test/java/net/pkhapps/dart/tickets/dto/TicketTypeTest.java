package net.pkhapps.dart.tickets.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.pkhapps.dart.common.i18n.Locales;
import net.pkhapps.dart.common.i18n.LocalizedString;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link TicketType}.
 */
public class TicketTypeTest {

    @Test
    public void testJsonSerialization() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules()
                .enable(SerializationFeature.INDENT_OUTPUT);
        TicketType original = new TicketType(1L, "402", LocalizedString.builder()
                .with(Locales.FINNISH, "rakennuspalo: keskisuuri")
                .with(Locales.SWEDISH, "byggnadsbrand: medelstor").build(), true, true);

        String json = objectMapper.writeValueAsString(original);
        System.out.println(json);

        TicketType read = objectMapper.readValue(json, TicketType.class);
        assertThat(read).isEqualTo(original);
    }
}
