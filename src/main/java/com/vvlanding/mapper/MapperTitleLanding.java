package com.vvlanding.mapper;
import com.vvlanding.dto.DtoTitleLanding;
import com.vvlanding.table.TitleLanding;
import com.vvlanding.utils.EntityMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MapperTitleLanding extends EntityMapper<TitleLanding, DtoTitleLanding>{
    @Override
    DtoTitleLanding toDto(TitleLanding source);

    @Override
    TitleLanding toEntity(DtoTitleLanding source);

    @Override
    List<DtoTitleLanding> toDtoList(List<TitleLanding> sourceList);

    @Override
    List<TitleLanding> toEntityList(List<DtoTitleLanding> sourceList);
}
