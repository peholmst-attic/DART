package net.pkhapps.dart.base.messaging.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rabbitmq.client.AMQP;
import net.pkhapps.dart.base.messaging.annotation.MessageType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Implementation of {@link MessageSerializer} that uses a Jackson {@link ObjectMapper} to serialize the data into JSON.
 */
public class JacksonMessageSerializer implements MessageSerializer {

    private final ObjectMapper objectMapper;

    /**
     * Creates a new {@code JacksonMessageSerializer} with an internal {@link ObjectMapper}.
     */
    public JacksonMessageSerializer() {
        this(new ObjectMapper()
                .findAndRegisterModules()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
    }

    /**
     * Creates a new {@code JacksonMessageSerializer} with the specified {@link ObjectMapper}.
     *
     * @param objectMapper the object mapper to use.
     */
    public JacksonMessageSerializer(@NotNull ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper);
    }

    @Override
    @NotNull
    public byte[] serialize(@Nullable Object data, @NotNull AMQP.BasicProperties.Builder propertiesBuilder)
            throws IOException {
        Objects.requireNonNull(propertiesBuilder);
        if (data == null) {
            return new byte[0];
        } else {
            byte[] buf = objectMapper.writer().writeValueAsBytes(data);
            propertiesBuilder.contentType("application/json");
            propertiesBuilder.contentEncoding(StandardCharsets.UTF_8.toString());
            if (data.getClass().isAnnotationPresent(MessageType.class)) {
                propertiesBuilder.type(data.getClass().getAnnotation(MessageType.class).value());
            }
            return buf;
        }
    }
}
