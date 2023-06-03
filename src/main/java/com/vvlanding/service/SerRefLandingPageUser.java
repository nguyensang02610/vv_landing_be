package com.vvlanding.service;

import com.vvlanding.dto.DtoBannerLanding;
import com.vvlanding.dto.DtoRefLandingPageUser;
import com.vvlanding.dto.DtoTitleLanding;
import com.vvlanding.dto.DtoTitleLandingPage;
import com.vvlanding.mapper.MapperBannerLanding;
import com.vvlanding.mapper.MapperRefLandingPageUser;
import com.vvlanding.mapper.MapperTitleLanding;
import com.vvlanding.mapper.MapperTitleLandingPage;
import com.vvlanding.payload.ResponseLandUser;
import com.vvlanding.payload.ResponseStatus;
import com.vvlanding.repo.*;
import com.vvlanding.security.UserPrincipal;
import com.vvlanding.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SerRefLandingPageUser {
    @Autowired
    RepoRefLandingPageUser repoRefLandingPageUser;

    @Autowired
    RepoLandingPage repoLandingPage;

    @Autowired
    RepoUser repoUser;

    @Autowired
    RepoProduct repoProduct;

    @Autowired
    RepoShopInfo repoShopInfo;

    @Autowired
    MapperRefLandingPageUser mapperRefLandingPageUser;

    @Autowired
    MapperTitleLanding mapperTitleLanding;

    @Autowired
    MapperTitleLandingPage mapperTitleLandingPage;

    @Autowired
    MapperBannerLanding mapperBannerLanding;

    @Autowired
    SerTitleLading serTitleLading;

    @Autowired
    SerBannerLading serBannerLading;

    @Autowired
    RepoTitleLanding repoTitleLanding;

    @Autowired
    RepoShopUserRole repoShopUserRole;

    @Autowired
    RepoTitleLandingDetail repoTitleLandingDetail;

    @Autowired
    RepoBannerLanding repoBannerLanding;

    @Autowired
    RepoTitleLandingPage repoTitleLandingPage;

    @Autowired
    SerTitleLandingPage serTitleLandingPage;

    public Optional<RefLandingPageUser> find(long id) {
        return repoRefLandingPageUser.findById(id);
    }

    // author thecong filter reflandingpage by query
    public List<DtoRefLandingPageUser> findRefLandingPageUserByQuery(long shopId, String s, String query) {
        List<DtoRefLandingPageUser> listDtoRefLandingPageUser = new ArrayList<>();
        List<RefLandingPageUser> listRefLandingPageUser = repoRefLandingPageUser.findAllByShopInfo_IdAndDomainOrProductName(shopId, s, query);
        for (RefLandingPageUser refLandingPageUser : listRefLandingPageUser) {
            List<TitleLanding> titleLandings = serTitleLading.findByRefLandingPageUser1Id(refLandingPageUser.getId());
            List<TitleLandingPage> titleLandingPages = serTitleLandingPage.findByRefLandingPageUserId(refLandingPageUser.getId());
            List<BannerLanding> bannerLandings = serBannerLading.findByByRefLandingPageUserId(refLandingPageUser.getId());
            DtoRefLandingPageUser dtoRefLandingPageUser = GetAllRefLandingPageUserss(refLandingPageUser.getShopInfo(), refLandingPageUser, bannerLandings, titleLandings, titleLandingPages);
            listDtoRefLandingPageUser.add(dtoRefLandingPageUser);

        }
        return listDtoRefLandingPageUser.stream()
                .sorted(Comparator.comparing(DtoRefLandingPageUser::getId).reversed())
                .collect(Collectors.toList());
    }

    //get Reflanding by shop id
    public List<DtoRefLandingPageUser> getRefLandingOfShop(ShopInfo shop) {
        List<DtoRefLandingPageUser> listDtoRefLandingPageUser = new ArrayList<>();
        List<RefLandingPageUser> refLandingPageUser = repoRefLandingPageUser.findByShopInfo(shop);
        for (RefLandingPageUser r : refLandingPageUser) {
            List<TitleLanding> titleLandings = serTitleLading.findByRefLandingPageUser1Id(r.getId());
            List<TitleLandingPage> titleLandingPages = serTitleLandingPage.findByRefLandingPageUserId(r.getId());
            List<BannerLanding> bannerLandings = serBannerLading.findByByRefLandingPageUserId(r.getId());
            DtoRefLandingPageUser dtoRefLandingPageUsers = GetAllRefLandingPageUserss(shop, r, bannerLandings, titleLandings, titleLandingPages);
            listDtoRefLandingPageUser.add(dtoRefLandingPageUsers);
        }
        return listDtoRefLandingPageUser.stream()
                .sorted(Comparator.comparing(DtoRefLandingPageUser::getId).reversed())
                .collect(Collectors.toList());
    }

    public static DtoRefLandingPageUser GetAllRefLandingPageUserss(ShopInfo shopInfo, RefLandingPageUser refLandingPageUser,
                                                                   List<BannerLanding> bannerLandings, List<TitleLanding> titleLandings,
                                                                   List<TitleLandingPage> titleLandingPages) {

        long id = refLandingPageUser.getId();
        String landingPageName = refLandingPageUser.getLandingPage().getTitle();
        String landingBanner = refLandingPageUser.getLandingPage().getBanner();
        String userName = refLandingPageUser.getUser().getTitle();
        String productName = refLandingPageUser.getProductName();
        Boolean status = refLandingPageUser.getStatus();
        String domain = refLandingPageUser.getDomain();
        long productId = refLandingPageUser.getProductId();
        long userId = refLandingPageUser.getUser().getId();
        long shopId = shopInfo.getId();
        long landingId = refLandingPageUser.getLandingPage().getId();
        String codeLd = refLandingPageUser.getLandingPage().getCodeLd();

        List<DtoBannerLanding> dtoBannerLandings = new ArrayList<DtoBannerLanding>();
        for (BannerLanding bannerLanding : bannerLandings) {
            long _id = bannerLanding.getId();
            String title = bannerLanding.getTitle();
            String section = bannerLanding.getSection();
            DtoBannerLanding dtoBannerLanding = new DtoBannerLanding(_id, title, bannerLanding.getImage(), section);
            dtoBannerLandings.add(dtoBannerLanding);
        }

        List<DtoTitleLanding> dtoTitleLandings = new ArrayList<DtoTitleLanding>();
        for (TitleLanding titleLanding : titleLandings) {
            long _id = titleLanding.getId();

            List<TitleLandingDetail> titleLandingDetails = titleLanding.getTitleLandingDetails().stream()
                    .collect(Collectors.mapping(p -> new TitleLandingDetail(p.getTitle(), p.getContent(), p.getImage(), p.getId()), Collectors.toList()));
            String title = titleLanding.getTitle();
            String content = titleLanding.getContent();
            String section = titleLanding.getSection();
            DtoTitleLanding dtoTitleLanding = new DtoTitleLanding(_id, title, content, section, titleLandingDetails);
            dtoTitleLandings.add(dtoTitleLanding);
        }

        List<DtoTitleLandingPage> dtoTitleLandingPages = new ArrayList<DtoTitleLandingPage>();
        for (TitleLandingPage titleLandingPage : titleLandingPages) {
            long _id = titleLandingPage.getId();
            String content = titleLandingPage.getContent();
            int section = titleLandingPage.getSection();
            int sub_section = titleLandingPage.getSub_section();
            int kind = titleLandingPage.getKind();
            DtoTitleLandingPage dtoTitleLandingPage = new DtoTitleLandingPage(_id, content, section, sub_section, kind);
            dtoTitleLandingPages.add(dtoTitleLandingPage);
        }

        DtoRefLandingPageUser doNewRefLandingPageUser = new DtoRefLandingPageUser(id, userName, productName, status, domain, productId, userId, landingPageName, landingBanner, landingId, shopId, codeLd, dtoTitleLandings, dtoBannerLandings, dtoTitleLandingPages);

        return doNewRefLandingPageUser;
    }

    // check domain khi them domain moi
    private boolean isDomain(String domain) {
        List<RefLandingPageUser> ListDomain = repoRefLandingPageUser.findByDomain(domain);
        if (ListDomain != null && ListDomain.size() > 0) {
            return true;
        }
        return false;
    }

    public RefLandingPageUser getRefLandingPageUserId(Long id, Long shopid) {
        Optional<RefLandingPageUser> opRefLandingPageUser = repoRefLandingPageUser.findById(id);
        RefLandingPageUser refLandingPageUser = opRefLandingPageUser.get();
        return refLandingPageUser;
    }

    public RefLandingPageUser getRefLandingPageId(Long id) {
        Optional<RefLandingPageUser> opRefLandingPageUser = repoRefLandingPageUser.findById(id);
        RefLandingPageUser refLandingPageUser = opRefLandingPageUser.get();
        return refLandingPageUser;
    }

    public Boolean checkShop(Long id, UserPrincipal currentUser) {
        Optional<ShopUserRole> opShopInfo = repoShopUserRole.findByShopInfoIdAndUserId(id, currentUser.getId());
        if (!opShopInfo.isPresent()) {
            return false;
        }
        return true;
    }

    public RefLandingPageUser checkRef(Long id, Long shopID) {
        Optional<RefLandingPageUser> opRefLandingPageUser = repoRefLandingPageUser.findById(id);
        if (!opRefLandingPageUser.isPresent()) {
            return null;
        }
        return opRefLandingPageUser.get();
    }

    public RefLandingPageUser checkRef2(Long id) {
        Optional<RefLandingPageUser> opRefLandingPageUser = repoRefLandingPageUser.findById(id);
        if (!opRefLandingPageUser.isPresent()) {
            return null;
        }
        return opRefLandingPageUser.get();
    }

    // Thêm domain
    public ResponseEntity upDate(DtoRefLandingPageUser res, Long shopId) {
        Map<String, Object> response = new HashMap<>();
        //check domain
        if (isDomain(res.getDomain())) {
            response.put("message", "domain đã tồn tại!!");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        // check ref
        RefLandingPageUser refLandingPageUser = checkRef(res.getId(), shopId);
        if (refLandingPageUser.equals(null)) {
            response.put("mesager", "không tìm thấy thông tin   !!!!");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        refLandingPageUser.setDomain(res.getDomain());
        refLandingPageUser.setStatus(true);
        RefLandingPageUser refLandingPageUser1 = repoRefLandingPageUser.save(refLandingPageUser);
        response.put("data", refLandingPageUser1);
        response.put("success", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // thêm refLandingUser
    public Object insSer(ResponseLandUser responseLandUser, UserPrincipal currentUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<User> opUser = repoUser.findByIdAndUsername(responseLandUser.getUserId(), currentUser.getUsername());
            Optional<LandingPage> opLand = repoLandingPage.findById(responseLandUser.getLandingId());
            Optional<Product> opProduct = repoProduct.findById(responseLandUser.getProductId());
            Optional<ShopUserRole> opShopInfo = repoShopUserRole.findByShopInfoIdAndUserId(responseLandUser.getShopId(), currentUser.getId());
            if (!opShopInfo.isPresent()) {
                response.put("success", false);
                response.put("messager", "không tìm cửa hàng");
                return response;
            }
            if (!opUser.isPresent()) {
                response.put("success", false);
                response.put("messager", "không tìm thấy tài khoản");
                return response;
            }
            if (!opLand.isPresent()) {
                response.put("success", false);
                response.put("messager", "không tìm thấy  landing page");
                return response;
            }
            if (!opProduct.isPresent()) {
                response.put("success", false);
                response.put("messager", "không tìm thấy  sản phẩm ");
                return response;
            }
            RefLandingPageUser data = new RefLandingPageUser();
            data.setId(0L);
            data.setLandingPage(opLand.get());
            data.setUser(opUser.get());
            data.setShopInfo(opShopInfo.get().getShopInfo());
            data.setProductId(responseLandUser.getProductId());
            data.setProductName(opProduct.get().getTitle());
            data.setStatus(false);
            data.setStartTime(new Date());
            Date date = new Date();
            data.setEndTime(new Date(date.getTime() + 86400000));

            RefLandingPageUser newData = repoRefLandingPageUser.save(data);
            response.put("success", true);
            response.put("messager", "thêm thành công");
            response.put("data", newData);
            return response;
        } catch (Exception e) {
            response.put("success", false);
            response.put("messager", e.getMessage());
            return response;
        }
    }

    //Kich hoat domain
    public ResponseEntity updateStatus(ShopInfo shopInfo, ResponseStatus responseStatus) {
        Optional<RefLandingPageUser> opRefLandingPageUser = repoRefLandingPageUser.findById(responseStatus.getId());
        Map<String, Object> response = new HashMap<>();
        RefLandingPageUser refLandingPageUser = opRefLandingPageUser.get();
        if (!opRefLandingPageUser.isPresent()) {
            response.put("message", "Không tìm thấy domain");
            response.put("success", "false");
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        } else {
            refLandingPageUser.setStatus(responseStatus.getStatus());
            RefLandingPageUser updateRefLandingPageUser = repoRefLandingPageUser.save(refLandingPageUser);
            response.put("data", updateRefLandingPageUser);
            response.put("message", "Cập nhật thành công");
            response.put("success", "true");
            return new ResponseEntity(response, HttpStatus.OK);
        }
    }

    // Gia hạn landing page
    public ResponseEntity updateTime(Long id, Long time) {
        Map<String, Object> response = new HashMap<>();
        Optional<RefLandingPageUser> ref = repoRefLandingPageUser.findById(id);
        if (!ref.isPresent()) {
            response.put("success", false);
            response.put("messager", "không tìm thấy landing page");
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        RefLandingPageUser refLanding = ref.get();
        refLanding.setStartTime(new Date());
        if (ref.get().getEndTime().getTime() >= new Date().getTime()) {
            Date date = new Date();
            long date2 = ref.get().getEndTime().getTime() - new Date().getTime();
            long getTime = time * 1000 * 60 * 60 * 24 + date2;
            refLanding.setEndTime(new Date(date.getTime() + getTime));
            refLanding.setStatus(true);
            repoRefLandingPageUser.save(refLanding);
        } else {
            long getTime = time * 1000 * 60 * 60 * 24;
            Date date = new Date();
            refLanding.setEndTime(new Date(date.getTime() + getTime));
            refLanding.setStatus(true);
            repoRefLandingPageUser.save(refLanding);
        }
        response.put("success", true);
        response.put("messager", "Thành công");
        return new ResponseEntity(response, HttpStatus.OK);
    }

    //    // check landing hết hạn
//    public ResponseEntity checkTime(){
//        Map<String,Object> response = new HashMap<>();
//        List<RefLandingPageUser> list = repoRefLandingPageUser.findAll();
//        for (RefLandingPageUser r: list) {
//            if (r.getEndTime()!= null){
//                if (r.getEndTime().getTime() < new Date().getTime()){
//                    r.setStatus(false);
//                    repoRefLandingPageUser.save(r);
//                }
//            }
//        }
//        response.put("success",true);
//        return new ResponseEntity(response\
    // Thêm Banner vào theme
    public ResponseEntity insSerThemeBanner(DtoRefLandingPageUser dtoRefLandingPageUser, Long shopId) {
        Map<String, Object> response = new HashMap<>();
        // check ref
        RefLandingPageUser refLandingPageUser = checkRef(dtoRefLandingPageUser.getId(), shopId);
        if (refLandingPageUser.equals(null)) {
            response.put("mesager", "không tìm thấy thông tin   !!!!");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
//        RefLandingPageUser refLandingPageUser = checkRef2(dtoRefLandingPageUser.getId());
        RefLandingPageUser newData = repoRefLandingPageUser.save(refLandingPageUser);
        List<DtoBannerLanding> dtoBannerLandingList = dtoRefLandingPageUser.getDtoBannerLandings();
        List<DtoBannerLanding> dataBannerLanding = createListBannerLanding(dtoBannerLandingList, newData, dtoRefLandingPageUser);
        dtoRefLandingPageUser.setDtoBannerLandings(dataBannerLanding);
        response.put("data", dtoRefLandingPageUser);
        response.put("success", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Thêm Title vào theme
    public ResponseEntity insSerThemeTitle(DtoRefLandingPageUser dtoRefLandingPageUser, Long shopId) {
        Map<String, Object> response = new HashMap<>();
        // check ref
        RefLandingPageUser refLandingPageUser = checkRef(dtoRefLandingPageUser.getId(), shopId);
        if (refLandingPageUser.equals(null)) {
            response.put("mesager", "không tìm thấy thông tin   !!!!");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        RefLandingPageUser newData = repoRefLandingPageUser.save(refLandingPageUser);
        List<DtoTitleLanding> dtoTitleLandingList = dtoRefLandingPageUser.getDtoTitleLandings();
        List<DtoTitleLanding> dataDtoTitleLanding = createListTitleLanding(dtoTitleLandingList, newData, dtoRefLandingPageUser);
        dtoRefLandingPageUser.setDtoTitleLandings(dataDtoTitleLanding);
        response.put("data", dtoRefLandingPageUser);
        response.put("success", true);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    public ResponseEntity insSerThemeTitleLanding(DtoRefLandingPageUser dtoRefLandingPageUser, Long shopId) {
        Map<String, Object> response = new HashMap<>();
        // check ref
        RefLandingPageUser refLandingPageUser = checkRef(dtoRefLandingPageUser.getId(), shopId);
        if (refLandingPageUser.equals(null)) {
            response.put("mesager", "không tìm thấy thông tin   !!!!");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        RefLandingPageUser newData = repoRefLandingPageUser.save(refLandingPageUser);
        List<DtoTitleLandingPage> dtoTitleLandingPages = dtoRefLandingPageUser.getDtoTitleLandingPages();
        List<DtoTitleLandingPage> dataDtoTitleLandingPage = createListTitleLandingPage(dtoTitleLandingPages, newData, dtoRefLandingPageUser);
        dtoRefLandingPageUser.setDtoTitleLandingPages(dataDtoTitleLandingPage);
        response.put("data", dtoRefLandingPageUser);
        response.put("success", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // tạo titleLanding
    public List<DtoTitleLanding> createListTitleLanding(List<DtoTitleLanding> data, RefLandingPageUser refLandingPageUser, DtoRefLandingPageUser dtoRefLandingPageUser) {
        List<DtoTitleLanding> titleLandingList = new ArrayList<>();
        if (data != null && data.size() > 0) {
            for (DtoTitleLanding titleLanding1 : data) {
                DtoTitleLanding dtoTitleLanding = createTitleLanding(titleLanding1, refLandingPageUser);
                titleLandingList.add(dtoTitleLanding);
            }
        }
        return titleLandingList;
    }

    public List<DtoTitleLandingPage> createListTitleLandingPage(List<DtoTitleLandingPage> data, RefLandingPageUser refLandingPageUser, DtoRefLandingPageUser dtoRefLandingPageUser) {
        List<DtoTitleLandingPage> titleLandingPageList = new ArrayList<>();
        if (data != null && data.size() > 0) {
            for (DtoTitleLandingPage titleLandingPage1 : data) {
                DtoTitleLandingPage dtoTitleLandingPage = createTitleLandingPage(titleLandingPage1, refLandingPageUser);
                titleLandingPageList.add(dtoTitleLandingPage);
            }
        }
        return titleLandingPageList;
    }


    // tạo bannerLanding
    public List<DtoBannerLanding> createListBannerLanding(List<DtoBannerLanding> data, RefLandingPageUser refLandingPageUser, DtoRefLandingPageUser dtoRefLandingPageUser) {
        List<DtoBannerLanding> bannerLandingList = new ArrayList<>();
        if (data != null && data.size() > 0) {
            for (DtoBannerLanding bannerLanding : data) {
                DtoBannerLanding dtoBannerLanding = createBannerLanding(bannerLanding, refLandingPageUser);
                bannerLandingList.add(dtoBannerLanding);
            }
        }
        return bannerLandingList;
    }


    public void Delete(Long id) {
        repoRefLandingPageUser.deleteById(id);
    }


    // Công - api edit Theme Landingpage
    public ResponseEntity updateLandingPageBanner(DtoRefLandingPageUser dtoRefLandingPageUser, Long shopId) {
        Map<String, Object> response = new HashMap<>();

        Optional<RefLandingPageUser> opRefLandingPageUser = repoRefLandingPageUser.findById(dtoRefLandingPageUser.getId());
        if (!opRefLandingPageUser.isPresent()) {
            response.put("mesager", "không tìm thấy thông tin !!!!");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        RefLandingPageUser refLandingPageUser = opRefLandingPageUser.get();
        RefLandingPageUser updateRefLandingPageUser = repoRefLandingPageUser.save(refLandingPageUser);
        List<DtoBannerLanding> dataUpdate2 = updateBannerLanding(dtoRefLandingPageUser.getDtoBannerLandings(), updateRefLandingPageUser, dtoRefLandingPageUser);
        dtoRefLandingPageUser.setDtoBannerLandings(dataUpdate2);
        response.put("data", dtoRefLandingPageUser);
        response.put("success", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Công - api edit Theme Landingpage
    public ResponseEntity updateLandingPageTitle(DtoRefLandingPageUser dtoRefLandingPageUser, Long shopId) {
        Map<String, Object> response = new HashMap<>();

        // check RefLanding
        Optional<RefLandingPageUser> opRefLandingPageUser = repoRefLandingPageUser.findById(dtoRefLandingPageUser.getId());
        if (!opRefLandingPageUser.isPresent()) {
            response.put("mesager", "không tìm thấy thông tin !!!!");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        RefLandingPageUser refLandingPageUser = opRefLandingPageUser.get();
        RefLandingPageUser updateRefLandingPageUser = repoRefLandingPageUser.save(refLandingPageUser);
        List<DtoTitleLanding> dataUpdate1 = updateTitleLanding(dtoRefLandingPageUser.getDtoTitleLandings(), updateRefLandingPageUser, dtoRefLandingPageUser);
        dtoRefLandingPageUser.setDtoTitleLandings(dataUpdate1);
        response.put("data", dtoRefLandingPageUser);
        response.put("success", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // cập nhật titleLanding chưa có thì thành thêm mới
    private List<DtoTitleLanding> updateTitleLanding(List<DtoTitleLanding> dtoTitleLandings, RefLandingPageUser refLandingPageUser, DtoRefLandingPageUser dtoRefLandingPageUser) {
        List<DtoTitleLanding> listData = new ArrayList<>();
        for (DtoTitleLanding item : dtoTitleLandings) {
            Optional<TitleLanding> opTitleLanding = repoTitleLanding.findById(item.getId());
            if (!opTitleLanding.isPresent()) {
                createTitleLanding(item, refLandingPageUser);
            } else {
                TitleLanding titleLanding = opTitleLanding.get();
                List<TitleLandingDetail> newTitleLandingDetail = createTitleLandingDetail(item.getTitleLandingDetails());
                titleLanding.setTitle(item.getTitle());
                titleLanding.setContent(item.getContent());
                TitleLanding titleLanding1 = repoTitleLanding.save(titleLanding);
                DtoTitleLanding upDtoTitleLanding = mapperTitleLanding.toDto(titleLanding1);
                listData.add(upDtoTitleLanding);
            }
        }
        return listData;
    }

    public ResponseEntity updateLandingPageTitle2(DtoRefLandingPageUser dtoRefLandingPageUser, Long shopId) {
        Map<String, Object> response = new HashMap<>();

        // check RefLanding
        Optional<RefLandingPageUser> opRefLandingPageUser = repoRefLandingPageUser.findById(dtoRefLandingPageUser.getId());
        if (!opRefLandingPageUser.isPresent()) {
            response.put("mesager", "không tìm thấy thông tin !!!!");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        RefLandingPageUser refLandingPageUser = opRefLandingPageUser.get();
        RefLandingPageUser updateRefLandingPageUser = repoRefLandingPageUser.save(refLandingPageUser);
        List<DtoTitleLandingPage> dataUpdate1 = updateTitleLanding2(dtoRefLandingPageUser.getDtoTitleLandingPages(), updateRefLandingPageUser, dtoRefLandingPageUser);
        dtoRefLandingPageUser.setDtoTitleLandingPages(dataUpdate1);
        response.put("data", dtoRefLandingPageUser);
        response.put("success", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // cập nhật titleLanding chưa có thì thành thêm mới
    private List<DtoTitleLandingPage> updateTitleLanding2(List<DtoTitleLandingPage> dtoTitleLandingPages, RefLandingPageUser refLandingPageUser, DtoRefLandingPageUser dtoRefLandingPageUser) {
        List<DtoTitleLandingPage> listData = new ArrayList<>();
        for (DtoTitleLandingPage item : dtoTitleLandingPages) {
            Optional<TitleLandingPage> opTitleLanding = repoTitleLandingPage.findById(item.getId());
            if (!opTitleLanding.isPresent()) {
                createTitleLandingPage(item, refLandingPageUser);
            } else {
                TitleLandingPage titleLanding = opTitleLanding.get();
                titleLanding.setContent(item.getContent());
                titleLanding.setSection(item.getSection());
                titleLanding.setSub_section(item.getSub_section());
                titleLanding.setKind(item.getKind());
                TitleLandingPage titleLanding1 = repoTitleLandingPage.save(titleLanding);
                DtoTitleLandingPage upDtoTitleLanding = mapperTitleLandingPage.toDto(titleLanding1);
                listData.add(upDtoTitleLanding);
            }
        }
        return listData;
    }

    // cập nhật BannerLanding chưa có thì thành thêm mới
    private List<DtoBannerLanding> updateBannerLanding(List<DtoBannerLanding> dtoBannerLandings, RefLandingPageUser refLandingPageUser, DtoRefLandingPageUser dtoRefLandingPageUser) {
        List<DtoBannerLanding> listData = new ArrayList<>();
        for (DtoBannerLanding item : dtoBannerLandings) {
            Optional<BannerLanding> opBannerLanding = repoBannerLanding.findById(item.getId());
            if (!opBannerLanding.isPresent()) {
                createBannerLanding(item, refLandingPageUser);
            } else {
                BannerLanding bannerLanding = opBannerLanding.get();
                bannerLanding.setTitle(item.getTitle());
                bannerLanding.setImage(item.getImage());
                bannerLanding.setSection(item.getSection());
                BannerLanding bannerLanding1 = repoBannerLanding.save(bannerLanding);
                DtoBannerLanding upDtoBannerLanding = mapperBannerLanding.toDto(bannerLanding1);
                listData.add(upDtoBannerLanding);
            }
        }
        return listData;
    }

    // tao mới TitleLandingDetail
    public List<TitleLandingDetail> createTitleLandingDetail(List<TitleLandingDetail> listTitleLandingDetail) {
        List<TitleLandingDetail> newTitleLandingDetail = new ArrayList<>();
        if (listTitleLandingDetail.size() > 0 && listTitleLandingDetail != null) {
            for (TitleLandingDetail titleLandingDetail : listTitleLandingDetail) {
                List<TitleLandingDetail> titleLandingDetails = repoTitleLandingDetail.findAllByTitleAndAndContentAndImage(titleLandingDetail.getTitle(), titleLandingDetail.getContent(), titleLandingDetail.getImage());
                if (titleLandingDetails.size() > 0) {
                    newTitleLandingDetail.add(titleLandingDetails.get(0));
                } else {
                    TitleLandingDetail newItem = repoTitleLandingDetail.save(titleLandingDetail);
                    newTitleLandingDetail.add(newItem);
                }
            }
        }
        return newTitleLandingDetail;
    }

    // tạo các titleLanding to
    public DtoTitleLanding createTitleLanding(DtoTitleLanding dtoTitleLanding, RefLandingPageUser refLandingPageUser) {
        TitleLanding titleLanding;
        titleLanding = mapperTitleLanding.toEntity(dtoTitleLanding);
        titleLanding.setTitle(dtoTitleLanding.getTitle());
        titleLanding.setContent(dtoTitleLanding.getContent());
        titleLanding.setRefLandingPageUser1(refLandingPageUser);
        List<TitleLandingDetail> newTitleLandingDetail = createTitleLandingDetail(dtoTitleLanding.getTitleLandingDetails());
        TitleLanding titleLanding1 = repoTitleLanding.save(titleLanding);
        dtoTitleLanding.setId(titleLanding1.getId());
        dtoTitleLanding.setTitleLandingDetails(newTitleLandingDetail);
        return dtoTitleLanding;
    }

    public DtoTitleLandingPage createTitleLandingPage(DtoTitleLandingPage dtoTitleLandingPage, RefLandingPageUser refLandingPageUser) {
        TitleLandingPage titleLandingPage;
        titleLandingPage = mapperTitleLandingPage.toEntity(dtoTitleLandingPage);
        titleLandingPage.setContent(dtoTitleLandingPage.getContent());
        titleLandingPage.setSection(dtoTitleLandingPage.getSection());
        titleLandingPage.setSub_section(dtoTitleLandingPage.getSub_section());
        titleLandingPage.setKind(dtoTitleLandingPage.getKind());
        titleLandingPage.setRefLandingPageUser(refLandingPageUser);
        TitleLandingPage titleLandingPage1 = repoTitleLandingPage.save(titleLandingPage);
        dtoTitleLandingPage.setId(titleLandingPage1.getId());
        return dtoTitleLandingPage;
    }

    // tạo các BannerLanding to
    public DtoBannerLanding createBannerLanding(DtoBannerLanding dtoBannerLanding, RefLandingPageUser refLandingPageUser) {
        BannerLanding bannerLanding;
        bannerLanding = mapperBannerLanding.toEntity(dtoBannerLanding);
        bannerLanding.setTitle(dtoBannerLanding.getTitle());
        bannerLanding.setImage(dtoBannerLanding.getImage());
        bannerLanding.setSection(dtoBannerLanding.getSection());
        bannerLanding.setRefLandingPageUser(refLandingPageUser);
        BannerLanding bannerLanding1 = repoBannerLanding.save(bannerLanding);
        dtoBannerLanding.setId(bannerLanding1.getId());
        return dtoBannerLanding;
    }

    public ResponseEntity deleteDomain(Long refId, Long shopId){
        Optional<RefLandingPageUser> refLandingPageUser = repoRefLandingPageUser.findByShopInfoIdAndId(shopId,refId);
        Map<Object,String> response = new HashMap<>();
        if (!refLandingPageUser.isPresent()){
            response.put("message","Không tìm thấy domain ");
            return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
        }
        repoRefLandingPageUser.delete(refLandingPageUser.get());
        response.put("message","delete thành công");
        return new ResponseEntity(response,HttpStatus.OK);
    }

}
