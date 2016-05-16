package net.pkhapps.dart.messaging.amqp;

import com.rabbitmq.client.Channel;
import net.pkhapps.dart.messaging.facade.DestinationMissingException;
import net.pkhapps.dart.messaging.facade.MessagingFacade;
import net.pkhapps.dart.messaging.facade.ResponseCallback;
import net.pkhapps.dart.messaging.facade.SendFailureException;
import net.pkhapps.dart.messaging.messages.*;
import net.pkhapps.dart.messaging.converters.MessageConverterException;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Objects;

public class AmqpMessagingFacade implements MessagingFacade {

    @Override
    public void fireAndForget(@NotNull FireAndForgetCommand command)
            throws SendFailureException, MessageConverterException {
        Objects.requireNonNull(command, "command cannot be null");
        fireAndForget(command, getStaticDestination(command));
    }

    @Override
    public void fireAndForget(@NotNull FireAndForgetCommand command, @NotNull Destination destination)
            throws SendFailureException, MessageConverterException {
        Objects.requireNonNull(command, "command cannot be null");
        Objects.requireNonNull(destination, "destination cannot be null");
        fireAndForget(command, Collections.singleton(destination));
    }

    @Override
    public void fireAndForget(@NotNull FireAndForgetCommand command, @NotNull Iterable<Destination> destinations)
            throws SendFailureException, MessageConverterException {
        Objects.requireNonNull(command, "command cannot be null");
        Objects.requireNonNull(destinations, "destinations cannot be null");
        byte[] message = convertMessageToJSON(command).getBytes();
        for (Destination destination : destinations) {
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
    protected Destination getStaticDestination(@NotNull Message message) throws DestinationMissingException {
        final StaticDestination staticDestination = message.getClass().getAnnotation(StaticDestination.class);
        if (staticDestination == null) {
            throw new DestinationMissingException("No @StaticDestination annotation found on command class");
        }
        return new Destination() {
            @Override
            public String getExchange() {
                return staticDestination.exchange();
            }

            @Override
            public String getRoutingKey() {
                return staticDestination.routingKey();
            }
        };
    }

    protected void sendToDestination(@NotNull byte[] message, @NotNull Destination destination) throws SendFailureException {

    }

    protected Channel getChannelForMessage(Message message) throws SendFailureException {
        throw new SendFailureException();
    }
}
