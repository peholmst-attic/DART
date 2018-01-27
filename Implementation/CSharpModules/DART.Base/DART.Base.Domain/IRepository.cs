using MongoDB.Bson;

namespace DART.Base.Domain
{
    /// <summary>
    /// Interface defining a repository of aggregate roots of a specific type. Repositories are used to find, save and delete aggregate roots.
    /// </summary>
    /// <typeparam name="T">The type of aggregate roots stored in the repository.</typeparam>
    public interface IRepository<T> where T : AggregateRoot
    {
        /// <summary>
        /// Finds the aggregate root with the specified ID.
        /// </summary>
        /// <param name="id">The ID to look for (never null).</param>
        /// <returns>The aggregate root or null if not found.</returns>
        T Find(ObjectId id);

        /// <summary>
        /// Saves the specified aggregate root into the repository, overwriting any existing version.
        /// </summary>
        /// <param name="aggregateRoot">the aggregate root to save (never null).</param>
        void Save(T aggregateRoot);
    }
}
