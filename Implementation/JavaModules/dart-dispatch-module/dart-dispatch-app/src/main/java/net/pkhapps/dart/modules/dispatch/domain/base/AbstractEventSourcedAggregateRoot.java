package net.pkhapps.dart.modules.dispatch.domain.base;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Base class for aggregate roots that use event sourcing.
 */
public abstract class AbstractEventSourcedAggregateRoot extends AbstractAggregateRoot implements Auditable {

    private List<ActionRecord> actionRecords;

    /**
     * Default constructor.
     */
    public AbstractEventSourcedAggregateRoot() {
        actionRecords = new ArrayList<>();
    }

    /**
     * Copy constructor.
     *
     * @param original the original aggregate root to copy.
     */
    public AbstractEventSourcedAggregateRoot(@NotNull AbstractEventSourcedAggregateRoot original) {
        super(original);
        // Replay all the actions to bring the state of the copied aggregate root up-to-date.
        original.actionRecords.forEach(r -> r.getAction().apply(this));
        // Copy the records
        actionRecords = original.actionRecords.stream().map(ActionRecord::copy).collect(Collectors.toList());
    }

    /**
     * Returns all the actions that have been performed on this aggregate root, from the first to the latest.
     */
    public @NotNull List<ActionRecord> getActionRecords() {
        return Collections.unmodifiableList(actionRecords);
    }

    /**
     * Performs the given {@code action} and records it. Clients should never use this method directly. Instead, they
     * should invoke business methods on the aggregate root, that in turn perform the actions.
     */
    protected void performAction(@NotNull Action action) {
        Objects.requireNonNull(action, "action must not be null");
        action.apply(this);
        ActionRecord record = new ActionRecord(actionRecords.size(), DomainContext.getInstance().now(),
                DomainContext.getInstance().currentUserId().orElse(null), action);
        actionRecords.add(record);
    }

    @Override
    public @NotNull Optional<Instant> getCreated() {
        return actionRecords.stream().findFirst().map(ActionRecord::getTimestamp);
    }

    @Override
    public @NotNull Optional<String> getCreatedByUserId() {
        return actionRecords.stream().findFirst().flatMap(ActionRecord::getUserId);
    }

    @Override
    public @NotNull Optional<Instant> getLastModified() {
        if (actionRecords.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(actionRecords.get(actionRecords.size() - 1).getTimestamp());
        }
    }

    @Override
    public @NotNull Optional<String> getLastModifiedByUserId() {
        if (actionRecords.isEmpty()) {
            return Optional.empty();
        } else {
            return actionRecords.get(actionRecords.size() - 1).getUserId();
        }
    }

    /**
     * TODO Document me!
     */
    public interface Action extends Serializable {

        /**
         * @param aggregateRoot
         */
        void apply(@NotNull AbstractEventSourcedAggregateRoot aggregateRoot);

        /**
         * @param locale
         * @return
         */
        default @NotNull String getDescription(@Nullable Locale locale) {
            return getClass().getSimpleName();
        }
    }

    /**
     * TODO Document me!
     *
     * @param <A>
     */
    public static abstract class AbstractAction<A extends AbstractEventSourcedAggregateRoot> implements Action {

        @Transient
        private final Class<A> aggregateRootClass;

        @SuppressWarnings("unchecked")
        public AbstractAction() {
            aggregateRootClass =
                    (Class<A>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }

        /**
         * @param aggregateRoot
         */
        protected abstract void doApply(@NotNull A aggregateRoot);

        @Override
        public void apply(@NotNull AbstractEventSourcedAggregateRoot aggregateRoot) {
            doApply(aggregateRootClass.cast(aggregateRoot));
        }

        @Override
        public String toString() {
            return getClass().getSimpleName();
        }
    }

    /**
     * TODO Document me
     *
     * @see DomainContext
     */
    public static class ActionRecord implements Serializable {

        private final Integer id;
        private final Instant timestamp;
        private final String userId;
        private final Action action;

        @PersistenceConstructor
        ActionRecord(@NotNull Integer id, @NotNull Instant timestamp, @Nullable String userId,
                     @NotNull Action action) {
            this.id = Objects.requireNonNull(id, "id must not be null");
            this.timestamp = Objects.requireNonNull(timestamp, "timestamp must not be null");
            this.userId = userId;
            this.action = Objects.requireNonNull(action, "action must not be null");
        }

        /**
         * Returns the action that was performed.
         */
        public @NotNull Action getAction() {
            return action;
        }

        /**
         * Returns the timestamp when this action was performed.
         */
        public @NotNull Instant getTimestamp() {
            return timestamp;
        }

        /**
         * Returns the ID that uniquely identifies this action record within its owning aggregate root.
         */
        public @NotNull Integer getId() {
            return id;
        }

        /**
         * Returns the user ID of the user who performed this action, if available.
         */
        public @NotNull Optional<String> getUserId() {
            return Optional.ofNullable(userId);
        }

        /**
         * Returns an exact copy of this {@code ActionRecord}.
         */
        @NotNull ActionRecord copy() {
            return new ActionRecord(id, timestamp, userId, action);
        }

        @Override
        public String toString() {
            return String.format("%s[id=%d, timestamp=%s, userId=%s, action=%s]", getClass().getSimpleName(), id,
                    timestamp, userId, action);
        }
    }
}
