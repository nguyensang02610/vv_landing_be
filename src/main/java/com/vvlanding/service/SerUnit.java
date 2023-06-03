package com.vvlanding.service;

import com.vvlanding.table.Units;
import com.vvlanding.repo.RepoUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SerUnit {
    @Autowired
    RepoUnit repoUnit;

    public Page<Units> getPage(Pageable pageable) {
        return repoUnit.findAll(pageable);
    }

    public Optional<Units> FindById(Long id) {
        return repoUnit.findById(id);
    }
//    public List<Units> FindByName(String name) {
//        return repoUnit.findByName(name);
//    }

    public Units InsSent(Units units) {
        return repoUnit.save(units);
    }

    public void Delete(Long id) {
        repoUnit.deleteById(id);
    }


}
