using JetBrains.Annotations;
using System;

namespace DART.Base.Domain
{
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
