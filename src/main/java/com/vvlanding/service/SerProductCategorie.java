package com.vvlanding.service;
import com.vvlanding.table.ProductCategories;
import com.vvlanding.repo.RepoProductCategorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service

public class SerProductCategorie {
    @Autowired
    RepoProductCategorie repoProductCategorie;

    public Page<ProductCategories> getPage(Pageable pageable) {
        return repoProductCategorie.findAll(pageable);
    }
    public Optional<ProductCategories> FindById(Long id) {
        return repoProductCategorie.findById(id);
    }
    public Optional<ProductCategories> FindByTitle(String title) {
        return repoProductCategorie.findByTitle(title);
    }

    public  ProductCategories InsSent(ProductCategories productCategories){
        return  repoProductCategorie.save(productCategories);
    }

    public  void Delete( Long id){
        repoProductCategorie.deleteById(id);
    }


}
