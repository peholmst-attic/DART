package net.pkhapps.dart.modules.dispatch.domain.base;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Optional;

/**
 * A class for providing contextual information to domain classes that are not managed by a Spring application context.
 * Classes that are managed by Spring should get their contextual information through dependency injection.
 */
public abstract class DomainContext {

    // TODO Replace with some kind of repository hook that fills in the necessary information just before save.

    /**
     * Returns the current instant.
     */
    public abstract @NotNull Instant now();

    /**
     * Returns the ID of the current user, if available.
     */
    public abstract @NotNull Optional<String> currentUserId();

    /**
     * Default implementation of {@link DomainContext}.
     */
    public static class DefaultDomainContext extends DomainContext {

        /**
         * {@inheritDoc}
         * <p>
         * This implementation calls {@link Instant#now()}.
         */
        @Override
        public @NotNull Instant now() {
            return Instant.now();
        }

        /**
         * {@inheritDoc}
         * <p>
         * This implementation always returns an empty {@code Optional}.
         */
        @Override
        public @NotNull Optional<String> currentUserId() {
            return Optional.empty();
        }
    }

    private static DomainContext INSTANCE;

    /**
     * Sets the {@code DomainContext} singleton instance to use.
     *
     * @param domainContext the domain context instance. If {@code null}, a {@link DefaultDomainContext} will be
     *                      created and used.
     */
    public static void setInstance(@Nullable DomainContext domainContext) {
        if (domainContext == null) {
            INSTANCE = new DefaultDomainContext();
        } else {
            INSTANCE = domainContext;
        }
    }

    /**
     * Returns the {@code DomainContext} singleton instance.
     */
    public static @NotNull DomainContext getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DefaultDomainContext();
        }
        return INSTANCE;
    }
}
