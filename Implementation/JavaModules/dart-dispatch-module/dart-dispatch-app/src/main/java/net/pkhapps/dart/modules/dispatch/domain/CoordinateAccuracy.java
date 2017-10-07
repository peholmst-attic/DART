package net.pkhapps.dart.modules.dispatch.domain;

/**
 * Enumeration of different levels of accuracy for a set of geographical {@link Coordinates}.
 */
public enum CoordinateAccuracy {
    /**
     * The coordinates are accurate and pinpoint the actual location of the incident.
     */
    ACCURATE,
    /**
     * The coordinates are on the right street, but the actual location of the incident is still unknown.
     */
    STREET,
    /**
     * The coordinates are in the right district of a municipality, but the actual location of the incident is still
     * unknown.
     */
    DISTRICT,
    /**
     * The coordinates are in the right municipality, but the actual location of the incident is still unknown.
     */
    MUNICIPALITY
}
