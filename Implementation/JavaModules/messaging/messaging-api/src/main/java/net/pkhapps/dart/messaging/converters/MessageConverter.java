package net.pkhapps.dart.messaging.converters;

import net.pkhapps.dart.messaging.messages.Message;

public interface MessageConverter<M extends Message, T> {

    T marshal(M message) throws MessageConverterException;


    M unmarshal(T data) throws MessageConverterException;

    boolean supports(T data);

    boolean supports(Message message);
}
