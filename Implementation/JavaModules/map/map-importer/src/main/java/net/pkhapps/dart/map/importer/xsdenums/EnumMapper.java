package net.pkhapps.dart.map.importer.xsdenums;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO Document me!
 */
public abstract class EnumMapper<E extends Enum<E>> {

    private final Map<E, Integer> enumToInteger = new HashMap<>();
    private final Map<Integer, E> integerToEnum = new HashMap<>();

    protected void registerEnumConstant(E enumConstant, Integer integerValue) {
        enumToInteger.put(enumConstant, integerValue);
        integerToEnum.put(integerValue, enumConstant);
    }

    public E toEnum(Integer value) {
        return integerToEnum.get(value);
    }

    public Integer fromEnum(E value) {
        return enumToInteger.get(value);
    }
}
