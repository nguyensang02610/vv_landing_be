package com.vvlanding.mapper;

import com.vvlanding.dto.DtoConfig;
import com.vvlanding.table.Config;
import com.vvlanding.utils.EntityMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MapperConfig extends EntityMapper<Config, DtoConfig> {
    @Override
    DtoConfig toDto(Config source);

    @Override
    Config toEntity(DtoConfig source);

    @Override
    List<DtoConfig> toDtoList(List<Config> sourceList);

    @Override
    List<Config> toEntityList(List<DtoConfig> sourceList);
}
