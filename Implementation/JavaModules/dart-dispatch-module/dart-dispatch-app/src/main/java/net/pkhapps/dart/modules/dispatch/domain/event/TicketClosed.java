package net.pkhapps.dart.modules.dispatch.domain.event;

import net.pkhapps.dart.modules.dispatch.domain.Ticket;
import org.jetbrains.annotations.NotNull;

/**
 * Domain event published when a ticket has been closed.
 */
public class TicketClosed extends TicketEvent {

    public TicketClosed(@NotNull Ticket ticket) {
        super(ticket);
    }
}
