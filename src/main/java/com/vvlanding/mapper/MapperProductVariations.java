package com.vvlanding.mapper;

import com.vvlanding.dto.DtoProductVariations;
import com.vvlanding.table.ProductVariations;
import com.vvlanding.utils.EntityMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MapperProductVariations extends EntityMapper<ProductVariations, DtoProductVariations> {
    @Override
    DtoProductVariations toDto(ProductVariations source);

    @Override
    ProductVariations toEntity(DtoProductVariations source);

    @Override
    List<DtoProductVariations> toDtoList(List<ProductVariations> sourceList);

    @Override
    List<ProductVariations> toEntityList(List<DtoProductVariations> sourceList);
}
