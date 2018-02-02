using MongoDB.Bson;

namespace DART.Base.Domain
{
    /// <summary>
    /// Interface defining a repository of aggregates of a specific type. Repositories are used to find, save and delete aggregates.
    /// </summary>
    /// <typeparam name="T">The aggregate root type.</typeparam>
    public interface IRepository<T> where T : AggregateRoot
    {
        /// <summary>
        /// Finds the aggregate with the specified ID.
        /// </summary>
        /// <param name="id">The ID to look for (never null).</param>
        /// <returns>The aggregate or null if not found.</returns>
        T Find(ObjectId id);

        /// <summary>
        /// Saves the specified aggregate into the repository, overwriting any existing version.
        /// </summary>
        /// <param name="aggregate">the aggregate to save (never null).</param>
        void Save(T aggregate);

        /// <summary>
        /// Deletes the aggregate from the repository. If the aggregate does not exist, nothing happens.
        /// </summary>
        /// <param name="id">The ID of the aggregate to delete (never null).</param>
        /// <exception cref="NotSupportedException">If the repository does not support deletions.</exception>
        void Delete(ObjectId id);
    }
}
