package com.vvlanding.utils;

import java.util.List;

public interface EntityMapper<E, D> {
    D toDto(E source);

    E toEntity(D source);

    List<D> toDtoList(List<E> sourceList);

    List<E> toEntityList(List<D> sourceList);
}
