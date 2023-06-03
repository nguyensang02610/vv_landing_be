package com.vvlanding.mapper;

import com.vvlanding.dto.DtoProduct;
import com.vvlanding.table.Product;
import com.vvlanding.utils.EntityMapper;
import org.mapstruct.Mapper;
import java.util.List;
@Mapper(componentModel = "spring")
public interface MapperProduct extends EntityMapper<Product, DtoProduct> {
    @Override
    DtoProduct toDto(Product source);

    @Override
    Product toEntity(DtoProduct source);

    @Override
    List<DtoProduct> toDtoList(List<Product> sourceList);

    @Override
    List<Product> toEntityList(List<DtoProduct> sourceList);
}