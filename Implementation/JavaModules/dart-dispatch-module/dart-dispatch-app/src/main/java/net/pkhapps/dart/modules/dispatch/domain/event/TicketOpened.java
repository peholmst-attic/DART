package net.pkhapps.dart.modules.dispatch.domain.event;

import net.pkhapps.dart.modules.dispatch.domain.Ticket;
import org.jetbrains.annotations.NotNull;

/**
 * Domain event published when a ticket has been opened.
 */
public class TicketOpened extends TicketEvent {

    public TicketOpened(@NotNull Ticket ticket) {
        super(ticket);
    }
}
