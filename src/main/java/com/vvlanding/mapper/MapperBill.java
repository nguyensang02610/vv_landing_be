package com.vvlanding.mapper;

import com.vvlanding.dto.DtoBill;
import com.vvlanding.table.Bill;
import com.vvlanding.utils.EntityMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MapperBill extends EntityMapper<Bill, DtoBill> {
    @Override
    DtoBill toDto(Bill source);

    @Override
    Bill toEntity(DtoBill source);

    @Override
    List<DtoBill> toDtoList(List<Bill> sourceList);

    @Override
    List<Bill> toEntityList(List<DtoBill> sourceList);
}