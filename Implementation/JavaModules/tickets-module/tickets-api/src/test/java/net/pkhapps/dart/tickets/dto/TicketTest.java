package net.pkhapps.dart.tickets.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.pkhapps.dart.common.Coordinates;
import net.pkhapps.dart.common.i18n.Locales;
import net.pkhapps.dart.common.i18n.LocalizedString;
import net.pkhapps.dart.common.location.Location;
import net.pkhapps.dart.common.location.Municipality;
import net.pkhapps.dart.common.location.Road;
import net.pkhapps.dart.common.location.StreetAddress;
import net.pkhapps.dart.tickets.enums.TicketPriority;
import net.pkhapps.dart.tickets.enums.TicketState;
import org.junit.Test;

import java.time.Instant;

/**
 * Unit test for {@link Ticket}.
 */
public class TicketTest {

    @Test
    public void testJsonSerialization_noSubtickets() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(SerializationFeature.INDENT_OUTPUT);
        TicketType ticketType = new TicketType(1L, "402", LocalizedString.builder()
                .with(Locales.FINNISH, "rakennuspalo: keskisuuri")
                .with(Locales.SWEDISH, "byggnadsbrand: medelstor").build(), true, true);
        Municipality municipality = new Municipality(123,
                LocalizedString.builder().with(Locales.SWEDISH, "Pargas").build());
        Road road = new Road(123, 10, 1,
                LocalizedString.builder().with(Locales.SWEDISH, "Road").build(), true, municipality);
        StreetAddress address = new StreetAddress(road, "1", "B2");
        Location location = new Location(Coordinates.builder()
                .withLatitude(60.291746)
                .withLongitude(22.307109).build(),
                "a description", municipality,
                false, address);

        Ticket original = Ticket.builder()
                .withId(123)
                .withState(TicketState.CREATED)
                .withOpened(Instant.now()).withType(ticketType)
                .withPriority(TicketPriority.B)
                .withLocation(location)
                .withDetails("two storey building, smoke from the roof, no people inside")
                .withReporterName("Joe Cool")
                .withReporterPhone("123 456 7890")
                .build();

        String json = objectMapper.writeValueAsString(original);
        System.out.println(json);
    }
}
