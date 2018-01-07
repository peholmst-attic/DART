package net.pkhapps.dart.modules.dispatch.domain.event;

import net.pkhapps.dart.base.domain.MergeEqualEvents;
import net.pkhapps.dart.modules.dispatch.domain.Ticket;
import org.jetbrains.annotations.NotNull;

/**
 * Domain event published when an open ticket has been updated.
 */
@MergeEqualEvents
public class TicketUpdated extends TicketEvent {

    public TicketUpdated(@NotNull Ticket ticket) {
        super(ticket);
    }
}
