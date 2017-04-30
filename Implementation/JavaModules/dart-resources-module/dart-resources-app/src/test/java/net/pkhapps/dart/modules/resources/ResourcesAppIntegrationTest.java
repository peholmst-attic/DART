package net.pkhapps.dart.modules.resources;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import net.pkhapps.dart.modules.base.rabbitmq.event.RabbitMQConnectionOpened;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.consumer.DefaultAsyncMessageBroker;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.consumer.DefaultSyncMessageBroker;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.consumer.IncomingMessageConverter;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.consumer.OutgoingMessageConverter;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.consumer.util.DefaultIncomingMessageConverter;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.consumer.util.DefaultOutgoingMessageConverter;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.consumer.util.MessageRouter;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.util.JaxbMessageConverter;
import net.pkhapps.dart.modules.base.rabbitmq.messaging.util.MessageConverter;
import net.pkhapps.dart.modules.resources.integration.rabbitmq.Routing;
import net.pkhapps.dart.modules.resources.integration.xsd.Command;
import net.pkhapps.dart.modules.resources.integration.xsd.ObjectFactory;
import net.pkhapps.dart.modules.resources.integration.xsd.Request;
import net.pkhapps.dart.modules.resources.integration.xsd.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static org.mockito.Mockito.mock;

/**
 * Simple integration test for the DART Resources App. For this test to work, the database must be setup and running,
 * RabbitMQ must be running, DART Accounts App must be running and DART Resources App must be running.
 */
public class ResourcesAppIntegrationTest {

    private DefaultAsyncMessageBroker asyncMessageBroker;
    private DefaultSyncMessageBroker syncMessageBroker;
    private IncomingMessageConverter<Response> responseMessageConverter;
    private OutgoingMessageConverter<Request> requestMessageConverter;
    private OutgoingMessageConverter<Command> commandMessageConverter;
    private final MessageRouter messageRouter = new MessageRouter() {

        @Override
        @Nullable
        public String getRoutingKey(@NotNull Object message) {
            if (message instanceof Command) {
                return Routing.COMMANDS_ROUTING_KEY;
            } else if (message instanceof Request) {
                return Routing.REQUESTS_ROUTING_KEY;
            }
            return null;
        }

        @Override
        @Nullable
        public String getExchange(@NotNull Object message) {
            return Routing.EXCHANGE;
        }
    };
    private RabbitMQProperties properties;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        MessageConverter messageConverter = new JaxbMessageConverter(
                JAXBContext.newInstance(ObjectFactory.class.getPackage().getName(), getClass().getClassLoader()));
        responseMessageConverter = new DefaultIncomingMessageConverter<>(messageConverter, Response.class);
        requestMessageConverter = new DefaultOutgoingMessageConverter<>(messageConverter, messageRouter);
        commandMessageConverter = new DefaultOutgoingMessageConverter<>(messageConverter, messageRouter);

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        properties = mock(RabbitMQProperties.class);

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername("integration-test");
        connectionFactory.setPassword("password");
        connectionFactory.setVirtualHost("dart");

        connection = connectionFactory.newConnection();

        asyncMessageBroker = new DefaultAsyncMessageBroker(executorService, properties);
        asyncMessageBroker.onRabbitMQConnectionOpened(new RabbitMQConnectionOpened(connection));

        syncMessageBroker = new DefaultSyncMessageBroker(asyncMessageBroker);
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }

    @Test
    public void findResources() throws Exception {
        Response response = syncMessageBroker
                .sendRequest(new FindResourcesRequest(), requestMessageConverter, responseMessageConverter);
    }

    public void findStatusDescriptors() {

    }
}
