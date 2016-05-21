package net.pkhapps.dart.messaging.converters;

import net.pkhapps.dart.messaging.messages.Message;
import org.jetbrains.annotations.NotNull;

public interface MessageConverter<M extends Message, T> {

    @NotNull T marshal(@NotNull M message) throws MessageConverterException;

    @NotNull
    M unmarshal(@NotNull T data) throws MessageConverterException;

    boolean supports(@NotNull T data);

    boolean supports(Message message);
}
