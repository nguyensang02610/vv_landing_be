package com.vvlanding.service;

import com.vvlanding.dto.DtoGetAllTypeLd;
import com.vvlanding.dto.DtoLandingPage;

import com.vvlanding.mapper.MapperLandingPage;
import com.vvlanding.repo.RepoTypeLd;
import com.vvlanding.repo.RepoLandingPage;
import com.vvlanding.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SerLandingPage {
    @Autowired
    RepoLandingPage repoLandingPage;

    @Autowired
    MapperLandingPage mapperLandingPage;

    @Autowired
    RepoTypeLd repoTypeLd;

    public List<LandingPage> GetAll()

    {
        return repoLandingPage.findAll();
    }

    public Page<LandingPage> getPage(Pageable pageable)
    {
        return repoLandingPage.findAll(pageable);
    }

    // check xem mã theme có tồn tại hay ko
    private LandingPage checkTheme(String codeld) {
        List<LandingPage> landingPagesOtp = repoLandingPage.findByCodeLd(codeld);
        if (landingPagesOtp.size() > 0) {
            return landingPagesOtp.get(0);
        }
        return null;
    }

    // check xem typeLd có tồn tại hay ko
    private TypeLd checkTypeLd(String title) {
        List<TypeLd> typeLdOtp = repoTypeLd.findByTitle(title);
        if (typeLdOtp.size() > 0) {
            return typeLdOtp.get(0);
        }
        return null;
    }
    // check xem typeLd, nếu có thì trả ra, ko có thêm mới
    public TypeLd checkTypeTypeLd(String title){
        TypeLd typeLd = checkTypeLd(title);
        if (typeLd == null) {
            TypeLd newTypeLd = new TypeLd();
            newTypeLd.setTitle(title);
            typeLd = repoTypeLd.save(newTypeLd);
        }
        return  typeLd;
    }

    // thêm theme landingpage
    public Object InsSent(DtoLandingPage dtoLandingPage) {
        Map<String, Object> response = new HashMap<>();
        try {

            // tìm tên tyleLd nêu có thì trả ra
            String title = dtoLandingPage.getTypeld();
            TypeLd typeLd = checkTypeTypeLd(title);

            String codeld = dtoLandingPage.getCodeLd();
            LandingPage landingPagee = checkTheme(codeld);
            if(landingPagee == null){
                LandingPage landingPage = new LandingPage();
                landingPage.setTypeLd(typeLd);
                landingPage.setTitle(dtoLandingPage.getTitle());
                landingPage.setImages(dtoLandingPage.getImages());
                landingPage.setCodeLd(codeld);
                landingPage.setBanner(dtoLandingPage.getBanner());
                LandingPage landingPage1 =  repoLandingPage.save(landingPage);
                dtoLandingPage.setId(landingPage1.getId());
                response.put("data", dtoLandingPage);
                response.put("success", true);
                return response;
            }
            response.put("message", "mã theme đã tồn tại !!!");
            response.put("success", false);
            return response;

        } catch (Exception ex) {
            response.put("data", ex);
            response.put("success", false);
            return response;
        }
    }

    public Optional<LandingPage> FindById(Long id) {
        return repoLandingPage.findById(id);
    }

    public void Delete(Long id) {
        repoLandingPage.deleteById(id);
    }

    public  List<LandingPage> FindLandingPageQueryList(String query){
        List<LandingPage> landingPageFind = repoLandingPage.findByTitleContaining(query);
        List<LandingPage> landingPageSearch = new ArrayList<>();
        if(landingPageFind.size() > 0){
            for(LandingPage landingPage : landingPageFind){
                landingPageSearch.add(landingPage);
            }
        }
        return getLandingPage(landingPageSearch);
    }

    public List<LandingPage> FindLandingPageByQueryPage(Pageable pageable,String query){
        Page<LandingPage> landingPageFind = repoLandingPage.findByTitleContaining(pageable,query);
        List<LandingPage> landingPageSearch = new ArrayList<>();
        if(landingPageFind.getSize() > 0){
            for(LandingPage landingPage : landingPageFind){
                landingPageSearch.add(landingPage);
            }
        }
        return getLandingPage(landingPageSearch);
    }

    private List<LandingPage> getLandingPage( List<LandingPage> landingPageSearch) {
        List<LandingPage> landingPageList = landingPageSearch.stream()
                .sorted(Comparator.comparing(LandingPage::getId).reversed())
                .collect(Collectors.toList());
        return landingPageList;
    }

    public List<DtoLandingPage> getAllLandingByTypeId(TypeLd typeLd) {
        List<LandingPage> landingPages = repoLandingPage.findAllByTypeLd(typeLd);
        List<DtoLandingPage> dtoLandingPages = new ArrayList<>();
        for (LandingPage landingPage : landingPages){
            DtoLandingPage dtoLandingPage = mapperLandingPage.toDto(landingPage);
            dtoLandingPage.setTypeld(landingPage.getTypeLd().getTitle());
            dtoLandingPages.add(dtoLandingPage);
        }
        return dtoLandingPages;
    }

    public Object getAllLdpByTypeId(Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<TypeLd> opTypeld = repoTypeLd.findById(id);
            if (!opTypeld.isPresent()) {
                response.put("success", false);
                response.put("mesage", " Không tìm thấy TypeLd id , vui lòng nhập lại !! ");
                return response;
            }
            List<LandingPage> listLand = repoLandingPage.findAllByTypeLdId(id);
            DtoGetAllTypeLd data = new DtoGetAllTypeLd();
            data.setId(opTypeld.get().getId());
            data.setTitle(opTypeld.get().getTitle());
            data.setLandingPage(listLand);
            response.put("data", data);
            response.put("success", true);
            response.put("mesage", " ok ");
            return response;
        } catch (Exception ex) {
            response.put("data", ex.getMessage());
            response.put("message", "ERROR !!");
            response.put("success", false);
            return response;
        }
    }

}
