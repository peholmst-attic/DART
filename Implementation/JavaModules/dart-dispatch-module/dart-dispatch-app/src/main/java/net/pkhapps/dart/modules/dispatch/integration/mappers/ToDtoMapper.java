package net.pkhapps.dart.modules.dispatch.integration.mappers;

import org.jetbrains.annotations.Contract;

/**
 * TODO Document me
 *
 * @param <ENTITY>
 * @param <DTO>
 */
@FunctionalInterface
public interface ToDtoMapper<ENTITY, DTO> {

    @Contract("null -> null")
    DTO toDto(ENTITY entity);
}
