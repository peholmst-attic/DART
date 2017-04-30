package net.pkhapps.dart.modules.resources.integration.rabbitmq.command;

import net.pkhapps.dart.modules.resources.integration.xsd.Command;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.util.AnnotationLiteral;
import java.util.Objects;

/**
 * TODO Document me!
 */
class HandlerForQualifier extends AnnotationLiteral<HandlerFor> implements HandlerFor {

    private final Class<? extends Command> value;

    HandlerForQualifier(@NotNull Class<? extends Command> value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public Class<? extends Command> value() {
        return value;
    }
}
