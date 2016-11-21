package net.pkhapps.dart.map.api;

/**
 * An enumeration of different modes to use when performing textual name searches.
 */
public enum NameMatch {

    /**
     * The name should match the search term exactly (ignoring case).
     */
    EXACT,
    /**
     * The name should start with the search term (ignoring case).
     */
    STARTS_WITH,
    /**
     * The name should end with the search term (ignoring case).
     */
    ENDS_WITH,
    /**
     * The name should contain the search term (ignoring case).
     */
    CONTAINS
}
