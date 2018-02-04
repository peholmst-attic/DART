
using JetBrains.Annotations;

namespace DART.Base.Domain.Attributes
{
    /// <summary>
    /// An attribute used to specify the aggregate name on an <see cref="AggregateRoot"/>. This name may be used
    /// by <see cref="IRepository{T}"/> implementations when looking up the correct object store 
    /// (collection, table, directory, file, etc.)
    /// </summary>
    /// <seealso cref="AggregateNameUtil"/>
    [System.AttributeUsage(System.AttributeTargets.Class | System.AttributeTargets.Struct)]
    public class AggregateName : System.Attribute
    {
        /// <summary>
        /// The name of the aggregate.
        /// </summary>
        public string Name { get; }

        /// <summary>
        /// Creates a new AggregateName attribute.
        /// </summary>
        /// <param name="name">The name of the aggregate.</param>
        public AggregateName([NotNull] string name)
        {
            Name = name;
        }
    }
}
