package com.vvlanding.mapper;

import com.vvlanding.dto.DtoOrderDetail;
import com.vvlanding.table.BillDetail;
import com.vvlanding.utils.EntityMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MapperBillDetail extends EntityMapper<BillDetail, DtoOrderDetail> {
    @Override
    DtoOrderDetail toDto(BillDetail source);

    @Override
    BillDetail toEntity(DtoOrderDetail source);

    @Override
    List<DtoOrderDetail> toDtoList(List<BillDetail> sourceList);

    @Override
    List<BillDetail> toEntityList(List<DtoOrderDetail> sourceList);
}