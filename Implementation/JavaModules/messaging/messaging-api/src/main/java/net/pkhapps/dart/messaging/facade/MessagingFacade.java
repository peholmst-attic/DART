package net.pkhapps.dart.messaging.facade;

import net.pkhapps.dart.messaging.messages.*;
import net.pkhapps.dart.messaging.converters.MessageConverterException;
import net.pkhapps.dart.messaging.routing.Destination;
import org.jetbrains.annotations.NotNull;

public interface MessagingFacade {

    void fireAndForget(@NotNull FireAndForgetCommand command)
            throws SendFailureException, MessageConverterException;

    void fireAndForget(@NotNull FireAndForgetCommand command, @NotNull Destination destination)
            throws SendFailureException, MessageConverterException;

    void fireAndForget(@NotNull FireAndForgetCommand command, @NotNull Iterable<Destination> destinations)
            throws SendFailureException, MessageConverterException;

    <REQ extends Request<RESP>, RESP extends Response> void query(@NotNull REQ request,
                                                                  @NotNull ResponseCallback<RESP> responseCallback)
            throws SendFailureException, MessageConverterException;

    void broadcast(@NotNull Broadcast message) throws SendFailureException, MessageConverterException;
}
