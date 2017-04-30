package net.pkhapps.dart.modules.base.rabbitmq.messaging.consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * TODO Document me!
 */
public interface AsyncMessageBroker {

    /**
     * Asynchronously sends the specified request to its destination.
     *
     * @param request           the request to send.
     * @param callback          a callback object that will receive the response.
     * @param requestConverter  a converter that knows how to convert the request POJO to an AMQP message.
     * @param responseConverter a converter that knows how to convert the response from AMQP into a POJO.
     * @throws IOException if something goes wrong while sending the request.
     */
    <REQUEST, RESPONSE> void sendRequest(@NotNull REQUEST request,
                                         @NotNull CallbackWithResponse<RESPONSE> callback,
                                         @NotNull OutgoingMessageConverter<REQUEST> requestConverter,
                                         @NotNull IncomingMessageConverter<RESPONSE> responseConverter)
            throws IOException;

    /**
     * Asynchronously sends the specified command to its destination.
     *
     * @param command          the command to send.
     * @param callback         a callback object that will receive confirmation when the command has
     *                         been processed, or {@code null} to just "fire and forget".
     * @param commandConverter a converter that knows how to convert the command POJO to an AMQP message.
     * @throws IOException if something goes wrong while sending the command.
     */
    <COMMAND> void sendCommand(@NotNull COMMAND command,
                               @Nullable CallbackWithoutResponse callback,
                               @NotNull OutgoingMessageConverter<COMMAND> commandConverter)
            throws IOException;
}
