package com.vvlanding.control;

import com.vvlanding.dto.DtoProduct;
import com.vvlanding.mapper.MapperProduct;
import com.vvlanding.repo.RepoProduct;
import com.vvlanding.security.CurrentUser;
import com.vvlanding.security.UserPrincipal;
import com.vvlanding.service.SerProductVariations;
import com.vvlanding.service.SerShopInfo;
import com.vvlanding.service.SerProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/product")
public class ContProduct {

    @Autowired
    SerProduct serProduct;

    @Autowired
    MapperProduct mapperProduct;

    @Autowired
    RepoProduct repoProduct;

    @Autowired
    SerShopInfo serShopInfo;

    @Autowired
    SerProductVariations serProductVariations;

    // ok
    //- Thêm
    @RequestMapping(value = "/ins", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", maxAge = 3600)
    public ResponseEntity<?> InsProduct(@CurrentUser UserPrincipal currentUser, @RequestBody DtoProduct prInput) {
        return new ResponseEntity<>(serProduct.InsSent(prInput, currentUser), HttpStatus.OK);

    }

}