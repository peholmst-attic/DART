package net.pkhapps.dart.modules.dispatch.domain.event;

import net.pkhapps.dart.modules.dispatch.domain.Ticket;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Base class for domain events that concern a specific {@link Ticket}.
 */
public abstract class TicketEvent {

    private final Ticket ticket;

    public TicketEvent(@NotNull Ticket ticket) {
        this.ticket = Objects.requireNonNull(ticket);
    }

    public @NotNull Ticket getTicket() {
        return ticket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TicketEvent that = (TicketEvent) o;

        return ticket.equals(that.ticket);
    }

    @Override
    public int hashCode() {
        return ticket.hashCode();
    }
}
