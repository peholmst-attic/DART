package net.pkhapps.dart.messaging.common.facade;

import net.pkhapps.dart.messaging.common.messages.*;
import net.pkhapps.dart.messaging.common.messages.converters.MessageConverterException;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Objects;

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
