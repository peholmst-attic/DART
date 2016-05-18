package net.pkhapps.dart.messaging.amqp;

import com.rabbitmq.client.Channel;
import net.pkhapps.dart.messaging.amqp.routing.AmqpDestination;
import net.pkhapps.dart.messaging.amqp.routing.StaticAmqpDestination;
import net.pkhapps.dart.messaging.converters.MessageConverterException;
import net.pkhapps.dart.messaging.facade.DestinationMissingException;
import net.pkhapps.dart.messaging.facade.MessagingFacade;
import net.pkhapps.dart.messaging.facade.ResponseCallback;
import net.pkhapps.dart.messaging.facade.SendFailureException;
import net.pkhapps.dart.messaging.messages.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AmqpMessagingFacade implements MessagingFacade<AmqpDestination> {

    @Override
    public void fireAndForget(@NotNull FireAndForgetCommand command)
            throws SendFailureException, MessageConverterException {
        Objects.requireNonNull(command, "command cannot be null");
        fireAndForget(command, getStaticDestination(command));
    }

    @Override
    public void fireAndForget(@NotNull FireAndForgetCommand command, @NotNull Iterable<AmqpDestination> destinations)
            throws SendFailureException, MessageConverterException {
        Objects.requireNonNull(command, "command cannot be null");
        Objects.requireNonNull(destinations, "destinations cannot be null");
        byte[] message = convertMessageToJSON(command).getBytes();
        for (AmqpDestination destination : destinations) {
            sendToDestination(message, destination);
        }
    }

    @Override
    public <REQ extends Request<RESP>, RESP extends Response> void query(REQ request, ResponseCallback<RESP> responseCallback) throws SendFailureException, MessageConverterException {

    }

    @Override
    public void broadcast(@NotNull Broadcast message) throws SendFailureException, MessageConverterException {

    }

    protected String convertMessageToJSON(Message message) throws MessageConverterException {
        throw new MessageConverterException();
    }


    @NotNull
    protected AmqpDestination getStaticDestination(@NotNull Message message) throws DestinationMissingException {
        final StaticAmqpDestination staticAmqpDestination = message.getClass().getAnnotation(StaticAmqpDestination.class);
        if (staticAmqpDestination == null) {
            throw new DestinationMissingException("No @StaticDestination annotation found on command class");
        }
        return new AmqpDestination(staticAmqpDestination.exchange(), staticAmqpDestination.routingKey());
    }

    protected void sendToDestination(@NotNull byte[] message, @NotNull AmqpDestination destination) throws SendFailureException {

    }

    protected Channel getChannelForMessage(Message message) throws SendFailureException {
        throw new SendFailureException();
    }
}
