package net.pkhapps.dart.platform.query;

/**
 * Interface defining a page of a paginated result set.
 */
public interface Page<V> extends Iterable<V> {

    /**
     * Returns whether this page is the first page in the result set.
     *
     * @return true if this is the first page, false otherwise.
     */
    default boolean isFirstPage() {
        return getPageNumber() == 0;
    }

    /**
     * Returns whether this page is the last page in the result set.
     *
     * @return true if this is the last page, false otherwise.
     */
    default boolean isLastPage() {
        return getPageNumber() == getPageCount() - 1;
    }

    /**
     * Returns the actual number of items in this particular page. This can never be greater than {@link #getPageSize()}.
     *
     * @return the actual page size.
     */
    int size();

    /**
     * Returns the number of this particular page in the result set.
     *
     * @return the page number, starting from 0.
     */
    long getPageNumber();

    /**
     * Returns an approximation of the total number of pages in the result set.
     *
     * @return the total number of pages.
     */
    long getPageCount();

    /**
     * Returns the maximum number of items that fit in a page.
     *
     * @return the maximum page size.
     */
    int getPageSize();
}
