package com.vvlanding.service;

import com.vvlanding.data.ReportBill;
import com.vvlanding.data.TotalOrderStatus;
import com.vvlanding.dto.*;
import com.vvlanding.dto.shipped.DtoOrderProduct;
import com.vvlanding.dto.shipped.TotalBillDTO;
import com.vvlanding.mapper.MapperBill;
import com.vvlanding.mapper.MapperBillDetail;
import com.vvlanding.mapper.MapperCustomer;
import com.vvlanding.payload.ResponseStatus;
import com.vvlanding.repo.*;
import com.vvlanding.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SerBill {
    @Autowired
    RepoBill repoBill;

    @Autowired
    RepoLandingPage repoLandingPage;

    @Autowired
    RepoShopInfo repoShopInfo;

    @Autowired
    RepoBillDetail repoBillDetail;

    @Autowired
    MapperBill mapperBill;

    @Autowired
    SerBillDetail serBillDetail;

    @Autowired
    RepoCustomer repoCustomer;

    @Autowired
    MapperBillDetail mapperBillDetail;

    @Autowired
    MapperCustomer mapperCustomer;

    @Autowired
    RepoProduct repoProduct;

    @Autowired
    SimpMessagingTemplate template;

    @Autowired
    RepoProperties repoProperties;

    @Autowired
    RepoProductVariations repoProductVariations;

    @Autowired
    RepoStatus repoStatus;

    @Autowired
    RepoMapGHTK repoMapGHTK;

    @Autowired
    RepoMapGHN repoMapGHN;

    @Autowired
    RepoMapViettel repoMapViettel;

    public StatusShipped checkStatusShipped(Long id) {
        Optional<StatusShipped> opStatusShipped = repoStatus.findById(id);
        if (!opStatusShipped.isPresent()) {
            return null;
        }
        return opStatusShipped.get();
    }

    // author hungpham report shop
    public ResponseEntity ReportServiceShopOfAbout(Long shopId, String month, String channel, String shipPartner) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> response = new HashMap<>();
        try {
            String[] newData;
            newData = month.split("/");
            String fromData;
            fromData = newData[0];
            String toData = newData[1];
            String fromDate = fromData.concat(" 00:00:00");
            String toDate = toData.concat(" 23:59:59");
            Date newFromDate = formatter.parse(fromDate);
            Date newToDate = formatter.parse(toDate);
            LocalDate FromDate = newFromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate ToDate = newToDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int fromMonth = ((LocalDate) FromDate).getMonthValue();
            int toMonth = ((LocalDate) ToDate).getMonthValue();
            List<Bill> listData = new ArrayList<>();
            if (channel == null && shipPartner == null) {
                listData = repoBill.reportRenvenueOfShopTimeAbout(newFromDate, newToDate, shopId);
            } else if (channel != null && shipPartner == null) {
                listData = repoBill.findDateAndChannel(newFromDate, newToDate, channel, shopId);
            } else if (channel == null && shipPartner != null) {
                listData = repoBill.findDateAndShip(newFromDate, newToDate, shipPartner, shopId);
            } else if (channel != null && shipPartner != null) {
                listData = repoBill.findDateAndChannelAndShip(newFromDate, newToDate, channel, shipPartner, shopId);
            }
            int totalBill = listData.size();
            int totalConfirmBill = listData.stream().filter(item -> item.getIsActive().booleanValue()).collect(Collectors.toList()).size();
            double totalMoney = listData.stream().collect(Collectors.toList()).stream().mapToDouble(item -> item.getTotalMoney()).sum();
            double totalConfirmMoney = listData.stream().filter(item -> item.getIsActive().booleanValue()).collect(Collectors.toList()).stream().mapToDouble(item -> item.getTotalMoney()).sum();
            double totalShipMoney = listData.stream().collect(Collectors.toList()).stream().mapToDouble(total -> total.getShipFee()).sum();
            ReportBill data = new ReportBill();
            data.setTotalBill(totalBill);
            data.setTotalConfirmBill(totalConfirmBill);
            data.setTotalMoney(totalMoney);
            data.setTotalConfirmMoney(totalConfirmMoney);
            data.setTotalShipMoney(totalShipMoney);
            data.setMonth(toMonth - fromMonth + 1);
            response.put("data", data);
            response.put("message", "dữ liệu");
            response.put("success", "true");
            return new ResponseEntity(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("message", "không có thông tin");
            response.put("success", "false");
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity TotalStatus(Long shopId, String month) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> response = new HashMap<>();
        try {
            String[] newData;
            newData = month.split("/");
            String fromData = newData[0];
            String toData = newData[1];
            String fromDate = fromData.concat(" 00:00:00");
            String toDate = toData.concat(" 23:59:59");
            Date newFromDate = formatter.parse(fromDate);
            Date newToDate = formatter.parse(toDate);
            LocalDate FromDate = newFromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate ToDate = newToDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int fromMonth = ((LocalDate) FromDate).getMonthValue();
            int toMonth = ((LocalDate) ToDate).getMonthValue();
            List<Bill> listData = repoBill.reportRenvenueOfShopTimeAbout(newFromDate, newToDate, shopId);
            List<Bill> listDM = repoBill.TotalStatus(newFromDate, newToDate, 1L, shopId);
            List<Bill> listDXN = repoBill.TotalStatus(newFromDate, newToDate, 2L, shopId);
            List<Bill> listGVC = repoBill.TotalStatus(newFromDate, newToDate, 3L, shopId);
            List<Bill> listDG = repoBill.TotalStatus(newFromDate, newToDate, 4L, shopId);
            List<Bill> listDaG = repoBill.TotalStatus(newFromDate, newToDate, 5L, shopId);
            List<Bill> listCH = repoBill.TotalStatus(newFromDate, newToDate, 6L, shopId);
            List<Bill> listDH = repoBill.TotalStatus(newFromDate, newToDate, 7L, shopId);
            List<Bill> listDDS = repoBill.TotalStatus(newFromDate, newToDate, 8L, shopId);
            List<Bill> listDelay = repoBill.TotalStatus(newFromDate, newToDate, 9L, shopId);
            List<Bill> listHuy = repoBill.TotalStatus(newFromDate, newToDate, 10L, shopId);
            int totalData = listData.size();
            int totalDM = listDM.size();
            int totalDXN = listDXN.size();
            int totalGVC = listGVC.size();
            int totalDG = listDG.size();
            int totalDaG = listDaG.size();
            int totalCH = listCH.size();
            int totalDH = listDH.size();
            int totalDDS = listDDS.size();
            int totalDelay = listDelay.size();
            int totalHuy = listHuy.size();
            TotalOrderStatus data = new TotalOrderStatus();
            data.setTotalDM(totalDM);
            data.setTotalDXN(totalDXN);
            data.setTotalGVC(totalGVC);
            data.setTotalDG(totalDG);
            data.setTotalDaG(totalDaG);
            data.setTotalCH(totalCH);
            data.setTotalDH(totalDH);
            data.setTotalDDS(totalDDS);
            data.setTotalDelay(totalDelay);
            data.setTotalHuy(totalHuy);
            data.setTotalData(totalData);
            data.setMonth(toMonth - fromMonth + 1);
            response.put("data", data);
            response.put("message", "Dữ liệu");
            response.put("success", "true");
            return new ResponseEntity(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("message", "không có thông tin");
            response.put("success", "false");
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    // update status bill
    public ResponseEntity updateStatus(ShopInfo shopInfo, ResponseStatus responseStatus) {
        Optional<Bill> opBill = repoBill.findByShopIdAndId(shopInfo.getId(), responseStatus.getId());
        Map<String, Object> response = new HashMap<>();
        Bill bill = opBill.get();
        if (!opBill.isPresent()) {
            response.put("message", "Không tìm thấy thông tin phiếu mua hàng");
            response.put("success", "false");
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        } else {
            bill.setIsActive(responseStatus.getStatus());
            Bill updateBill = repoBill.save(bill);
            response.put("data", updateBill);
            response.put("message", "Cập nhật thành công đơn hàng");
            response.put("success", "true");
            return new ResponseEntity(response, HttpStatus.OK);
        }
    }

    public List<DtoBill> getBillOfShopByQuery(ShopInfo shopInfo, String s, String query, String month) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String[] newData;
        newData = month.split("/");
        String fromData = newData[0];
        String toData = newData[1];
        String fromDate = fromData.concat(" 00:00:00");
        String toDate = toData.concat(" 23:59:59");
        Date newFromDate = formatter.parse(fromDate);
        Date newToDate = formatter.parse(toDate);
        LocalDate FromDate = newFromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate ToDate = newToDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int fromMonth = ((LocalDate) FromDate).getMonthValue();
        int toMonth = ((LocalDate) ToDate).getMonthValue();
        List<DtoBill> listDtoBill = new ArrayList<>();
        Optional<StatusShipped> statusShippeds = repoStatus.findByStatusName(query);
        List<Customer> customer = repoCustomer.findByShopIdAndPhoneOrTitle(shopInfo.getId(),s, query);
        List<Bill> listBillQuery = repoBill.findAllByOrderCode(query);
//        Optional<Bill> bill = repoBill.findByCodeBillContainingOrChannelContaining(s, query);
        if (statusShippeds.isPresent()) {
            List<Bill> listBill = repoBill.TotalStatus(newFromDate, newToDate, statusShippeds.get().getId(), shopInfo.getId());
            if (listBill.size() > 0) {
                for (Bill b : listBill) {
                    DtoBill dtoBill = new DtoBill();
                    dtoBill.setShopShipMoney(b.getShopShipMoney());
                    dtoBill.setCodeBill(b.getCodeBill());
                    dtoBill.setCustomerDistrict(b.getCustomer().getDistrict());
                    dtoBill.setCustomerName(b.getCustomer().getTitle());
                    dtoBill.setCustomerFulladdress(b.getCustomer().getAddress());
                    dtoBill.setCreatedDate(b.getCreatedDate());
                    dtoBill.setIsActive(b.getIsActive());
                    dtoBill.setViewStatus(b.getViewStatus());
                    dtoBill.setStatus(b.getStatus());
                    dtoBill.setCustomerWard(b.getCustomer().getWard());
                    dtoBill.setCustomerProvince(b.getCustomer().getProvince());
                    dtoBill.setCustomerPhone(b.getCustomer().getPhone());
                    dtoBill.setId(b.getId());
                    dtoBill.setShopId(b.getShop().getId());
                    dtoBill.setShipPartner(b.getShipPartner());
                    dtoBill.setWeight(b.getWeight());
                    dtoBill.setTotalMoney(b.getTotalMoney());
                    dtoBill.setChannel(b.getChannel());
                    dtoBill.setOrderCode(b.getOrderCode());
                    dtoBill.setIpAddress(b.getIpAddress());
                    dtoBill.setStatusName(b.getStatusShipped().getStatusName());
                    List<DtoOrderDetail> BillDetail = new ArrayList();
                    for (BillDetail bd : b.getBillDetails()) {
                        try {
                            DtoOrderDetail dtoBillDetail = new DtoOrderDetail();
                            dtoBillDetail = mapperBillDetail.toDto(bd);
                            dtoBillDetail.setProductId(bd.getProduct().getId());
                            dtoBillDetail.setVariantId(bd.getVariant().getId());
                            dtoBillDetail.setProductTitle(bd.getProduct().getTitle());
                            dtoBillDetail.setProperties(bd.getProperties());
                            dtoBillDetail.setChannel(bd.getChannel());
                            dtoBillDetail.setProductImages(bd.getProduct().getImages());
                            BillDetail.add(dtoBillDetail);
                        } catch (Exception e) {
                            DtoOrderDetail dtoBillDetail = new DtoOrderDetail();
                            BillDetail.add(dtoBillDetail);
                        }

                    }
                    dtoBill.setOrderDetails(BillDetail);
                    listDtoBill.add(dtoBill);
                }
                return listDtoBill.stream()
                        .sorted(Comparator.comparing(DtoBill::getId).reversed())
                        .collect(Collectors.toList());
            }
        }
        if (customer.size() > 0) {
            for (Customer c: customer) {
                List<Bill> listBill1 = repoBill.findAllByShopIdAndCustomerId(shopInfo.getId(), c.getId());
                if (listBill1.size() > 0) {
                    for (Bill b : listBill1) {
                        DtoBill dtoBill = new DtoBill();
                        dtoBill.setShopShipMoney(b.getShopShipMoney());
                        dtoBill.setCodeBill(b.getCodeBill());
                        dtoBill.setCustomerDistrict(b.getCustomer().getDistrict());
                        dtoBill.setCustomerName(b.getCustomer().getTitle());
                        dtoBill.setCustomerFulladdress(b.getCustomer().getAddress());
                        dtoBill.setCreatedDate(b.getCreatedDate());
                        dtoBill.setIsActive(b.getIsActive());
                        dtoBill.setViewStatus(b.getViewStatus());
                        dtoBill.setStatus(b.getStatus());
                        dtoBill.setCustomerWard(b.getCustomer().getWard());
                        dtoBill.setCustomerProvince(b.getCustomer().getProvince());
                        dtoBill.setCustomerPhone(b.getCustomer().getPhone());
                        dtoBill.setId(b.getId());
                        dtoBill.setShopId(b.getShop().getId());
                        dtoBill.setShipPartner(b.getShipPartner());
                        dtoBill.setWeight(b.getWeight());
                        dtoBill.setTotalMoney(b.getTotalMoney());
                        dtoBill.setChannel(b.getChannel());
                        dtoBill.setOrderCode(b.getOrderCode());
                        dtoBill.setIpAddress(b.getIpAddress());
                        dtoBill.setStatusName(b.getStatusShipped().getStatusName());
                        List<DtoOrderDetail> BillDetail = new ArrayList();
                        for (BillDetail bd : b.getBillDetails()) {
                            try {
                                DtoOrderDetail dtoBillDetail = new DtoOrderDetail();
                                dtoBillDetail = mapperBillDetail.toDto(bd);
                                dtoBillDetail.setProductId(bd.getProduct().getId());
                                dtoBillDetail.setVariantId(bd.getVariant().getId());
                                dtoBillDetail.setProductTitle(bd.getProduct().getTitle());
                                dtoBillDetail.setProperties(bd.getProperties());
                                dtoBillDetail.setChannel(bd.getChannel());
                                dtoBillDetail.setProductImages(bd.getProduct().getImages());
                                BillDetail.add(dtoBillDetail);
                            } catch (Exception e) {
                                DtoOrderDetail dtoBillDetail = new DtoOrderDetail();
                                BillDetail.add(dtoBillDetail);
                            }

                        }
                        dtoBill.setOrderDetails(BillDetail);
                        listDtoBill.add(dtoBill);
                    }
                    return listDtoBill.stream()
                            .sorted(Comparator.comparing(DtoBill::getId).reversed())
                            .collect(Collectors.toList());
                }
            }

        }
        if (listBillQuery.size() > 0) {
            return getDtoBills(listDtoBill, listBillQuery);
        }
        return listDtoBill;
    }

    public List<DtoBill> getBillOfShopByQueryPage(ShopInfo shopInfo, String s, String query, String month, Pageable pageable) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String[] newData;
        newData = month.split("/");
        String fromData = newData[0];
        String toData = newData[1];
        String fromDate = fromData.concat(" 00:00:00");
        String toDate = toData.concat(" 23:59:59");
        Date newFromDate = formatter.parse(fromDate);
        Date newToDate = formatter.parse(toDate);
        LocalDate FromDate = newFromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate ToDate = newToDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int fromMonth = ((LocalDate) FromDate).getMonthValue();
        int toMonth = ((LocalDate) ToDate).getMonthValue();
        List<DtoBill> listDtoBill = new ArrayList<>();
        Optional<StatusShipped> statusShippeds = repoStatus.findByStatusName(query);
        List<Customer> customer = repoCustomer.findByShopIdAndPhoneOrTitle(shopInfo.getId(),s, query);
        List<Bill> listBillQuery = repoBill.findAllByOrderCode(query);
        Optional<Bill> bill = repoBill.findByCodeBillContainingOrChannelContaining(s, query);
        if (statusShippeds.isPresent()) {
            List<Bill> listBill = repoBill.TotalStatusPage(newFromDate, newToDate, statusShippeds.get().getId(), shopInfo.getId(), pageable);
            if (listBill.size() > 0) {
                for (Bill b : listBill) {
                    DtoBill dtoBill = new DtoBill();
                    dtoBill.setShopShipMoney(b.getShopShipMoney());
                    dtoBill.setCodeBill(b.getCodeBill());
                    dtoBill.setCustomerDistrict(b.getCustomer().getDistrict());
                    dtoBill.setCustomerName(b.getCustomer().getTitle());
                    dtoBill.setCustomerFulladdress(b.getCustomer().getAddress());
                    dtoBill.setCreatedDate(b.getCreatedDate());
                    dtoBill.setIsActive(b.getIsActive());
                    dtoBill.setViewStatus(b.getViewStatus());
                    dtoBill.setStatus(b.getStatus());
                    dtoBill.setCustomerWard(b.getCustomer().getWard());
                    dtoBill.setCustomerProvince(b.getCustomer().getProvince());
                    dtoBill.setCustomerPhone(b.getCustomer().getPhone());
                    dtoBill.setId(b.getId());
                    dtoBill.setShopId(b.getShop().getId());
                    dtoBill.setShipPartner(b.getShipPartner());
                    dtoBill.setWeight(b.getWeight());
                    dtoBill.setTotalMoney(b.getTotalMoney());
                    dtoBill.setChannel(b.getChannel());
                    dtoBill.setOrderCode(b.getOrderCode());
                    dtoBill.setIpAddress(b.getIpAddress());
                    dtoBill.setStatusName(b.getStatusShipped().getStatusName());
                    List<DtoOrderDetail> BillDetail = new ArrayList();
                    for (BillDetail bd : b.getBillDetails()) {
                        try {
                            DtoOrderDetail dtoBillDetail = new DtoOrderDetail();
                            dtoBillDetail = mapperBillDetail.toDto(bd);
                            dtoBillDetail.setProductId(bd.getProduct().getId());
                            dtoBillDetail.setVariantId(bd.getVariant().getId());
                            dtoBillDetail.setProductTitle(bd.getProduct().getTitle());
                            dtoBillDetail.setProperties(bd.getProperties());
                            dtoBillDetail.setChannel(bd.getChannel());
                            dtoBillDetail.setProductImages(bd.getProduct().getImages());
                            BillDetail.add(dtoBillDetail);
                        } catch (Exception e) {
                            DtoOrderDetail dtoBillDetail = new DtoOrderDetail();
                            BillDetail.add(dtoBillDetail);
                        }
                    }
                    dtoBill.setOrderDetails(BillDetail);
                    listDtoBill.add(dtoBill);
                }
                return listDtoBill.stream()
                        .sorted(Comparator.comparing(DtoBill::getId).reversed())
                        .collect(Collectors.toList());
            }
        }

        if (customer.size() > 0) {
            for (Customer c: customer) {
                List<Bill> listBill1 = repoBill.findAllByShopIdAndCustomerId(shopInfo.getId(), c.getId(), pageable);
                if (listBill1.size() > 0) {
                    for (Bill b : listBill1) {
                        DtoBill dtoBill = new DtoBill();
                        dtoBill.setShopShipMoney(b.getShopShipMoney());
                        dtoBill.setCodeBill(b.getCodeBill());
                        dtoBill.setCustomerDistrict(b.getCustomer().getDistrict());
                        dtoBill.setCustomerName(b.getCustomer().getTitle());
                        dtoBill.setCustomerFulladdress(b.getCustomer().getAddress());
                        dtoBill.setCreatedDate(b.getCreatedDate());
                        dtoBill.setIsActive(b.getIsActive());
                        dtoBill.setViewStatus(b.getViewStatus());
                        dtoBill.setStatus(b.getStatus());
                        dtoBill.setCustomerWard(b.getCustomer().getWard());
                        dtoBill.setCustomerProvince(b.getCustomer().getProvince());
                        dtoBill.setCustomerPhone(b.getCustomer().getPhone());
                        dtoBill.setId(b.getId());
                        dtoBill.setShopId(b.getShop().getId());
                        dtoBill.setShipPartner(b.getShipPartner());
                        dtoBill.setWeight(b.getWeight());
                        dtoBill.setTotalMoney(b.getTotalMoney());
                        dtoBill.setChannel(b.getChannel());
                        dtoBill.setOrderCode(b.getOrderCode());
                        dtoBill.setIpAddress(b.getIpAddress());
                        dtoBill.setStatusName(b.getStatusShipped().getStatusName());
                        List<DtoOrderDetail> BillDetail = new ArrayList();
                        for (BillDetail bd : b.getBillDetails()) {
                            try {
                                DtoOrderDetail dtoBillDetail = new DtoOrderDetail();
                                dtoBillDetail = mapperBillDetail.toDto(bd);
                                dtoBillDetail.setProductId(bd.getProduct().getId());
                                dtoBillDetail.setVariantId(bd.getVariant().getId());
                                dtoBillDetail.setProductTitle(bd.getProduct().getTitle());
                                dtoBillDetail.setProperties(bd.getProperties());
                                dtoBillDetail.setChannel(bd.getChannel());
                                dtoBillDetail.setProductImages(bd.getProduct().getImages());
                                BillDetail.add(dtoBillDetail);
                            } catch (Exception e) {
                                DtoOrderDetail dtoBillDetail = new DtoOrderDetail();
                                BillDetail.add(dtoBillDetail);
                            }

                        }
                        dtoBill.setOrderDetails(BillDetail);
                        listDtoBill.add(dtoBill);
                    }
                    return listDtoBill.stream()
                            .sorted(Comparator.comparing(DtoBill::getId).reversed())
                            .collect(Collectors.toList());
                }
            }

        }


//        if (customer.isPresent()) {
//            List<Bill> listBill1 = repoBill.findAllByShopIdAndCustomerId(shopInfo.getId(), customer.get().getId(), pageable);
//            if (listBill1.size() > 0) {
//                for (Bill b : listBill1) {
//                    DtoBill dtoBill = new DtoBill();
//                    dtoBill.setShopShipMoney(b.getShopShipMoney());
//                    dtoBill.setCodeBill(b.getCodeBill());
//                    dtoBill.setCustomerDistrict(b.getCustomer().getDistrict());
//                    dtoBill.setCustomerName(b.getCustomer().getTitle());
//                    dtoBill.setCustomerFulladdress(b.getCustomer().getAddress());
//                    dtoBill.setCreatedDate(b.getCreatedDate());
//                    dtoBill.setIsActive(b.getIsActive());
//                    dtoBill.setViewStatus(b.getViewStatus());
//                    dtoBill.setStatus(b.getStatus());
//                    dtoBill.setCustomerWard(b.getCustomer().getWard());
//                    dtoBill.setCustomerProvince(b.getCustomer().getProvince());
//                    dtoBill.setCustomerPhone(b.getCustomer().getPhone());
//                    dtoBill.setId(b.getId());
//                    dtoBill.setShopId(b.getShop().getId());
//                    dtoBill.setShipPartner(b.getShipPartner());
//                    dtoBill.setWeight(b.getWeight());
//                    dtoBill.setTotalMoney(b.getTotalMoney());
//                    dtoBill.setChannel(b.getChannel());
//                    dtoBill.setOrderCode(b.getOrderCode());
//                    dtoBill.setIpAddress(b.getIpAddress());
//                    dtoBill.setStatusName(b.getStatusShipped().getStatusName());
//                    List<DtoOrderDetail> BillDetail = new ArrayList();
//                    for (BillDetail bd : b.getBillDetails()) {
//                        try {
//                            DtoOrderDetail dtoBillDetail = new DtoOrderDetail();
//                            dtoBillDetail = mapperBillDetail.toDto(bd);
//                            dtoBillDetail.setProductId(bd.getProduct().getId());
//                            dtoBillDetail.setVariantId(bd.getVariant().getId());
//                            dtoBillDetail.setProductTitle(bd.getProduct().getTitle());
//                            dtoBillDetail.setProperties(bd.getProperties());
//                            dtoBillDetail.setChannel(bd.getChannel());
//                            dtoBillDetail.setProductImages(bd.getProduct().getImages());
//                            BillDetail.add(dtoBillDetail);
//                        } catch (Exception e) {
//                            DtoOrderDetail dtoBillDetail = new DtoOrderDetail();
//                            BillDetail.add(dtoBillDetail);
//                        }
//                    }
//                    dtoBill.setOrderDetails(BillDetail);
//                    listDtoBill.add(dtoBill);
//                }
//                return listDtoBill.stream()
//                        .sorted(Comparator.comparing(DtoBill::getId).reversed())
//                        .collect(Collectors.toList());
//            }
//        }
        if (listBillQuery.size() > 0) {
            return getDtoBills(listDtoBill, listBillQuery);
        }
        return listDtoBill;
    }

    public List<DtoBill> getBillOfShop(ShopInfo shopInfo, String month) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String[] newData;
        newData = month.split("/");
        String fromData = newData[0];
        String toData = newData[1];
        String fromDate = fromData.concat(" 00:00:00");
        String toDate = toData.concat(" 23:59:59");
        Date newFromDate = formatter.parse(fromDate);
        Date newToDate = formatter.parse(toDate);
        LocalDate FromDate = newFromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate ToDate = newToDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int fromMonth = ((LocalDate) FromDate).getMonthValue();
        int toMonth = ((LocalDate) ToDate).getMonthValue();
        List<DtoBill> listDtoBill = new ArrayList<>();
        List<Bill> listBill = repoBill.reportRenvenueOfShopTimeAbout(newFromDate, newToDate, shopInfo.getId());
        return getDtoBills(listDtoBill, listBill);
    }

    public List<DtoBill> getBillOfShopPage(ShopInfo shopInfo, String month, Pageable pageable) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String[] newData;
        newData = month.split("/");
        String fromData = newData[0];
        String toData = newData[1];
        String fromDate = fromData.concat(" 00:00:00");
        String toDate = toData.concat(" 23:59:59");
        Date newFromDate = formatter.parse(fromDate);
        Date newToDate = formatter.parse(toDate);
        LocalDate FromDate = newFromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate ToDate = newToDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int fromMonth = ((LocalDate) FromDate).getMonthValue();
        int toMonth = ((LocalDate) ToDate).getMonthValue();
        List<DtoBill> listDtoBill = new ArrayList<>();
        List<Bill> listBill = repoBill.reportRenvenueOfShopTimeAboutPage(newFromDate, newToDate, shopInfo.getId(), pageable);
        return getDtoBills(listDtoBill, listBill);
    }

    private List<DtoBill> getDtoBills(List<DtoBill> listDtoBill, List<Bill> listBill) {
        for (Bill b : listBill) {
            DtoBill dtoBill = new DtoBill();
            dtoBill.setShopShipMoney(b.getShopShipMoney());
            dtoBill.setCodeBill(b.getCodeBill());
            dtoBill.setCustomerDistrict(b.getCustomer().getDistrict());
            dtoBill.setCustomerName(b.getCustomer().getTitle());
            dtoBill.setCustomerFulladdress(b.getCustomer().getAddress());
            dtoBill.setCreatedDate(b.getCreatedDate());
            dtoBill.setIsActive(b.getIsActive());
            dtoBill.setViewStatus(b.getViewStatus());
            dtoBill.setStatus(b.getStatus());
            dtoBill.setCustomerWard(b.getCustomer().getWard());
            dtoBill.setCustomerProvince(b.getCustomer().getProvince());
            dtoBill.setCustomerPhone(b.getCustomer().getPhone());
            dtoBill.setId(b.getId());
            dtoBill.setShopId(b.getShop().getId());
            dtoBill.setShipPartner(b.getShipPartner());
            dtoBill.setWeight(b.getWeight());
            dtoBill.setTotalMoney(b.getTotalMoney());
            dtoBill.setChannel(b.getChannel());
            dtoBill.setOrderCode(b.getOrderCode());
            dtoBill.setIpAddress(b.getIpAddress());
            dtoBill.setStatusName(b.getStatusShipped().getStatusName());
            List<DtoOrderDetail> BillDetail = new ArrayList();
            for (BillDetail bd : b.getBillDetails()) {
                try {
                    DtoOrderDetail dtoBillDetail = new DtoOrderDetail();
                    dtoBillDetail = mapperBillDetail.toDto(bd);
                    dtoBillDetail.setProductId(bd.getProduct().getId());
                    dtoBillDetail.setProductTitle(bd.getProduct().getTitle());
                    dtoBillDetail.setProductImages(bd.getProduct().getImages());
                    dtoBillDetail.setVariantId(bd.getVariant().getId());
                    dtoBillDetail.setProperties(bd.getProperties());
                    dtoBillDetail.setChannel(bd.getChannel());
                    BillDetail.add(dtoBillDetail);
                } catch (NullPointerException n) {
                    DtoOrderDetail dtoBillDetail = new DtoOrderDetail();
                    BillDetail.add(dtoBillDetail);
                }
            }
            dtoBill.setOrderDetails(BillDetail);
            listDtoBill.add(dtoBill);
        }
        return listDtoBill.stream()
                .sorted(Comparator.comparing(DtoBill::getId).reversed())
                .collect(Collectors.toList());
    }

    public ResponseEntity getDetailBill(ShopInfo shopInfo, Long id) {
        Optional<Bill> opBill = repoBill.findByShopIdAndId(shopInfo.getId(), id);
        Map<String, Object> response = new HashMap<>();
        Bill bill = opBill.get();
        if (!opBill.isPresent()) {
            response.put("message", "Không tìm thấy thông tin phiếu mua hàng");
            response.put("success", "false");
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        } else {
            DtoBill dtoBill = new DtoBill();
            dtoBill.setCodeBill(bill.getCodeBill());
            dtoBill.setCustomerDistrict(bill.getCustomer().getDistrict());
            dtoBill.setCustomerName(bill.getCustomer().getTitle());
            dtoBill.setCustomerFulladdress(bill.getCustomer().getAddress());
            dtoBill.setCreatedDate(bill.getCreatedDate());
            dtoBill.setIsActive(bill.getIsActive());
            dtoBill.setViewStatus(bill.getViewStatus());
            dtoBill.setStatus(bill.getStatus());
            dtoBill.setCustomerWard(bill.getCustomer().getWard());
            dtoBill.setCashMoney(bill.getCashMoney());
            dtoBill.setCustomerProvince(bill.getCustomer().getProvince());
            dtoBill.setDiscountMoney(bill.getDiscountMoney());
            dtoBill.setCustomerPhone(bill.getCustomer().getPhone());
            dtoBill.setId(bill.getId());
            dtoBill.setDiscountMoney(bill.getDiscountMoney());
            dtoBill.setDiscountPercent(bill.getDiscountPercent());
            dtoBill.setShipFee(bill.getShipFee());
            dtoBill.setShipPartner(bill.getShipPartner());
            dtoBill.setWeight(bill.getWeight());
            dtoBill.setShipType(bill.getShipType());
            dtoBill.setShopShipMoney(bill.getShopShipMoney());
            dtoBill.setInnerNote(bill.getInnerNote());
            dtoBill.setPaidMoney(bill.getPaidMoney());
            dtoBill.setPrintNote(bill.getPrintNote());
            dtoBill.setTotalMoney(bill.getTotalMoney());
            dtoBill.setChannel(bill.getChannel());
            dtoBill.setOrderCode(bill.getOrderCode());
            dtoBill.setShopId(bill.getShop().getId());
            dtoBill.setShopName(bill.getShop().getTitle());
            dtoBill.setShopPhone(bill.getShop().getPhone());
            dtoBill.setShopFulladdress(bill.getShop().getAddress());
            dtoBill.setShopWard(bill.getShop().getWard());
            dtoBill.setShopDistrict(bill.getShop().getDistrict());
            dtoBill.setShopProvince(bill.getShop().getProvince());
            dtoBill.setStatusName(bill.getStatusShipped().getStatusName());
            List<DtoOrderDetail> BillDetail = new ArrayList();
            for (BillDetail b : bill.getBillDetails()) {
                try {
                    DtoOrderDetail dtoBillDetail = new DtoOrderDetail();
                    dtoBillDetail = mapperBillDetail.toDto(b);
                    dtoBillDetail.setProductId(b.getProduct().getId());
                    dtoBillDetail.setVariantId(b.getVariant().getId());
                    dtoBillDetail.setProductTitle(b.getProduct().getTitle());
                    dtoBillDetail.setProperties(b.getProperties());
                    dtoBillDetail.setChannel(b.getChannel());
                    dtoBillDetail.setProductImages(b.getProduct().getImages());
                    dtoBillDetail.setProperties(b.getProperties());
                    dtoBillDetail.setWeightProduct(b.getVariant().getWeight());
                    BillDetail.add(dtoBillDetail);
                }catch (Exception e){
                    DtoOrderDetail dtoBillDetail = new DtoOrderDetail();
                    BillDetail.add(dtoBillDetail);
                }
            }
            dtoBill.setOrderDetails(BillDetail);
            response.put("data", dtoBill);
            response.put("message", "Thông tin phiếu mua hàng");
            response.put("success", "true");
            return new ResponseEntity(response, HttpStatus.OK);
        }
    }

    public ResponseEntity getDetailBill2(ShopInfo shopInfo, Long id) {
        Optional<Bill> opBill = repoBill.findByShopIdAndId(shopInfo.getId(), id);
        Map<String, Object> response = new HashMap<>();
        Bill bill = opBill.get();
        if (!opBill.isPresent()) {
            response.put("message", "Không tìm thấy thông tin phiếu mua hàng");
            response.put("success", "false");
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        } else {
            DtoBill dtoBill = new DtoBill();
            dtoBill.setCodeBill(bill.getCodeBill());
            dtoBill.setCustomerDistrict(bill.getCustomer().getDistrict());
            dtoBill.setCustomerName(bill.getCustomer().getTitle());
            dtoBill.setCustomerFulladdress(bill.getCustomer().getAddress());
            dtoBill.setCreatedDate(bill.getCreatedDate());
            dtoBill.setIsActive(bill.getIsActive());
            dtoBill.setViewStatus(bill.getViewStatus());
            dtoBill.setStatus(bill.getStatus());
            dtoBill.setCustomerWard(bill.getCustomer().getWard());
            dtoBill.setCashMoney(bill.getCashMoney());
            dtoBill.setCustomerProvince(bill.getCustomer().getProvince());
            dtoBill.setDiscountMoney(bill.getDiscountMoney());
            dtoBill.setCustomerPhone(bill.getCustomer().getPhone());
            dtoBill.setId(bill.getId());
            dtoBill.setDiscountMoney(bill.getDiscountMoney());
            dtoBill.setDiscountPercent(bill.getDiscountPercent());
            dtoBill.setShipFee(bill.getShipFee());
            dtoBill.setShipPartner(bill.getShipPartner());
            dtoBill.setWeight(bill.getWeight());
            dtoBill.setShipType(bill.getShipType());
            dtoBill.setShopShipMoney(bill.getShopShipMoney());
            dtoBill.setInnerNote(bill.getInnerNote());
            dtoBill.setPaidMoney(bill.getPaidMoney());
            dtoBill.setPrintNote(bill.getPrintNote());
            dtoBill.setTotalMoney(bill.getTotalMoney());
            dtoBill.setChannel(bill.getChannel());
            dtoBill.setOrderCode(bill.getOrderCode());
            dtoBill.setShopId(bill.getShop().getId());
            dtoBill.setShopName(bill.getShop().getTitle());
            dtoBill.setShopPhone(bill.getShop().getPhone());
            dtoBill.setShopFulladdress(bill.getShop().getAddress());
            dtoBill.setShopWard(bill.getShop().getWard());
            dtoBill.setShopDistrict(bill.getShop().getDistrict());
            dtoBill.setShopProvince(bill.getShop().getProvince());
            dtoBill.setStatusName(bill.getStatusShipped().getStatusName());
            List<DtoOrderProduct> BillDetail = new ArrayList();
            for (BillDetail b : bill.getBillDetails()) {
                try {
                    DtoOrderProduct dtoBillDetail = new DtoOrderProduct();
                    dtoBillDetail.setId(b.getId());
                    dtoBillDetail.setProductId(b.getProduct().getId());
                    dtoBillDetail.setVariantId(b.getVariant().getId());
                    dtoBillDetail.setProductName(b.getProduct().getTitle());
                    dtoBillDetail.setProductSku(b.getProduct().getSku());
                    dtoBillDetail.setSku(b.getVariant().getSku());
                    dtoBillDetail.setProperties(b.getProperties());
                    dtoBillDetail.setChannel(b.getChannel());
                    dtoBillDetail.setImage(b.getVariant().getImage());
                    dtoBillDetail.setProperties(b.getProperties());
                    dtoBillDetail.setWeight(b.getVariant().getWeight());
                    dtoBillDetail.setPrice(b.getVariant().getPrice());
                    dtoBillDetail.setSaleprice(b.getVariant().getSaleprice());
                    dtoBillDetail.setInputQuantity(b.getQuantity());

                    BillDetail.add(dtoBillDetail);
                }catch (Exception e){
                    DtoOrderProduct dtoBillDetail = new DtoOrderProduct();
                    BillDetail.add(dtoBillDetail);
                }
            }
            dtoBill.setDtoOrderProducts(BillDetail);
            response.put("data", dtoBill);
            response.put("message", "Thông tin phiếu mua hàng");
            response.put("success", "true");
            return new ResponseEntity(response, HttpStatus.OK);
        }
    }

    public Page<Bill> getAllBillInMonth(String channel, String shipPartner, String query, Pageable pageable) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
            if (query == null) {
                Date date = new Date();
                String from = formatter.format(date);
                String to = formatter.format(date);
                query = from + "/" + to;
            }
            String[] newData = query.split("/");
            String fromData = newData[0];
            String toData = newData[1];
            String fromDate = fromData.concat(" 00:00:00");
            String toDate = toData.concat(" 23:59:59");

            Pageable pagingSort = PageRequest.of(pageable.getPageNumber(), 10);
            if (channel != null && shipPartner != null) {
                Page<Bill> dataBill = repoBill.findAllWithCreationDateTimeBefore(channel, shipPartner, fromDate, toDate, pagingSort);
                return dataBill;
            } else if (channel != null && shipPartner == null) {
                Page<Bill> datas = repoBill.findAllChannelAndDate(channel, fromDate, toDate, pagingSort);
                return datas;
            } else if (channel == null && shipPartner != null) {
                Page<Bill> data = repoBill.findAllShipAndDate(shipPartner, fromDate, toDate, pagingSort);
                return data;
            } else if ((channel == null && shipPartner == null)) {
                Page<Bill> data = repoBill.findAllDate(fromDate, toDate, pagingSort);
                return data;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public TotalBillDTO findBillByDate(String fromDate, String toDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        TotalBillDTO totalBillDTO = new TotalBillDTO();
        Date newFromDate = formatter.parse(fromDate);
        Date newToDate = formatter.parse(toDate);
        List<Bill> bills = repoBill.findAllWithCreationDateTimeAbout(newFromDate, newToDate);
        List<DtoBill> dtoBills = new ArrayList<>();
        totalBillDTO.setBills(getDtoBills(dtoBills, bills));
        Double totalMoney = 0.0;
        Double totalShipped = 0.0;
        Double money = 0.0;
        for (Bill b : bills) {
            totalShipped = totalShipped + b.getShopShipMoney();
            totalMoney = totalMoney + b.getTotalMoney();
        }
        money = totalMoney - totalShipped;
        totalBillDTO.setTotalShipped(totalShipped);
        totalBillDTO.setTotalRevenue(totalMoney);
        totalBillDTO.setMoney(money);
        return totalBillDTO;
    }

    private List<DtoBillStatus> getDtoBillStatus(List<DtoBillStatus> listDtoBill, List<Bill> listBill) {
        for (Bill b : listBill) {
            DtoBillStatus dtoBill = new DtoBillStatus();
            dtoBill.Bill(b.getId(), b.getTotalMoney(), b.getCustomer().getTitle(), b.getCustomer().getPhone(),
                    b.getIsActive(), b.getCreatedDate(),
                    b.getShipPartner(), b.getStatus(), b.getChannel(), b.getCodeBill());
            listDtoBill.add(dtoBill);
        }
        return listDtoBill.stream()
                .sorted(Comparator.comparing(DtoBillStatus::getId).reversed())
                .collect(Collectors.toList());
    }

    public List<DtoBillStatus> getAllBillByShopId(Long shopId, Long statusId) {
        List<Bill> bills = repoBill.findAllByShopIdAndStatusShippedId(shopId, statusId);
        List<DtoBillStatus> billStatuses = new ArrayList<>();
        return getDtoBillStatus(billStatuses, bills);
    }

    public ResponseEntity updateStatusById(Long shopId, Long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<Bill> opBill = repoBill.findByShopIdAndId(shopId, id);
        Bill bill = opBill.get();
        if (!opBill.isPresent()) {
            response.put("message", "Không tìm thấy thông tin phiếu mua hàng");
            response.put("success", "false");
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        } else {
            Optional<StatusShipped> shipped = repoStatus.findById(2L);
            bill.setStatus(shipped.get().getStatusName());
            bill.setStatusShipped(shipped.get());
            repoBill.save(bill);
            response.put("success", "true");
            return new ResponseEntity(response, HttpStatus.OK);
        }
    }

    //    @EventListener
    public ResponseEntity updateListStatus(Long shopId, List<Long> id) {
        Map<String, Object> response = new HashMap<>();
        Optional<StatusShipped> shipped = repoStatus.findById(2L);
        for (int i = 0; i < id.size(); i++) {
            Bill bill1 = new Bill();
            Optional<Bill> bill = repoBill.findByShopIdAndId(shopId, id.get(i));
            bill1 = bill.get();
            bill1.setStatus(shipped.get().getStatusName());
            bill1.setStatusShipped(shipped.get());
            bill1.setIsActive(true);
            bill1.setViewStatus(true);
            repoBill.save(bill1);
            template.convertAndSend("/topic/changestatus/" + shopId, bill1.getId());
        }
        response.put("success", "true");
        return new ResponseEntity(response, HttpStatus.OK);
    }


    // get 10 bill
    public List<DtoBill> getTopBill(Long shopId) {
        Date dateTime = new Date();
        List<DtoBill> dtoBills = new ArrayList<>();
        List<Bill> bills = repoBill.findBill(shopId);
        for (Bill _bill : bills
        ) {
            long _minutes = _bill.getCreatedDate().getTime() - dateTime.getTime();
            if (_minutes < 7200000) {
                Date _ndate = new Date(dateTime.getTime() - 7000000);
                _bill.setCreatedDate(_ndate);
            }
        }
        return getDtoBills(dtoBills, bills);
    }

    public ResponseEntity TotalViewStatus(Long shopId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<DtoBillView> listDtoBill = new ArrayList<>();
            List<Bill> listviewTotal = repoBill.findAllByViewStatusAndShopIdAndStatusShippedId(false, shopId, 1L, Sort.by(Sort.Order.desc("id")));
            List<Bill> listview = repoBill.findAllByStatusShippedIdAndShopId(1L, shopId, Sort.by(Sort.Order.desc("id")));
            for (Bill b : listview) {
                DtoBillView dtoBill = new DtoBillView();
                dtoBill.Bill(b.getId(), b.getStatus(), b.getTotalMoney(), b.getCustomer().getTitle(), b.getCustomer().getPhone(),
                        b.getCustomer().getDistrict(), b.getCustomer().getProvince(), b.getViewStatus(), b.getCreatedDate(), b.getShop().getId(), b.getChannel());
                listDtoBill.add(dtoBill);
            }
            response.put("total", listviewTotal.size());
            response.put("data", listDtoBill);
            response.put("success", "true");
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "không có thông tin");
            response.put("success", "false");
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity deleteBill(Long billId, Long shopId) {
        Optional<Bill> bill = repoBill.findByShopIdAndId(shopId, billId);

        Map<Object, String> response = new HashMap<>();
        if (!bill.isPresent()) {
            response.put("message", "Không tìm thấy bill của shop");
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        Optional<MapGHN> mapGHN = repoMapGHN.findByBillId(billId);
        Optional<MapGHTK> mapGHTK = repoMapGHTK.findByBillId(billId);
        Optional<MapViettel> mapViettel = repoMapViettel.findByBillId(billId);
        if (mapGHN.isPresent()) {
            repoMapGHN.delete(mapGHN.get());
        } else if (mapGHTK.isPresent()) {
            repoMapGHTK.delete(mapGHTK.get());
        } else if (mapViettel.isPresent()) {
            repoMapViettel.delete(mapViettel.get());
        }
        List<BillDetail> billDetails = new ArrayList<>();
        for (BillDetail b : bill.get().getBillDetails()) {
            billDetails.add(b);
        }
        repoBillDetail.deleteAll(billDetails);
        repoBill.delete(bill.get());
        template.convertAndSend("/topic/deleteBill/" + shopId, bill.get().getId());
        response.put("message", "delete thành công");
        return new ResponseEntity(response, HttpStatus.OK);
    }


    public ResponseEntity updateViewBill(Long shopId, Long id) {
        Optional<Bill> opBill = repoBill.findByShopIdAndId(shopId, id);
        Map<String, Object> response = new HashMap<>();
        Bill bill = opBill.get();
        if (!opBill.isPresent()) {
            response.put("message", "Không tìm thấy thông tin");
            response.put("success", "false");
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        } else {
            bill.setViewStatus(true);
            Bill updateBill = repoBill.save(bill);
            template.convertAndSend("/topic/viewtrue/" + shopId, bill.getId());
            response.put("data", updateBill);
            response.put("message", "Cập nhật thành công đơn hàng");
            response.put("success", "true");
            return new ResponseEntity(response, HttpStatus.OK);
        }
    }

    public Double formatStringToDouble(String data) {
        Double price = 0.0;
        String originPrice = data;
        if (originPrice != null && originPrice.length() > 0) {
            price = Double.parseDouble(originPrice);
        }
        return price;
    }

    public ResponseEntity<?> updateBill(Long shopId, DtoBillUpdate dtoBill) {
        Map<String, Object> response = new HashMap<>();
        Optional<Bill> bills = repoBill.findByShopIdAndId(shopId, dtoBill.getBillId());
        if (bills.isPresent()) {
            Bill bill = bills.get();
            Double shipFee = formatStringToDouble(dtoBill.getShipFee());
            Double totalMoney = dtoBill.getTotalMoney();
            Double shopShipMoney = totalMoney + shipFee;
            Double cashMoney = dtoBill.getTotalMoney();
            bill.setTotalMoney(totalMoney);
            bill.setShopShipMoney(shopShipMoney);
            bill.setWeight(dtoBill.getWeight());
            bill.setCashMoney(cashMoney);

            Customer customer = bills.get().getCustomer();
            if (dtoBill.getCustomerName() != null) customer.setTitle(dtoBill.getCustomerName());
            if (dtoBill.getCustomerPhone() != null) customer.setPhone(dtoBill.getCustomerPhone());
            if (dtoBill.getCustomerFulladdress() != null) customer.setAddress(dtoBill.getCustomerFulladdress());
            if (dtoBill.getCustomerProvince() != null) customer.setProvince(dtoBill.getCustomerProvince());
            if (dtoBill.getCustomerDistrict() != null) customer.setDistrict(dtoBill.getCustomerDistrict());
            if (dtoBill.getCustomerWard() != null) customer.setWard(dtoBill.getCustomerWard());
            repoCustomer.save(customer);

            for (DtoOrderDetail b : dtoBill.getDtoBillDetail()) {
                Optional<BillDetail> billDetails = repoBillDetail.findById(b.getId());
                if (billDetails.isPresent()) {
                    Optional<Product> product = repoProduct.findById(b.getProductId());
                    Optional<ProductVariations> productVariations = repoProductVariations.findById(b.getVariantId());
                    if (product.isPresent() && productVariations.isPresent()){
                        BillDetail billDetail = billDetails.get();
                        billDetail.setQuantity(b.getQuantity());
                        billDetail.setProduct(product.get());
                        billDetail.setProperties(b.getProperties());
                        billDetail.setMoney(b.getQuantity() * productVariations.get().getSaleprice());
                        billDetail.setVariant(productVariations.get());
                        repoBillDetail.save(billDetail);
                    }else {
                        BillDetail billDetail = billDetails.get();
                        billDetail.setQuantity(b.getQuantity());
                        billDetail.setProperties(b.getProperties());
                        billDetail.setMoney(b.getQuantity() * billDetail.getVariant().getSaleprice());
                        repoBillDetail.save(billDetail);
                    }
                }
            }
//            response.put("data", dtoBill);
            response.put("success", "true");
            response.put("message", "Sửa đơn hàng thành công");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.put("mesager", " Không tìm thấy thông tin Bill!!!!");
        response.put("success", false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}