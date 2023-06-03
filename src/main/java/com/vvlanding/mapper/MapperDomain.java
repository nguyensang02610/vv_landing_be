//package com.vvlanding.mapper;
//
//import com.vvlanding.dto.DtoDomain;
//import com.vvlanding.table.Domain;
//import com.vvlanding.utils.EntityMapper;
//import org.mapstruct.Mapper;
//
//import java.util.List;
//
//@Mapper(componentModel = "spring")
//public interface MapperDomain extends EntityMapper<Domain, DtoDomain> {
//    @Override
//    DtoDomain toDto(Domain source);
//
//    @Override
//    Domain toEntity(DtoDomain source);
//
//    @Override
//    List<DtoDomain> toDtoList(List<Domain> sourceList);
//
//    @Override
//    List<Domain> toEntityList(List<DtoDomain> sourceList);
//}