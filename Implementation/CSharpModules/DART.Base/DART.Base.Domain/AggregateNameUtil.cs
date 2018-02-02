using DART.Base.Domain.Attributes;
using System.Reflection;

namespace DART.Base.Domain
{
    /// <summary>
    /// Utility class for working with the <see cref="AggregateName"/> attribute.
    /// </summary>
    public static class AggregateNameUtil
    {
        /// <summary>
        /// Gets the aggregate name of the given aggregate root. If the <see cref="AggregateName"/> attribute is
        /// present, the aggregate name is taken from there. Otherwise, the aggregate name is the name of the class itself.
        /// </summary>
        /// <typeparam name="T">The aggregate root class.</typeparam>
        /// <returns>The name of the aggregate.</returns>
        public static string GetAggregateName<T>() where T : AggregateRoot
        {
            var aggregateName = typeof(T).GetCustomAttribute<AggregateName>(false);
            return aggregateName != null ? aggregateName.Name : typeof(T).Name;
        }
    }
}
