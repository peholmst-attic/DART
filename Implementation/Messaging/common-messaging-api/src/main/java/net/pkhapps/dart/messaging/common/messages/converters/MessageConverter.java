package net.pkhapps.dart.messaging.common.messages.converters;

import net.pkhapps.dart.messaging.common.messages.Message;
import org.jetbrains.annotations.NotNull;

public interface MessageConverter<M extends Message> {

    @NotNull
    String toJson(@NotNull M message) throws MessageConverterException;

    boolean supportsJson(@NotNull Object parsedJson);

    @NotNull
    M fromJson(@NotNull Object parsedJson) throws MessageConverterException;
}
