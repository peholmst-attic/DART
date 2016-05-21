package net.pkhapps.dart.messaging.converters.gson;

import com.google.gson.JsonObject;
import net.pkhapps.dart.messaging.converters.MessageConverter;
import net.pkhapps.dart.messaging.converters.MessageConverterException;
import net.pkhapps.dart.messaging.messages.Message;
import org.jetbrains.annotations.NotNull;

public abstract class GsonMessageConverter<M extends Message> implements MessageConverter<M, JsonObject> {

    @NotNull
    @Override
    public JsonObject marshal(@NotNull M message) throws MessageConverterException {
        return null;
    }

    @NotNull
    @Override
    public M unmarshal(@NotNull JsonObject data) throws MessageConverterException {
        return null;
    }

    @NotNull
    protected abstract M fromJson(@NotNull JsonObject jsonObject) throws MessageConverterException;

    @NotNull
    protected abstract JsonObject toJson(@NotNull M message) throws MessageConverterException;
}
