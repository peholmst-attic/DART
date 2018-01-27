using MongoDB.Bson.Serialization.Attributes;

namespace DART.Base.Domain
{
    /// <summary>
    /// Base class for local entities that are a part of an aggregate.
    /// </summary>
    /// <seealso cref="AggregateRoot"/>
    public abstract class Entity
    {
        /// <summary>
        /// The local ID of this entity (never null). The ID is guaranteed to be unique within the owning aggregate.
        /// </summary>
        [BsonElement("_id")]
        public long Id { get; protected set; }

        /// <summary>
        /// Creates a new entity belonging to the specified aggregate root. The entity will receive a new, unique local ID
        /// from the aggregate root.
        /// </summary>
        /// <param name="aggregateRoot">The aggregate root that owns the entity.</param>
        public Entity(AggregateRoot aggregateRoot) => Id = aggregateRoot.ReturnAndIncrementNextFreeLocalId();

        /// <summary>
        /// Creates a new entity that is an exact (deep) copy of the specified entity.
        /// </summary>
        /// <param name="original">The original entity to copy from.</param>
        public Entity(Entity original) => Id = original.Id;

        public override bool Equals(object obj)
        {
            if (obj == null || GetType() != obj.GetType())
            {
                return false;
            }
            Entity entity = (Entity)obj;
            return Id == entity.Id;
        }

        public override int GetHashCode()
        {
            return Id.GetHashCode();
        }
    }
}
