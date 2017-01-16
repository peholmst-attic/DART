package net.pkhapps.dart.tickets.client;

import net.pkhapps.dart.platform.async.AsyncResult;
import org.jetbrains.annotations.NotNull;

/**
 * Created by peholmst on 30/10/2016.
 */
public interface Tickets {

    @NotNull
    AsyncResult<Ticket> findById(long ticketId);


}

