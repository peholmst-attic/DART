package net.pkhapps.dart.messaging.amqp.routing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StaticAmqpDestination {

    String exchange() default "";

    String routingKey();
}
