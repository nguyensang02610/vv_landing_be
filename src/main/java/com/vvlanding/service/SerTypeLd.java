package com.vvlanding.service;

import com.vvlanding.dto.DtoTypeLd;
import com.vvlanding.mapper.MapperTypeLd;
import com.vvlanding.table.TypeLd;
import com.vvlanding.repo.RepoTypeLd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SerTypeLd {
    @Autowired
    RepoTypeLd repoTypeLd;

    @Autowired
    MapperTypeLd mapperTypeLd;

    @Autowired
    SerLandingPage serLandingPage;

    public List<TypeLd> GetAll() {return repoTypeLd.findAll();}
    public Page<TypeLd> getPage(Pageable pageable) {
        return repoTypeLd.findAll(pageable);
    }
    public Optional<TypeLd > FindById(Long id) {
        return repoTypeLd .findById(id);
    }
    public Page<TypeLd > FindByTitle(String title,Pageable pageable) {
        return repoTypeLd .findByTitle(title,pageable);
    }
    public Optional<TypeLd> getTypeLd(Long typeId) {
        return repoTypeLd.findById(typeId);
    }

    public  TypeLd InsSent(TypeLd typeLd){
        return  repoTypeLd.save(typeLd);
    }

    public  void Delete( Long id){
        repoTypeLd.deleteById(id);
    }

    public List<DtoTypeLd> getDtoTypeLdPage(Pageable pageable) {
        List<TypeLd> typeLds = repoTypeLd.findAllBy(pageable);
        List<DtoTypeLd> dtoTypeLds = new ArrayList<>();
        for (TypeLd typeLd : typeLds) {
            DtoTypeLd dtoTypeLd = mapperTypeLd.toDto(typeLd);
            dtoTypeLd.setLandingPages(new ArrayList<>(serLandingPage.getAllLandingByTypeId(typeLd)));
            dtoTypeLds.add(dtoTypeLd);
        }
        return dtoTypeLds;
    }
}