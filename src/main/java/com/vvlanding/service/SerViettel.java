package com.vvlanding.service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vvlanding.dto.shipped.viettel.*;
import com.vvlanding.repo.*;
import com.vvlanding.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class SerViettel {

    private static final String ORDER = "https://partner.viettelpost.vn/v2/order/createOrder";

    private static final String FEE = "https://partner.viettelpost.vn/v2/order/getPrice";

    private static final String UPDATE_ORDER = "https://partner.viettelpost.vn/v2/order/UpdateOrder";

    private static final String FEE_PROVINCE = "https://partner.viettelpost.vn/v2/categories/listProvinceById?provinceId=-1";

    private static RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private RepoShipped repoShipped;

    @Autowired
    private RepoBill repoBill;

    @Autowired
    private RepoCustomer repoCustomer;

    @Autowired
    private RepoStatus repoStatus;

    @Autowired
    private RepoMapViettel repoMapViettel;

    @Autowired
    private RepoViettel repoViettel;

    @Autowired
    private RepoProvinceViettel repoProvinceViettel;

    @Autowired
    private RepoDistrictViettel repoDistrictViettel;


    public ResponseEntity orderViettel(Long billId,String name, Long shopId,String userName) throws JsonProcessingException, URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> responses = new HashMap<>();
        Optional<Shipped> shipped = repoShipped.findByNameAndShopInfoId(name,shopId);
        Optional<Bill> bill = repoBill.findById(billId);
        if (bill.get().getIsActive() == false){
            responses.put("message","Active false");
            responses.put("success", "false");
            return new ResponseEntity(responses,HttpStatus.BAD_REQUEST);
        }
        Bill bills = bill.get();
        Customer customer = bill.get().getCustomer();
        ShopInfo shopInfo = bill.get().getShop();
        ViettelOrderDTO viettelOrderDTO = new ViettelOrderDTO();

        viettelOrderDTO.setOrderNumber(bill.get().getCodeBill());
        viettelOrderDTO.setGroupAddressId(bill.get().getId());
        viettelOrderDTO.setCusId(customer.getId());

//        viettelOrderDTO.setDeliveryDate(new Date().toString());
        viettelOrderDTO.setSenderFullName(userName);
        viettelOrderDTO.setSenderAddress(shopInfo.getAddress());
        viettelOrderDTO.setSenderPhone(shopInfo.getPhone());
        viettelOrderDTO.setSenderEmail(shopInfo.getEmail());
        ViettelAddressDistrict senderDistrict = getDistrict(shopInfo.getProvince(),shopInfo.getDistrict());
        ViettelAddressDistrict receiverDistrict = getDistrict(customer.getProvince(),customer.getDistrict());

        viettelOrderDTO.setSenderProvince(Long.valueOf(senderDistrict.getProvinceId()));
        viettelOrderDTO.setSenderDistrict(Long.valueOf(senderDistrict.getDistrictId()));
        viettelOrderDTO.setReceiverFullname(customer.getTitle());
        viettelOrderDTO.setReceiverAddress(customer.getAddress());
        viettelOrderDTO.setReceiverProvince(Long.valueOf(receiverDistrict.getProvinceId()));
        viettelOrderDTO.setReceiverDistrict(Long.valueOf(receiverDistrict.getDistrictId()));
        viettelOrderDTO.setReceiverPhone(customer.getPhone());
        viettelOrderDTO.setProduceWeight(Long.valueOf(bill.get().getWeight()));
        viettelOrderDTO.setProductType("HH");
        viettelOrderDTO.setOrderPayment(3L);
        viettelOrderDTO.setOrderService("VCN");
        viettelOrderDTO.setOrderNote(bill.get().getPrintNote());
        List<ListProductOrder> productOrders = new ArrayList<>();
        for (BillDetail b: bill.get().getBillDetails()) {
            ListProductOrder productOrder = new ListProductOrder();
            productOrder.setProductName(b.getProduct().getTitle());
            productOrder.setProductPrice(b.getPrice().longValue());
            productOrder.setProductWeight(b.getVariant().getWeight().longValue());
            productOrder.setProductQuantity(b.getVariant().getQuantity().longValue());
            productOrders.add(productOrder);
        }
        viettelOrderDTO.setListItem(productOrders);
        headers.set("token",shipped.get().getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper mapper = new ObjectMapper();
        mapper = mapper.setSerializationInclusion(Include.NON_NULL);
        mapper = mapper.setSerializationInclusion(Include.NON_DEFAULT);

        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(viettelOrderDTO));

        HttpEntity<ViettelOrderDTO> entity = new HttpEntity<ViettelOrderDTO>(viettelOrderDTO,headers);

        ViettelResponseOrderDTO response = restTemplate.postForObject(new URI(ORDER),entity,ViettelResponseOrderDTO.class);
        if (response.getStatus() == 200) {
            try {
                MapViettel mapViettel = new MapViettel();
                ViettelResponseOrder viettelResponseOrder = new ViettelResponseOrder();

                bills.setShipFee(response.getData().getMoneyTotal());
                bills.setOrderCode(response.getData().getOrderNumber());
                bills.setShopShipMoney(response.getData().getMoneyTotalFee() + bill.get().getTotalMoney());
                bills.setShipPartner("GHVT");
                Optional<StatusShipped> statusShipped = repoStatus.findById(3L);
                bills.setStatus(statusShipped.get().getStatusName());
                bills.setStatusShipped(statusShipped.get());
                repoBill.save(bills);
                viettelResponseOrder.setOrderNumber(response.getData().getOrderNumber());
                viettelResponseOrder.setMoneyCollection(response.getData().getMoneyCollection());
                viettelResponseOrder.setExchangeWeight(response.getData().getExchangeWeight());
                viettelResponseOrder.setMoneyTotal(response.getData().getMoneyTotal());
                viettelResponseOrder.setMoneyTotalFee(response.getData().getMoneyTotalFee());
                viettelResponseOrder.setMoneyFee(response.getData().getMoneyFee());
                viettelResponseOrder.setMoneyCollectionFee(response.getData().getMoneyCollectionFee());
                viettelResponseOrder.setMoneyOtherFee(response.getData().getMoneyOtherFee());
                viettelResponseOrder.setMoneyVas(response.getData().getMoneyVas());
                viettelResponseOrder.setMoneyVat(response.getData().getMoneyVat());
                viettelResponseOrder.setKpiHT(response.getData().getKpiHT());
                repoViettel.save(viettelResponseOrder);
                mapViettel.setShipped(shipped.get());
                mapViettel.setBill(bills);
                mapViettel.setViettelResponseOrder(viettelResponseOrder);
                repoMapViettel.save(mapViettel);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        responses.put("data",response);
        return new ResponseEntity(responses,HttpStatus.OK);
    }

    public ResponseEntity<FeeResponseViettelDTO> fee(FeeViettelDTO feeViettelDTO,Long billId,String name , Long shopId){

        HttpHeaders headers = new HttpHeaders();
        Optional<Shipped> shipped = repoShipped.findByNameAndShopInfoId(name,shopId);
        ShopInfo shopInfo = shipped.get().getShopInfo();
        Optional<Bill> bill = repoBill.findById(billId);
        Customer customer = bill.get().getCustomer();
        headers.set("Token",shipped.get().getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper mapper = new ObjectMapper();

        if (feeViettelDTO.getSenderProvinceName() != null && feeViettelDTO.getSenderDistrictName() != null){
            ViettelAddressDistrict senderDistrict = getDistrict(feeViettelDTO.getSenderProvinceName(),feeViettelDTO.getSenderDistrictName());
            feeViettelDTO.setSENDER_PROVINCE(String.valueOf(senderDistrict.getProvinceId()));
            feeViettelDTO.setSENDER_DISTRICT(String.valueOf(senderDistrict.getDistrictId()));
        }else {
            ViettelAddressDistrict senderDistrict = getDistrict(shopInfo.getProvince(),shopInfo.getDistrict());
            feeViettelDTO.setSENDER_PROVINCE(String.valueOf(senderDistrict.getProvinceId()));
            feeViettelDTO.setSENDER_DISTRICT(String.valueOf(senderDistrict.getDistrictId()));
        }
        ViettelAddressDistrict district = getDistrict(customer.getProvince(),customer.getDistrict());
        feeViettelDTO.setRECEIVER_PROVINCE(String.valueOf(district.getProvinceId()));
        feeViettelDTO.setRECEIVER_DISTRICT(String.valueOf(district.getDistrictId()));
        feeViettelDTO.setORDER_SERVICE("VCN");
        feeViettelDTO.setPRODUCT_WEIGHT(bill.get().getWeight());
        feeViettelDTO.setPRODUCT_PRICE(bill.get().getCashMoney());
        feeViettelDTO.setPRODUCT_TYPE("HH");
        feeViettelDTO.setNATIONAL_TYPE(1);
        try {
            feeViettelDTO.setSenderDistrictName("");
            feeViettelDTO.setSenderProvinceName("");
            feeViettelDTO.setReceiverDistrictName("");
            feeViettelDTO.setReceiverProvinceName("");

            mapper = mapper.setSerializationInclusion(Include.NON_NULL);
            mapper = mapper.setSerializationInclusion(Include.NON_DEFAULT);
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(feeViettelDTO));

            HttpEntity<String> entity = new HttpEntity<String>(mapper.writeValueAsString(feeViettelDTO), headers);

            ResponseEntity<FeeResponseViettelDTO> response = restTemplate.exchange(new URI(FEE),HttpMethod.POST,entity,FeeResponseViettelDTO.class);

            System.out.println(mapper.writeValueAsString(response));
            return response;
        }catch (JsonProcessingException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ResponseEntity<FeeResponseViettelDTO> update(Long id){
        UpdateOrderViettel update = new UpdateOrderViettel();
        HttpHeaders headers = new HttpHeaders();
        Optional<MapViettel> mapViettel = repoMapViettel.findByBillId(id);
        headers.set("Token",mapViettel.get().getShipped().getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        update.setOrderNumber(mapViettel.get().getViettelResponseOrder().getOrderNumber());
        update.setType(4.0);
        update.setNote("");
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper = mapper.setSerializationInclusion(Include.NON_NULL);

            mapper = mapper.setSerializationInclusion(Include.NON_DEFAULT);

            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(update));

            HttpEntity<String> entity = new HttpEntity<String>(mapper.writeValueAsString(update), headers);

            ResponseEntity<FeeResponseViettelDTO> response = restTemplate.exchange(new URI(UPDATE_ORDER),HttpMethod.POST,entity,FeeResponseViettelDTO.class);

            Optional<Bill> bill = repoBill.findById(mapViettel.get().getBill().getId());

            if (response.getBody().getStatus() == 200){

                Bill bills = bill.get();
                Optional<StatusShipped> statusShipped = repoStatus.findById(10L);
                bills.setStatusShipped(statusShipped.get());
                bills.setStatus(statusShipped.get().getStatusName());
                repoBill.save(bills);
            }
            return response;

        }catch (JsonProcessingException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ResponseEntity webhook(ViettelWebhook webhook,String token){
        Map<String, Object> response = new HashMap<>();
        Optional<MapViettel> mapViettel = repoMapViettel.findByViettelResponseOrder_OrderNumber(webhook.getOrderNumber());
        Optional<Shipped> shipped = repoShipped.findByToken(token);
        if (!shipped.isPresent()){
            response.put("mesager", "token không tồn tại !!!!");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (!mapViettel.isPresent()){
            response.put("mesager", "Order Number không tồn tại !!!!");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        Bill bills = mapViettel.get().getBill();
        bills.setStatus(webhook.getStatusName());
        bills.setShopShipMoney(webhook.getMoneyCollection());
        bills.setTotalMoney(webhook.getMoneyTotal());
        int status = webhook.getOrderStatus();
        if (status == -100){
            Optional<StatusShipped> statusShipped = repoStatus.findById(1L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        }else if (status == 100 || status == 101|| status == 102 || status == 103 || status == 104 || status == -108 ){
            Optional<StatusShipped> statusShipped = repoStatus.findById(2L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        }else if (status == 107 || status == 201 ||status==503){
            Optional<StatusShipped> statusShipped = repoStatus.findById(10L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        }else if (status == -109 || status== -110|| status == 105 || status ==200||status==202||status==300||status == 301||status==302||status == 303||status==320||status==400){
            Optional<StatusShipped> statusShipped = repoStatus.findById(3L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        }else if (status==401||status==402||status==403||status == 500||status ==506||status ==570||status ==508||status ==509||status ==550){
            Optional<StatusShipped> statusShipped = repoStatus.findById(4L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        }else if (status ==507||status==510){
            Optional<StatusShipped> statusShipped = repoStatus.findById(9L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        }else if (status == 505||status==106||status==502||status==515){
            Optional<StatusShipped> statusShipped = repoStatus.findById(6L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        }else if (status == 504){
            Optional<StatusShipped> statusShipped = repoStatus.findById(7L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        }
        else if (status == 501){
            Optional<StatusShipped> statusShipped = repoStatus.findById(5L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        }
        response.put("data", webhook);
        response.put("success", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    private Customer checkCustomer(String phone,Long shopId) {
        List<Customer> customerOpt = repoCustomer.findByPhoneAndShopId(phone, shopId);
        if (customerOpt.size() > 0) {
            return customerOpt.get(0);
        }
        return null;
    }
    private ViettelAddressDistrict getDistrict(String province,String district){

        System.out.println("-----" + province +"-------"+district);

        Optional<ViettelAddressProvince> provinces = repoProvinceViettel.findByProvinceName(province);

        Optional<ViettelAddressDistrict> addressDistrict = repoDistrictViettel.findByDistrictNameAndProvinceId(district,provinces.get().getProvinceId());

        return addressDistrict.get();
    }
}
