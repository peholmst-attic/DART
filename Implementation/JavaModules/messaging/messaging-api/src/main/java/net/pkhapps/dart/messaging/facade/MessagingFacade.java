package net.pkhapps.dart.messaging.facade;

import net.pkhapps.dart.messaging.messages.*;
import net.pkhapps.dart.messaging.converters.MessageConverterException;
import net.pkhapps.dart.messaging.routing.Destination;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Objects;

public interface MessagingFacade<D extends Destination> {

    void fireAndForget(@NotNull FireAndForgetCommand command)
            throws SendFailureException, MessageConverterException;

    default void fireAndForget(@NotNull FireAndForgetCommand command, @NotNull D destination)
            throws SendFailureException, MessageConverterException {
        Objects.requireNonNull(command, "command cannot be null");
        Objects.requireNonNull(destination, "destination cannot be null");
        fireAndForget(command, Collections.singleton(destination));
    }

    void fireAndForget(@NotNull FireAndForgetCommand command, @NotNull Iterable<D> destinations)
            throws SendFailureException, MessageConverterException;

    <REQ extends Request<RESP>, RESP extends Response> void query(@NotNull REQ request,
                                                                  @NotNull ResponseCallback<RESP> responseCallback)
            throws SendFailureException, MessageConverterException;

    void broadcast(@NotNull Broadcast message) throws SendFailureException, MessageConverterException;
}
