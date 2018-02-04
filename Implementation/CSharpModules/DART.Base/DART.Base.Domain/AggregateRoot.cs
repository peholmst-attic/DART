using JetBrains.Annotations;
using MongoDB.Bson.Serialization.Attributes;
using MongoDB.Bson.Serialization.IdGenerators;
using System;

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

        [BsonElement("_opt_lock_version")]
        public long? Version { get; protected set; } = null;

        [BsonElement("_created")]
        public DateTime? Created { get; protected set; } = null;

        [BsonElement("_last_modified")]
        public DateTime? LastModified { get; protected set; } = null;

        /// <summary>
        /// The ID that uniquely identifies this aggregate root.
        /// </summary>
        [BsonId(IdGenerator = typeof(StringObjectIdGenerator))]
        public string Id { get; protected set; }

        /// <summary>
        /// Creates a shallow copy of the aggregate that is ready to be saved by a repository. The copy has updated
        /// timestamps and an increased optimistic locking version number. Clients should normally not need to access this directly.
        /// </summary>
        /// <param name="domainContext">The domain context to use to get the current date and time. If null, <seealso cref="DateTime.Now"/> is used instead.</param>
        /// <returns>A shallow modified copy of this aggregate.</returns>
        [NotNull]
        internal AggregateRoot CopyForSaving([CanBeNull] IDomainContext domainContext = null)
        {
            // TODO Use a DomainContext to get session info
            var copy = (AggregateRoot)MemberwiseClone();
            var now = (domainContext != null ? domainContext.CurrentDateTime : DateTime.Now);
            if (copy.Created == null)
            {
                copy.Created = now;
            }
            copy.LastModified = now;
            if (copy.Version == null)
            {
                copy.Version = 1;
            }
            else
            {
                copy.Version++;
            }
            return copy;
        }

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

        public override bool Equals(object obj)
        {
            if (obj == null || GetType() != obj.GetType())
            {
                return false;
            }

            if (obj == this)
            {
                return true;
            }

            return Id != null && Id.Equals((obj as AggregateRoot).Id);
        }

        public override int GetHashCode()
        {
            return Id == null ? base.GetHashCode() : Id.GetHashCode();
        }
    }
}
