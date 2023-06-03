package com.vvlanding.mapper;
import com.vvlanding.dto.DtoTypeLd;
import com.vvlanding.table.TypeLd;
import com.vvlanding.utils.EntityMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MapperTypeLd extends EntityMapper<TypeLd, DtoTypeLd> {
    @Override
    DtoTypeLd toDto(TypeLd source);

    @Override
    TypeLd toEntity(DtoTypeLd source);

    @Override
    List<DtoTypeLd> toDtoList(List<TypeLd> sourceList);

    @Override
    List<TypeLd> toEntityList(List<DtoTypeLd> sourceList);
}
