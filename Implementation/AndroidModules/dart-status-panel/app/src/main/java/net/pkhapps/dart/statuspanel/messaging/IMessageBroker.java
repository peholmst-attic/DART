package net.pkhapps.dart.statuspanel.messaging;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Interface defining a message broker that sends commands and requests to other application modules
 * through RabbitMQ.
 */
public interface IMessageBroker {

    /**
     * Asynchronously sends the specified request to its destination.
     *
     * @param request           the request to send.
     * @param callback          a callback object that will receive the response.
     * @param requestConverter  a converter that knows how to convert the request POJO to a RabbitMQ message.
     * @param responseConverter a converter that knows how to convert the response from RabbitMQ into a POJO.
     * @throws IOException if something goes wrong while sending the request.
     */
    <REQUEST, RESPONSE> void sendRequest(@NonNull REQUEST request,
                                         @NonNull ICallbackWithResponse<RESPONSE> callback,
                                         @NonNull IOutgoingMessageConverter<REQUEST> requestConverter,
                                         @NonNull IIncomingMessageConverter<RESPONSE> responseConverter)
            throws IOException;

    /**
     * Asynchronously sends the specified command to its destination.
     *
     * @param command          the command to send.
     * @param callback         a callback object that will receive confirmation when the command has
     *                         been processed, or {@code null} to just "fire and forget".
     * @param commandConverter a converter that knows how to convert the command POJO to a RabbitMQ message.
     * @throws IOException if something goes wrong while sending the command.
     */
    <COMMAND> void sendCommand(@NonNull COMMAND command,
                               @Nullable ICallbackWithoutResponse callback,
                               @NonNull IOutgoingMessageConverter<COMMAND> commandConverter)
            throws IOException;
}
