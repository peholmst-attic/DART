package net.pkhapps.dart.messaging.converters;

public interface MessageParser {

    Object parseJson(String jsonString) throws MessageConverterException;
}
