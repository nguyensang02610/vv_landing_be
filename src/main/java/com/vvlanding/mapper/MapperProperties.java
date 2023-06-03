package com.vvlanding.mapper;
import com.vvlanding.dto.DtoProperties;
import com.vvlanding.table.Properties;
import com.vvlanding.utils.EntityMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MapperProperties extends EntityMapper<Properties, DtoProperties> {
    @Override
    DtoProperties toDto(Properties source);

    @Override
    Properties toEntity(DtoProperties source);

    @Override
    List<DtoProperties> toDtoList(List<Properties> sourceList);

    @Override
    List<Properties> toEntityList(List<DtoProperties> sourceList);
}
