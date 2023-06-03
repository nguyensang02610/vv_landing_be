package com.vvlanding.mapper;

import com.vvlanding.dto.DtoLandingPage;
import com.vvlanding.table.LandingPage;
import com.vvlanding.utils.EntityMapper;
import org.mapstruct.Mapper;
import java.util.List;
@Mapper(componentModel = "spring")
public interface MapperLandingPage extends EntityMapper<LandingPage, DtoLandingPage> {
    @Override
    DtoLandingPage toDto(LandingPage source);

    @Override
    LandingPage toEntity(DtoLandingPage source);

    @Override
    List<DtoLandingPage> toDtoList(List<LandingPage> sourceList);

    @Override
    List<LandingPage> toEntityList(List<DtoLandingPage> sourceList);
}
