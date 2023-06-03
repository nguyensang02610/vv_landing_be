//package com.vvlanding.service;
//
//import com.vvlanding.dto.DtoDomain;
//import com.vvlanding.mapper.MapperDomain;
//import com.vvlanding.repo.RepoDomain;
//import com.vvlanding.repo.RepoShopInfo;
//import com.vvlanding.table.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class SerDomain {
//    @Autowired
//    RepoDomain repoDomain;
//
//    @Autowired
//    MapperDomain mapperDomain;
//
//    @Autowired
//    RepoShopInfo repoShopInfo;
//
//    public List<Domain> GetAll()
//
//    {
//        return repoDomain.findAll();
//    }
//
//    public Page<Domain> getPage(Pageable pageable)
//    {
//        return repoDomain.findAll(pageable);
//    }
//
//    public  List<Domain> FindDomainQueryList(String query){
//        List<ShopInfo> shopInfoFind = repoShopInfo.findByTitleContaining(query);
//
//        List<Domain> domainSearch = new ArrayList<>();
//
////        if(landingPagesFind.size() > 0){
////            for(LandingPage landingPage : landingPagesFind ){
////                landingPageSearch.add(landingPage);
////            }
////        }
//        return getDomain(shopInfoFind, domainSearch);
//    }
//
//    public List<Domain> FindDomainByQueryPage(Pageable pageable,String query){
//        List<ShopInfo> shopInfoFind = repoShopInfo.findByTitleContaining(query);
//
//        List<Domain> domainSearch = new ArrayList<>();
//
//        return getDomain(shopInfoFind, domainSearch);
//    }
//
//    private List<Domain> getDomain(List<ShopInfo> shopInfoFind, List<Domain> domainSearch) {
//        if(shopInfoFind.size() > 0){
//            List<Domain> domainList = repoDomain.findAll();
//            for(ShopInfo shopInfo : shopInfoFind){
//                for(Domain domain:domainList ){
//                    if(shopInfo.getId().equals(domain.getShopInfo().getId())){
//                        domainSearch.add(domain);
//                    }
//                }
//            }
//        }
//        List<Domain> domainList = domainSearch.stream()
//                .sorted(Comparator.comparing(Domain::getId).reversed())
//                .collect(Collectors.toList());
//        return domainList;
//    }
//
////    public List<DtoDomain> getDtoDomainPage(Pageable pageable) {
////        List<Domain> Domains = repoDomain.findAllBy(pageable);
////        List<DtoDomain> dtoDomains = new ArrayList<>();
////        for (Domain Domain : Domains) {
////            DtoDomain dtoDomain = mapperDomain.toDto(Domain);
////        dtoDomain.setShopTitle(Domain.getShopInfo().getTitle());
////        dtoDomain.setShopId(Domain.getShopInfo().getId());
//////        dtoDomain.setConfigContent(Domain.getConfig().getContent());
////        dtoDomain.setConfigId(Domain.getConfig().getId());
////            dtoDomains.add(dtoDomain);
////        }
////        return dtoDomains;
////    }
//    public Optional<Domain > FindById(Long id) {
//        return repoDomain .findById(id);
//    }
//    public Page<Domain > FindByUrl(String url,Pageable pageable) {
//        return repoDomain .findByUrl(url,pageable);
//    }
//
//    public Object InsSent(DtoDomain dtoDomain) {
//        Map<String, Object> response = new HashMap<>();
//        try {
//            Domain Domain = mapperDomain.toEntity(dtoDomain);
////            Optional<Config> opConfig = repoConfig.findById(dtoDomain.getConfigId());
////            Config config = opConfig.get();
////            if (!opConfig.isPresent()) {
////                response.put("message", "Không tìm thấy cấu hình !!");
////                response.put("success", false);
////                return response;
////            }
//
//            Optional<ShopInfo> opShopInfo = repoShopInfo.findById(dtoDomain.getShopId());
//            ShopInfo shopInfo = opShopInfo.get();
//            if (!opShopInfo.isPresent()) {
//                response.put("message", "Không tìm thấy thông tin shop !!");
//                response.put("success", false);
//                return response;
//            }
//
//            Domain.setShopInfo(shopInfo);
////            Domain.setConfig(config);
//            Domain.setStatus(false);
//            Calendar cal = Calendar.getInstance();
//            cal.setTimeZone(TimeZone.getTimeZone("GMT"));
//            Date date =  cal.getTime();
//            Domain.setDateCreated(date);
//            Domain newDomain = repoDomain.save(Domain);
//            dtoDomain.setId(newDomain.getId());
//            dtoDomain.setStatus(false);
//            dtoDomain.setDateCreated(newDomain.getDateCreated());
//            response.put("success", true);
//            response.put("message", "thêm mới thành công !!!");
//            response.put("data", newDomain);
//            return  response;
//        } catch (Exception ex) {
//            response.put("data", ex.getMessage());
////            response.put("message", "Không tìm thấy cấu hình hoặc cửa hàng trên, vui lòng nhập lại !!");
//            response.put("message", "Không tìm thấy cửa hàng trên, vui lòng nhập lại !!");
//            response.put("success", false);
//            return response;
//        }
//    }
//
//    public  void Delete( Long id){
//        repoDomain.deleteById(id);
//    }
//}
