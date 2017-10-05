package net.pkhapps.dart.modules.dispatch.domain.base;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.Persistable;
import org.springframework.util.ClassUtils;

import java.util.Objects;

/**
 * Base class for aggregate roots.
 *
 * @see AbstractEventSourcedAggregateRoot
 */
public abstract class AbstractAggregateRoot extends org.springframework.data.domain.AbstractAggregateRoot
        implements Persistable<ObjectId> {

    @Id
    private ObjectId id;

    @Version
    private Long version;

    private Long nextFreeLocalId;

    /**
     * Default constructor.
     */
    public AbstractAggregateRoot() {
    }

    /**
     * Copy constructor.
     *
     * @param original the original aggregate root to copy.
     */
    public AbstractAggregateRoot(@NotNull AbstractAggregateRoot original) {
        Objects.requireNonNull(original, "original must not be null");
        this.id = original.id;
        this.version = original.version;
        this.nextFreeLocalId = original.nextFreeLocalId;
    }

    /**
     * Returns the next free local ID to be used by a {@link AbstractLocalEntity}. This method never returns the same
     * value twice when invoked on the same object instance.
     */
    Long getNextFreeLocalId() {
        if (nextFreeLocalId == null) {
            nextFreeLocalId = 0L;
        }
        return nextFreeLocalId++;
    }

    @Override
    public @Nullable ObjectId getId() {
        return id;
    }

    /**
     * Sets the ID of this aggregate root. The ID is normally set by repositories, which is why this method
     * is protected.
     *
     * @param id the ID to set.
     */
    protected void setId(@Nullable ObjectId id) {
        this.id = id;
    }

    /**
     * Returns the version number used for optimistic locking.
     *
     * @return the version or {@code null} if the aggregate root has not been persisted yet.
     */
    public @Nullable Long getVersion() {
        return version;
    }

    /**
     * Sets the optimistic locking version number of this aggregate root. The version number is normally set by
     * repositories, which is why this method is protected.
     *
     * @param version the version to set.
     */
    protected void setVersion(@Nullable Long version) {
        this.version = version;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    @Override
    public String toString() {
        return String.format("%s[id=%s, version=%d]", getClass().getSimpleName(), getId(), getVersion());
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(ClassUtils.getUserClass(obj))) {
            return false;
        }
        AbstractAggregateRoot that = (AbstractAggregateRoot) obj;
        return null != this.getId() && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode += null == getId() ? 0 : getId().hashCode() * 31;
        return hashCode;
    }
}
