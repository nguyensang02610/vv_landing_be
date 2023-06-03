package com.vvlanding.mapper;

import com.vvlanding.dto.DtoTitleLandingPage;
import com.vvlanding.table.TitleLandingPage;
import com.vvlanding.utils.EntityMapper;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring")
public interface MapperTitleLandingPage extends EntityMapper<TitleLandingPage, DtoTitleLandingPage> {
    @Override
    DtoTitleLandingPage toDto(TitleLandingPage source);

    @Override
    TitleLandingPage toEntity(DtoTitleLandingPage source);

    @Override
    List<DtoTitleLandingPage> toDtoList(List<TitleLandingPage> sourceList);

    @Override
    List<TitleLandingPage> toEntityList(List<DtoTitleLandingPage> sourceList);
}
