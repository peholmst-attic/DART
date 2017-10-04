package net.pkhapps.dart.modules.dispatch.domain.base;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Persistable;

/**
 * Interface to be implemented by {@link Persistable}s that can be "softly" deleted, meaning they are not actually
 * removed, only marked as deleted / deactivated.
 */
public interface SoftDeletable extends Persistable<ObjectId> {

    /**
     * Returns whether this entity is active (nondeleted) or not (deleted). This is the opposite of
     * {@link #isDeleted()}.
     */
    boolean isActive();

    /**
     * Returns whether this entity is deleted (deactivated) or not (active). This is the opposite of
     * {@link #isActive()}.
     */
    default boolean isDeleted() {
        return !isActive();
    }

    /**
     * Activates this entity. If the entity is already active, nothing happens.
     */
    void activate();

    /**
     * Deactivates (deletes) this entity. If the entity is already deactivated, nothing happens.
     */
    void deactivate();
}
