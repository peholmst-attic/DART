using JetBrains.Annotations;
using System;

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

    /// <summary>
    /// Exception thrown when an operation is attempted on an aggregate that does not exist.
    /// </summary>
    public class AggregateNotFoundException : Exception
    {
        public string Id { get; }

        /// <summary>
        /// Creates a new AggregateNotFoundException.
        /// </summary>
        /// <param name="id">The ID that does not exist.</param>
        /// <param name="message">An optional message.</param>
        public AggregateNotFoundException([NotNull] string id, [CanBeNull] string message = null) : base(message)
        {
            Id = id;
        }
    }

    /// <summary>
    /// Exception thrown when an optimistic locking failure occurs, i.e. an aggregate has been modified by another client
    /// after it was retrieved from the repository.
    /// </summary>
    public class OptimisticLockingFailureException : Exception
    {
        public string Id { get; }

        /// <summary>
        /// Creates a new OptimisticLockingFailureException.
        /// </summary>
        /// <param name="id">The ID of the aggregate in question.</param>
        /// <param name="message">An optional message.</param>
        public OptimisticLockingFailureException([NotNull] string id, [CanBeNull] string message = null) : base(message)
        {
            Id = id;
        }
    }
}
