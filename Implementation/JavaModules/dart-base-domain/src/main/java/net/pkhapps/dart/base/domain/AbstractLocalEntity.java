package net.pkhapps.dart.base.domain;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * Base class for local entities that are owned and managed by an {@link AbstractAggregateRoot aggregate root}.
 */
public abstract class AbstractLocalEntity implements Serializable {

    private Long id;

    /**
     * Default constructor used by Spring Data only.
     */
    protected AbstractLocalEntity() {
    }

    /**
     * Creates a new {@link AbstractLocalEntity}.
     *
     * @param aggregateRoot the aggregate root that owns the entity. It will be used to generate the entity ID.
     * @see AbstractAggregateRoot#getNextFreeLocalId()
     */
    protected AbstractLocalEntity(@NotNull AbstractAggregateRoot aggregateRoot) {
        Objects.requireNonNull(aggregateRoot, "aggregateRoot must not be null");
        id = aggregateRoot.getNextFreeLocalId();
    }

    /**
     * Copy constructor
     *
     * @param original the original local entity to copy.
     */
    protected AbstractLocalEntity(@NotNull AbstractLocalEntity original) {
        Objects.requireNonNull(original, "original must not be null");
        this.id = original.id;
    }

    /**
     * Returns the ID of this entity. The ID is unique within the aggregate root that contains the entity.
     */
    public @NotNull Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AbstractLocalEntity that = (AbstractLocalEntity) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
