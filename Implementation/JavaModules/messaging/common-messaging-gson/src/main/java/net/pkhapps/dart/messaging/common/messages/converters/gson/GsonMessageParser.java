package net.pkhapps.dart.messaging.common.messages.converters.gson;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import net.pkhapps.dart.messaging.common.messages.converters.MessageConverterException;
import net.pkhapps.dart.messaging.common.messages.converters.MessageParser;

public class GsonMessageParser implements MessageParser {

    private final JsonParser parser = new JsonParser();

    @Override
    public Object parseJson(String jsonString) throws MessageConverterException {
        try {
            return parser.parse(jsonString).getAsJsonObject();
        } catch (JsonParseException ex) {
            throw new MessageConverterException("The JSON string could not be parsed", ex);
        }
    }
}
