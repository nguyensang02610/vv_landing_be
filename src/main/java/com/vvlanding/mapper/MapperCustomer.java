package com.vvlanding.mapper;

import com.vvlanding.dto.DtoCustomer;
import com.vvlanding.table.Customer;
import com.vvlanding.utils.EntityMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MapperCustomer extends EntityMapper<Customer, DtoCustomer> {
    @Override
    DtoCustomer toDto(Customer source);

    @Override
    Customer toEntity(DtoCustomer source);

    @Override
    List<DtoCustomer> toDtoList(List<Customer> sourceList);

    @Override
    List<Customer> toEntityList(List<DtoCustomer> sourceList);
}