package net.pkhapps.dart.messaging.common.messages.converters;

public interface MessageParser {

    Object parseJson(String jsonString) throws MessageConverterException;
}
