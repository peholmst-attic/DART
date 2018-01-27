using MongoDB.Bson.Serialization.Attributes;
using MongoDB.Bson.Serialization.IdGenerators;

namespace DART.Base.Domain
{
    /// <summary>
    /// Base class for aggregate roots.
    /// </summary>
    /// <seealso cref="Entity"/>
    public abstract class AggregateRoot
    {
        [BsonElement("_next_free_local_id")]
        private long nextFreeLocalId = 1;

        /// <summary>
        /// The ID that uniquely identifies this aggregate root.
        /// </summary>
        [BsonId(IdGenerator = typeof(StringObjectIdGenerator))]
        public string Id { get; protected set; }

        /// <summary>
        /// The next free local ID. Clients should normally not need to access this directly.
        /// </summary>
        /// <seealso cref="ReturnAndIncrementNextFreeLocalId"/>
        internal long NextFreeLocalId
        {
            get
            {
                return this.nextFreeLocalId;
            }
        }

        /// <summary>
        /// Returns and increments the next free local ID. This method is used by Entities to generate local IDs for them.
        /// </summary>
        /// <returns>A new, unique ID to be used within this aggregate. This method never returns the same value twice.</returns>
        internal long ReturnAndIncrementNextFreeLocalId()
        {
            return this.nextFreeLocalId++;
        }
    }
}
