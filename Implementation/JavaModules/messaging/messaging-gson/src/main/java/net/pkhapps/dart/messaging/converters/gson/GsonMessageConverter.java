package net.pkhapps.dart.messaging.converters.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.pkhapps.dart.messaging.messages.Message;
import net.pkhapps.dart.messaging.converters.MessageConverter;
import net.pkhapps.dart.messaging.converters.MessageConverterException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class GsonMessageConverter<M extends Message, B extends Message.Builder<M, B>> implements MessageConverter<M> {

    @NotNull
    @Override
    public String toJson(@NotNull M message) throws MessageConverterException {
        Objects.requireNonNull(message, "message cannot be null");
        return null;
    }

    @Override
    public boolean supportsJson(@NotNull Object parsedJson) {
        Objects.requireNonNull(parsedJson, "parsedJson cannot be null");
        JsonObject json = castToJsonObject(parsedJson);

        JsonObject header = json.getAsJsonObject("header");
        if (header == null) {
            return false;
        }

        JsonElement type = header.get("type");
        JsonElement version = header.get("version");
        if (type == null || version == null) {
            return false;
        }

        return supports(type.getAsString(), version.getAsString());
    }

    @NotNull
    @Override
    public M fromJson(@NotNull Object parsedJson) throws MessageConverterException {
        Objects.requireNonNull(parsedJson, "parsedJson cannot be null");
        return fromJson(castToJsonObject(parsedJson));
    }

    private JsonObject castToJsonObject(Object parsedJson) {
        if (parsedJson instanceof JsonObject) {
            return (JsonObject) parsedJson;
        } else {
            throw new IllegalArgumentException("Only JsonObjects are supported by this converter");
        }
    }

    protected abstract boolean supports(String type, String version);

    protected abstract M fromJson(JsonObject jsonObject) throws MessageConverterException;

    protected abstract B createMessageBuilder();

}
