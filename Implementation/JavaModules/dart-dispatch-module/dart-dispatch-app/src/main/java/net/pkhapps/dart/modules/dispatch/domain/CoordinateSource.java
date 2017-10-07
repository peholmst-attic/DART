package net.pkhapps.dart.modules.dispatch.domain;

/**
 * Enumeration of different coordinate sources.
 */
public enum CoordinateSource {
    /**
     * The coordinates have been taken directly from a GIS.
     */
    GIS,
    /**
     * The coordinates have been given by the caller.
     */
    CALLER,
    /**
     * The coordinates have been selected from a map by the dispatcher.
     */
    DISPATCHER
}
