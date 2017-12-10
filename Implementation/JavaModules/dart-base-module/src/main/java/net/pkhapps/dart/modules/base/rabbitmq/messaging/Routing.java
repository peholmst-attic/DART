package net.pkhapps.dart.modules.base.rabbitmq.messaging;

/**
 * TODO Document me!
 */
public @interface Routing {

    String exchange() default "";

    String routingKey();
}
