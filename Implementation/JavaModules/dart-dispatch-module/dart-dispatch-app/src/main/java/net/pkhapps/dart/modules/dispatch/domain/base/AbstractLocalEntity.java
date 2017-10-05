package net.pkhapps.dart.modules.dispatch.domain.base;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * TODO Document me
 */
public abstract class AbstractLocalEntity implements Serializable {

    private Long id;

    /**
     * Default constructor used by Spring Data only.
     */
    protected AbstractLocalEntity() {
    }

    /**
     * @param aggregateRoot
     */
    protected AbstractLocalEntity(@NotNull AbstractAggregateRoot aggregateRoot) {
        Objects.requireNonNull(aggregateRoot, "aggregateRoot must not be null");
        id = aggregateRoot.getNextFreeLocalId();
    }

    /**
     * @param original
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
