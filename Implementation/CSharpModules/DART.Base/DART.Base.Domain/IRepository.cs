using JetBrains.Annotations;

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
        [CanBeNull] T FindById([NotNull] string id);

        /// <summary>
        /// Saves the specified aggregate into the repository, overwriting any existing version.
        /// </summary>
        /// <param name="aggregate">the aggregate to save (never null).</param>
        /// <returns>A copy of the aggregate instance with updated metadata. The existing instance remains untouched.</returns>
        /// <exception cref="OptimisticLockingFailureException">If the aggregate has been modified or deleted by another client after it was retrieved from the repository.</exception>
        [NotNull] T Save([NotNull] T aggregate);

        /// <summary>
        /// Deletes the aggregate from the repository.
        /// </summary>
        /// <param name="id">The ID of the aggregate to delete (never null).</param>
        /// <exception cref="NotSupportedException">If the repository does not support deletions.</exception>
        /// <exception cref="AggregateNotFoundException">If the aggregate does not exist.</exception>
        void Delete([NotNull] string id);
    }
}
