package com.vvlanding.service;

import com.vvlanding.dto.DtoProperties;
import com.vvlanding.mapper.MapperProperties;
import com.vvlanding.repo.RepoProperties;
import com.vvlanding.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SerProperties {
    @Autowired
    RepoProperties repoProperties;

    @Autowired
    MapperProperties mapperProperties;


    public List<Properties> FindBy(String query) {
        return repoProperties.findByKeynameContainingOrValueContaining(query, query);
    }

    public Properties InsSent(Properties input) {
        return repoProperties.save(input);
    }

    public List<DtoProperties> getAllProperties(Pageable pageable) {
        List<Properties> properties = repoProperties.findAllBy(pageable);
        List<DtoProperties> dtoProperties = new ArrayList<>();
        for (Properties properties1 : properties) {
            DtoProperties dtoPropertiess = mapperProperties.toDto(properties1);
            dtoProperties.add(dtoPropertiess);
        }
        return dtoProperties;
    }

    public List<DtoProperties> getAllPropertiesByProductVariationsId(ProductVariations productVariations) {
        List<Properties> properties = repoProperties.findAllByProductVariations(productVariations);
        List<DtoProperties> dtoProperties = new ArrayList<>();
        for (Properties properties1 : properties) {
            DtoProperties dtoPropertiess = mapperProperties.toDto(properties1);
            dtoProperties.add(dtoPropertiess);
        }
        return dtoProperties;

    }
}
