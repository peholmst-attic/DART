package net.pkhapps.dart.messaging.facade;

import net.pkhapps.dart.messaging.converters.MessageConverterException;
import net.pkhapps.dart.messaging.messages.Broadcast;
import net.pkhapps.dart.messaging.messages.FireAndForgetCommand;
import net.pkhapps.dart.messaging.messages.Request;
import net.pkhapps.dart.messaging.messages.Response;
import net.pkhapps.dart.messaging.routing.Destination;

import java.util.Collections;
import java.util.Objects;

public interface MessagingFacade<D extends Destination> {

    void fireAndForget(FireAndForgetCommand command)
            throws SendFailureException, MessageConverterException;

    default void fireAndForget(FireAndForgetCommand command, D destination)
            throws SendFailureException, MessageConverterException {
        Objects.requireNonNull(command);
        Objects.requireNonNull(destination);
        fireAndForget(command, Collections.singleton(destination));
    }

    void fireAndForget(FireAndForgetCommand command, Iterable<D> destinations)
            throws SendFailureException, MessageConverterException;

    <REQ extends Request<RESP>, RESP extends Response> void query(REQ request,
                                                                  ResponseCallback<RESP> responseCallback)
            throws SendFailureException, MessageConverterException;

    void broadcast(Broadcast message) throws SendFailureException, MessageConverterException;
}
