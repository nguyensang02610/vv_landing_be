package com.vvlanding.mapper;

import com.vvlanding.dto.DtoRefLandingPageUser;
import com.vvlanding.table.RefLandingPageUser;
import com.vvlanding.utils.EntityMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MapperRefLandingPageUser extends EntityMapper<RefLandingPageUser, DtoRefLandingPageUser> {
    @Override
    DtoRefLandingPageUser toDto(RefLandingPageUser source);

    @Override
    RefLandingPageUser toEntity(DtoRefLandingPageUser source);

    @Override
    List<DtoRefLandingPageUser> toDtoList(List<RefLandingPageUser> sourceList);

    @Override
    List<RefLandingPageUser> toEntityList(List<DtoRefLandingPageUser> sourceList);
}
