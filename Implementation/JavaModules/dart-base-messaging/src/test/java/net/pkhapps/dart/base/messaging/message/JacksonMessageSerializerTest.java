package net.pkhapps.dart.base.messaging.message;

import com.rabbitmq.client.AMQP;
import net.pkhapps.dart.base.messaging.annotation.MessageType;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link JacksonMessageSerializer}.
 */
public class JacksonMessageSerializerTest {

    private MessageSerializer messageSerializer;
    private AMQP.BasicProperties.Builder builder;

    @Before
    public void setUp() {
        // We're using the internal object mapper for testing
        messageSerializer = new JacksonMessageSerializer();
        builder = new AMQP.BasicProperties.Builder();
    }

    @Test
    public void serialize_nullData_emptyByteArrayReturned() throws IOException {
        byte[] bytes = messageSerializer.serialize(null, builder);
        AMQP.BasicProperties props = builder.build();
        assertThat(bytes).isEmpty();
        assertThat(props.getContentEncoding()).isNull();
        assertThat(props.getContentType()).isNull();
        assertThat(props.getType()).isNull();
    }

    @Test
    public void serialize_nonNullWithoutType_jsonByteArrayAndPropertiesReturned() throws IOException {
        MyPojo data = new MyPojo();
        byte[] bytes = messageSerializer.serialize(data, builder);
        AMQP.BasicProperties props = builder.build();
        assertMyPojo(props, bytes);
        assertThat(props.getType()).isNull();
    }

    @Test
    public void serialize_nonNullWithType_jsonByteArrayAndPropertiesReturned() throws IOException {
        MyPojoWithType data = new MyPojoWithType();
        byte[] bytes = messageSerializer.serialize(data, builder);
        AMQP.BasicProperties props = builder.build();
        assertMyPojo(props, bytes);
        assertThat(props.getType()).isEqualTo("my-pojo");
    }

    private void assertMyPojo(AMQP.BasicProperties props, byte[] bytes) {
        assertThat(new String(bytes)).isEqualTo("{\"myString\":\"hello world\",\"myInteger\":1234,\"myTimestamp\":\"2018-01-21T19:32:15Z\"}");
        assertThat(props.getContentEncoding()).isEqualTo(StandardCharsets.UTF_8.toString());
        assertThat(props.getContentType()).isEqualTo("application/json");
    }

    public static class MyPojo {
        public String myString = "hello world";
        public int myInteger = 1234;
        public Instant myTimestamp = ZonedDateTime.of(2018, 1, 21,
                19, 32, 15, 0,
                ZoneId.of("UTC")).toInstant();
    }

    @MessageType("my-pojo")
    public static class MyPojoWithType extends MyPojo {
    }
}
