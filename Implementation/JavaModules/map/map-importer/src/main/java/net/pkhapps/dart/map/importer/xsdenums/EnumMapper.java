package net.pkhapps.dart.map.importer.xsdenums;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO Document me!
 */
public abstract class EnumMapper<E extends Enum<E>, T> {

    private final Map<E, T> enumToObject = new HashMap<>();
    private final Map<T, E> objectToEnum = new HashMap<>();

    protected void registerEnumConstant(E enumConstant, T value) {
        enumToObject.put(enumConstant, value);
        objectToEnum.put(value, enumConstant);
    }

    public E toEnum(T value) {
        return objectToEnum.get(value);
    }

    public T fromEnum(E value) {
        return enumToObject.get(value);
    }
}
