package net.pkhapps.dart.modules.base.messaging.command;

import javax.enterprise.util.AnnotationLiteral;

/**
 * Qualifier class for the {@link HandlerFor} annotation, used for programmatic CDI lookups.
 */
class HandlerForQualifier extends AnnotationLiteral<HandlerFor> implements HandlerFor {

    private final Class<? extends Command> value;

    HandlerForQualifier(
            Class<? extends Command> value) {
        this.value = value;
    }

    @Override
    public Class<? extends Command> value() {
        return value;
    }
}
