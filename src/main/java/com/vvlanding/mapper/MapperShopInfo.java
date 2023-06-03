package com.vvlanding.mapper;

import com.vvlanding.dto.DtoShopInfo;
import com.vvlanding.table.ShopInfo;
import com.vvlanding.utils.EntityMapper;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring")
public interface MapperShopInfo extends EntityMapper<ShopInfo, DtoShopInfo> {
    @Override
    DtoShopInfo toDto(ShopInfo source);

    @Override
    ShopInfo toEntity(DtoShopInfo source);

    @Override
    List<DtoShopInfo> toDtoList(List<ShopInfo> sourceList);

    @Override
    List<ShopInfo> toEntityList(List<DtoShopInfo> sourceList);
}
