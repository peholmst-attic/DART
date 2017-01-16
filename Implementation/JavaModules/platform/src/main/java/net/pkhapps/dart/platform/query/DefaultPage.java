package net.pkhapps.dart.platform.query;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Default implementation of {@link Page}.
 */
public class DefaultPage<V> implements Page<V> {

    private final List<V> items;
    private final long pageNumber;
    private final long pageCount;
    private final int pageSize;

    /**
     * Creates a new {@code DefaultPage}.
     *
     * @param items      the collection of items. The size must not exceed {@code pageSize}.
     * @param pageNumber the number of the page, starting from 0.
     * @param pageCount  the total number of pages in the result set.
     * @param pageSize   the maximum number of items in a page in the result set.
     * @throws IllegalArgumentException if the number of items exceeds {@code pageSize}.
     */
    public DefaultPage(@NotNull Collection<V> items, long pageNumber, long pageCount, int pageSize) {
        Objects.requireNonNull(items, "items must not be null");
        this.pageNumber = pageNumber;
        this.pageCount = pageCount;
        this.pageSize = pageSize;
        if (items.size() > pageSize) {
            throw new IllegalArgumentException("number of items cannot exceed page size");
        }
        this.items = new ArrayList<>(items);
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public long getPageNumber() {
        return pageNumber;
    }

    @Override
    public long getPageCount() {
        return pageCount;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public Iterator<V> iterator() {
        return items.iterator();
    }
}
