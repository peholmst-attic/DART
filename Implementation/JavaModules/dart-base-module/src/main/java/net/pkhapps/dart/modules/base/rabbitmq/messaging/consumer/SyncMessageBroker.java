package net.pkhapps.dart.modules.base.rabbitmq.messaging.consumer;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * TODO Document me
 */
public interface SyncMessageBroker {

    <REQUEST, RESPONSE> RESPONSE sendRequest(@NotNull REQUEST request,
                                             @NotNull OutgoingMessageConverter<REQUEST> requestConverter,
                                             @NotNull IncomingMessageConverter<RESPONSE> responseConverter)
            throws IOException, ApplicationException, TimeoutException;

    <COMMAND> void sendCommand(@NotNull COMMAND command,
                               @NotNull OutgoingMessageConverter<COMMAND> commandConverter)
            throws IOException, ApplicationException, TimeoutException;
}
