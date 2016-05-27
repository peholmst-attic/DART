package net.pkhapps.dart.messaging.converters.gson;

import com.google.gson.JsonObject;
import net.pkhapps.dart.messaging.converters.MessageConverter;
import net.pkhapps.dart.messaging.converters.MessageConverterException;
import net.pkhapps.dart.messaging.messages.Message;

public abstract class GsonMessageConverter<M extends Message> implements MessageConverter<M, JsonObject> {

    @Override
    public JsonObject marshal(M message) throws MessageConverterException {
        return null;
    }

    @Override
    public M unmarshal(JsonObject data) throws MessageConverterException {
        return null;
    }

    protected abstract M fromJson(JsonObject jsonObject) throws MessageConverterException;

    protected abstract JsonObject toJson(M message) throws MessageConverterException;
}
