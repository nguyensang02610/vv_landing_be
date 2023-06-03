package com.vvlanding.mapper;

import com.vvlanding.dto.DtoBannerLanding;
import com.vvlanding.table.BannerLanding;
import com.vvlanding.utils.EntityMapper;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MapperBannerLanding extends EntityMapper<BannerLanding, DtoBannerLanding>{
    @Override
    DtoBannerLanding toDto(BannerLanding source);

    @Override
    BannerLanding toEntity(DtoBannerLanding source);

    @Override
    List<DtoBannerLanding> toDtoList(List<BannerLanding> sourceList);

    @Override
    List<BannerLanding> toEntityList(List<DtoBannerLanding> sourceList);
}
