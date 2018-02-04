using JetBrains.Annotations;
using System;

namespace DART.Base.Domain
{
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
}
