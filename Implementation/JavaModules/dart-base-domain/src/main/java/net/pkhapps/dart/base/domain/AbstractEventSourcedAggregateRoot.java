package net.pkhapps.dart.base.domain;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Base class for aggregate roots that use event sourcing.
 */
public abstract class AbstractEventSourcedAggregateRoot extends AbstractAggregateRoot implements Auditable {

    private List<ActionRecord> actionRecords;

    @Indexed
    private Instant created;

    @Indexed
    private Instant lastModified;

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
        original.actionRecords.forEach(r -> r.getAction().perform(this));
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
     *
     * @throws IllegalStateException if the action cannot be performed at this time.
     * @see Action#canPerform(AbstractEventSourcedAggregateRoot)
     * @see Action#willChangeState(AbstractEventSourcedAggregateRoot)
     */
    protected void performAction(@NotNull Action action) {
        Objects.requireNonNull(action, "action must not be null");
        if (!action.canPerform(this)) {
            throw new IllegalStateException("The action cannot be performed at this time");
        }
        if (!action.willChangeState(this)) {
            return; // This action will not actually do anything so silently ignore it
        }
        action.perform(this);
        ActionRecord record = new ActionRecord(actionRecords.size(), DomainContext.getInstance().now(),
                DomainContext.getInstance().currentUserId().orElse(null), action);

        // Update date time indexes (for sorting)
        if (actionRecords.isEmpty()) {
            created = record.getTimestamp();
        }
        lastModified = record.getTimestamp();

        // Store record
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
     * Interface representing an action that can be performed on an aggregate root to change its state. Actions
     * are immutable once created and can be performed on any aggregate root using the
     * {@link AbstractEventSourcedAggregateRoot#performAction(Action)} method.
     *
     * @see AbstractAction
     */
    public interface Action extends Serializable {

        /**
         * Performs the action on the specified aggregate root. When this method is called, both
         * {@link #willChangeState(AbstractEventSourcedAggregateRoot)} and
         * {@link #canPerform(AbstractEventSourcedAggregateRoot)} have already been checked and returned true.
         *
         * @param aggregateRoot the aggregate root that the action will be performed on.
         */
        void perform(@NotNull AbstractEventSourcedAggregateRoot aggregateRoot);

        /**
         * Checks if this action can be performed at this time on the specified aggregate root. If an action cannot
         * be performed, an exception will be thrown if the action is performed anyway.
         *
         * @param aggregateRoot the aggregate root that the action will be performed on.
         * @return true if the action can be performed (default), false if not.
         */
        default boolean canPerform(@NotNull AbstractEventSourcedAggregateRoot aggregateRoot) {
            return true;
        }

        /**
         * Checks if this action will actually change the state of the specified aggregate root. If no state will be
         * changed, the action will be silently ignored. This situation could for example occur when you try to
         * perform the exact same action twice in a row.
         * <p>
         * When this method is called, {@link #canPerform(AbstractEventSourcedAggregateRoot)} has already been
         * checked and returned
         * true.
         *
         * @param aggregateRoot the aggregate root that the action will be performed on.
         * @return true if the action changes the state (default), false if not.
         */
        default boolean willChangeState(@NotNull AbstractEventSourcedAggregateRoot aggregateRoot) {
            return true;
        }
    }

    /**
     * Base class for {@link Action} that is parameterizable so that the implementation does not need to cast the
     * aggregate root to the proper type.
     */
    public static abstract class AbstractAction<A extends AbstractEventSourcedAggregateRoot> implements Action {

        /**
         * Invoked by {@link #perform(AbstractEventSourcedAggregateRoot)} to actually perform the action.
         */
        protected abstract void doPerform(@NotNull A aggregateRoot);

        @Override
        @SuppressWarnings("unchecked")
        public void perform(@NotNull AbstractEventSourcedAggregateRoot aggregateRoot) {
            doPerform((A) aggregateRoot);
        }

        @Override
        public String toString() {
            return getClass().getSimpleName();
        }
    }

    /**
     * An action record represents an action that has been performed and includes information about when and by whom
     * the action was performed.
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
