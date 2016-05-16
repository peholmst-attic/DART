package net.pkhapps.dart.messaging.converters;

import net.pkhapps.dart.messaging.messages.Message;
import org.jetbrains.annotations.NotNull;

/**
 * Created by peholmst on 15-05-2016.
 */
public class MessageConverterRegistry {

    @NotNull
    public static <M extends Message> MessageConverter<M> getConverter(@NotNull M message) throws MessageConverterException {
        throw new MessageConverterException();
    }

}
