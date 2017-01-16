package net.pkhapps.dart.tickets.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.pkhapps.dart.tickets.enums.TicketState;

import java.time.Instant;

/**
 * Created by peholmst on 30/10/2016.
 */
public class Ticket {

    @JsonProperty(required = true)
    private long id;

    @JsonProperty(required = true)
    private TicketState state;

    @JsonProperty(required = true)
    private Instant opened;

    @JsonProperty
    private Instant closed;

    @JsonProperty
    private TicketType type;

    /**
     * Used by Jackson only.
     */
    private Ticket() {
        // NOP
    }
}
