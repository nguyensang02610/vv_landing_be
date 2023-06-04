package com.vvlanding.service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vvlanding.dto.shipped.ghtk.*;
import com.vvlanding.repo.*;
import com.vvlanding.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class SerGhtkService {

    private static String GHTK_Order = "https://services.giaohangtietkiem.vn/services/shipment/order/?ver=1.5";
    private static String GHTK_Order_TEST = "https://services.giaohangtietkiem.vn/services/shipment/order/?ver=1.5";
    private static String GHTK_Status = "https://services.giaohangtietkiem.vn/services/shipment/v2/";
    private static String GHTK_Cancel = "https://services.giaohangtietkiem.vn/services/shipment/cancel/";
    private static String GHTK_FEE_ENDPOINT = "https://services.giaohangtietkiem.vn/services/shipment/fee";

    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private RepoBill repoBill;

    @Autowired
    private RepoShipped repoShipped;

    @Autowired
    private RepoCustomer repoCustomer;

    @Autowired
    private RepoMapGHTK repoMapGHTK;

    @Autowired
    private RepoShopInfo repoShopInfo;

    @Autowired
    private RepoGHTK repoGHTK;

    @Autowired
    private RepoStatus repoStatus;

    @Autowired
    RepoProductVariations repoProductVariations;

    @Autowired
    RepoBillDetail repoBillDetail;

    @Autowired
    RepoProduct repoProduct;

    @Autowired
    SimpMessagingTemplate template;

    public ResponseEntity<GHTKOrderResponseDTO> createShip(Long billId, String name, Long shopId ,String userName) throws RestClientException{
        Map<String, Object> responses = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();
        DtoGHTKOrder dtoOrder = new DtoGHTKOrder();
        List<DtoGHTKProduct> dtoGHTKProducts = new ArrayList<>();
        DtoGHTK dtoGHTK = new DtoGHTK();

        Optional<Shipped> shipped = repoShipped.findByNameAndShopInfoId(name, shopId);
        ShopInfo shopInfo = shipped.get().getShopInfo();
        headers.set("Token", shipped.get().getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper mapper = new ObjectMapper();

        Optional<Bill> bill = repoBill.findById(billId);
        if (bill.get().getIsActive() == false) {
            responses.put("message", "Active false");
            responses.put("success", "false");
            return new ResponseEntity(responses, HttpStatus.BAD_REQUEST);
        }
        Customer customer = bill.get().getCustomer();
        for (BillDetail b : bill.get().getBillDetails()) {
            DtoGHTKProduct product = new DtoGHTKProduct();
            product.setName(b.getProduct().getTitle());
            product.setWeight(b.getVariant().getWeight());
            product.setQuantity(b.getVariant().getQuantity().intValue());
            product.setPrice(b.getVariant().getSaleprice());
            product.setProductCode(b.getProduct().getId().intValue());
            dtoGHTKProducts.add(product);
        }
        dtoGHTK.setId(bill.get().getCodeBill());
        dtoGHTK.setPickName(userName);
        dtoGHTK.setPickAddress(shopInfo.getAddress());
        dtoGHTK.setPickProvince(shopInfo.getProvince());
        dtoGHTK.setPickDistrict(shopInfo.getDistrict());
        dtoGHTK.setWard(shopInfo.getWard());
        dtoGHTK.setPickTel(shopInfo.getPhone());
        dtoGHTK.setTel(customer.getPhone());
        dtoGHTK.setName(customer.getTitle());
        dtoGHTK.setWeightOption("gram");
        dtoGHTK.setAddress(customer.getAddress());
        dtoGHTK.setProvince(customer.getProvince());
        dtoGHTK.setDistrict(customer.getDistrict());
        dtoGHTK.setWard(customer.getWard());
        dtoGHTK.setHamlet("khác");
        dtoGHTK.setIsFreeship(0);
        dtoGHTK.setPickMoney(bill.get().getTotalMoney().intValue());
        dtoGHTK.setNote(bill.get().getPrintNote());
        dtoGHTK.setValue(bill.get().getTotalMoney().intValue());
        dtoOrder.setProducts(dtoGHTKProducts);
        dtoOrder.setOrder(dtoGHTK);
        try {
            mapper = mapper.setSerializationInclusion(Include.NON_NULL);
            mapper = mapper.setSerializationInclusion(Include.NON_DEFAULT);

            HttpEntity<DtoGHTKOrder> entity = new HttpEntity<DtoGHTKOrder>(dtoOrder, headers);
            String res = restTemplate.postForObject(new URI(GHTK_Order_TEST), entity, String.class);
            GHTKOrderResponseDTO response = mapper.readValue(res,GHTKOrderResponseDTO.class);
            if (response.isSuccess() == true) {
                Bill order = bill.get();
                order.setShipFee(formatStringToDouble(response.getOrder().getFee()));
                order.setOrderCode(response.getOrder().getLabel());
                order.setShipPartner("GHTK");
                order.setShopShipMoney(order.getTotalMoney() + order.getShipFee());
                Optional<StatusShipped> statusShipped = repoStatus.findById(3L);
                order.setStatus(statusShipped.get().getStatusName());
                order.setStatusShipped(statusShipped.get());
                repoBill.save(order);
                MapGHTK mapGHTK = new MapGHTK();
                GHTKOrderResponse ghtkOrderResponse = new GHTKOrderResponse();
                ghtkOrderResponse.setArea(response.getOrder().getArea());
                ghtkOrderResponse.setEstimatedDeliverTime(response.getOrder().getEstimatedDeliverTime());
                ghtkOrderResponse.setEstimatedPickTime(response.getOrder().getEstimatedPickTime());
                ghtkOrderResponse.setFee(response.getOrder().getFee());
                ghtkOrderResponse.setInsuranceFee(response.getOrder().getInsuranceFee());
                ghtkOrderResponse.setLabel(response.getOrder().getLabel());
                ghtkOrderResponse.setPartnerID(response.getOrder().getPartnerID());
                ghtkOrderResponse.setStatusId((response.getOrder().getStatusId()));
                repoGHTK.save(ghtkOrderResponse);
                mapGHTK.setBill(order);
                mapGHTK.setGhtkOrderResponse(ghtkOrderResponse);
                mapGHTK.setShipped(shipped.get());
                repoMapGHTK.save(mapGHTK);
                template.convertAndSend("/topic/transportGHTK/" + shopId, order.getId());
                responses.put("data", response);
                return new ResponseEntity(responses, HttpStatus.OK);
            }
            responses.put("message", res);
        } catch (Exception e) {
            e.printStackTrace();
            responses.put("message", e.getMessage());
        }

        return new ResponseEntity(responses, HttpStatus.OK);
    }

    public ResponseEntity<DtoGhtkStatusResponseDTO> statusResponse(Long id) throws URISyntaxException, RestClientException {

        HttpHeaders headers = new HttpHeaders();

        Optional<MapGHTK> mapGHTK = repoMapGHTK.findByBillId(id);
        String url = GHTK_Status + mapGHTK.get().getGhtkOrderResponse().getLabel();

        headers.set("Token", mapGHTK.get().getShipped().getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<DtoGhtkStatusResponseDTO> response = restTemplate.exchange(new URI(url), HttpMethod.GET, entity, DtoGhtkStatusResponseDTO.class);
        Optional<Bill> bill = repoBill.findById(id);
        Bill bills = bill.get();
        bills.setStatus(response.getBody().getOrder().getStatus_text());
        String status = response.getBody().getOrder().getStatus();
        if (status.contains("-1")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(10L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("1")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(1L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("2") || status.contains("12")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(2L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("3")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(3L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("4") || status.contains("5")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(4L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("6") || status.contains("11")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(8L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("7") || status.contains("8") || status.contains("9") || status.contains("10") || status.contains("127")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(9L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("20")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(6L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("13")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(7L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("21") || status.contains("45")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(5L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("128")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(2L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("49") || status.contains("410")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(9L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        }
        return response;
    }

    public ResponseEntity webhook(GHTKWebhook webhook) {
        Map<String, Object> response = new HashMap<>();
        Optional<MapGHTK> mapGHTK = repoMapGHTK.findByGhtkOrderResponse_Label(webhook.getLabelId());
        if (!mapGHTK.isPresent()) {
            response.put("mesager", "label_id không tồn tại !!!!");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        Bill bills = mapGHTK.get().getBill();
        bills.setShipFee(webhook.getFee().doubleValue());
        bills.setCashMoney(webhook.getPickMoney());
        String status = webhook.getStatus_id().toString();
        bills.setStatus(status(webhook.getStatus_id()));
        if (status.contains("-1")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(10L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("1")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(1L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("2") || status.contains("12")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(2L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("3")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(3L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("4") || status.contains("5") || status.contains("123")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(4L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("6") || status.contains("11")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(8L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("7") || status.contains("8") || status.contains("9") || status.contains("10") || status.contains("127")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(9L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("20")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(6L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("13")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(7L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("21") || status.contains("45")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(5L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("128")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(2L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("49") || status.contains("410")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(9L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        }
        response.put("data", webhook);
        response.put("success", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    public ResponseEntity<GHTKOrderResponseDTO> cancel(Long id) throws URISyntaxException {

        HttpHeaders headers = new HttpHeaders();
        Optional<MapGHTK> mapGHTK = repoMapGHTK.findByBillId(id);
        if (mapGHTK.isPresent()) {
            try {
                String url = GHTK_Cancel + mapGHTK.get().getGhtkOrderResponse().getLabel();
                headers.set("Token", mapGHTK.get().getShipped().getToken());
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<String> entity = new HttpEntity<String>(headers);

                ResponseEntity<GHTKOrderResponseDTO> response = restTemplate.exchange(new URI(url), HttpMethod.POST, entity, GHTKOrderResponseDTO.class);
                ObjectMapper mapper = new ObjectMapper();
                if (response.getBody().isSuccess() == true) {
                    Optional<Bill> bill = repoBill.findById(id);
                    Bill bill1 = bill.get();
                    Optional<StatusShipped> statusShipped = repoStatus.findById(10L);
                    bill1.setStatusShipped(statusShipped.get());
                    bill1.setStatus(statusShipped.get().getStatusName());
                    repoBill.save(bill1);
                    template.convertAndSend("/topic/cancelGHTK/" + bill1.getShop().getId(), bill1.getId());
                    return response;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return null;
    }

    public ResponseEntity<GHTKFeeDTO> calculateFee(Long billId, String name, Long shopId) {
        ShipPackageDTO shipPackage = new ShipPackageDTO();
        HttpHeaders headers = new HttpHeaders();
        Optional<Bill> bill = repoBill.findById(billId);
        ShopInfo shopInfo = bill.get().getShop();
        Customer customer = bill.get().getCustomer();
        Optional<Shipped> shipped = repoShipped.findByNameAndShopInfoId(name, shopId);

        shipPackage.setPickProvince(shopInfo.getProvince());
        shipPackage.setPickDistrict(shopInfo.getDistrict());
        shipPackage.setProvince(customer.getProvince());
        shipPackage.setDistrict(customer.getDistrict());
        shipPackage.setAddress(customer.getAddress());
        shipPackage.setWeight(bill.get().getWeight());
        shipPackage.setValue(0);
        shipPackage.setTransport("");
        headers.set("Token", shipped.get().getToken());
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(GHTK_FEE_ENDPOINT)
                .queryParam("pick_province", shipPackage.getPickProvince()).queryParam("pick_district", shipPackage.getPickDistrict())
                .queryParam("province", shipPackage.getProvince()).queryParam("district", shipPackage.getDistrict())
                .queryParam("address", shipPackage.getAddress())
                .queryParam("weight", shipPackage.getWeight())
                .queryParam("value", shipPackage.getValue())
                .queryParam("transport", shipPackage.getTransport()).build();

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<GHTKFeeDTO> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
                GHTKFeeDTO.class);

        return response;
    }

    //Tính giá shipper cho khách
    public ResponseEntity<GHTKFeeDTO> calculateFeeCustomes(ShipPackageDTO ship, String name, Long shopId) {
        ShipPackageDTO shipPackage = new ShipPackageDTO();
        HttpHeaders headers = new HttpHeaders();
        Optional<Shipped> shipped = repoShipped.findByNameAndShopInfoId(name, shopId);
        Optional<ShopInfo> shopInfo = repoShopInfo.findById(shopId);
        shipPackage.setPickProvince(shopInfo.get().getProvince());
        shipPackage.setPickDistrict(shopInfo.get().getDistrict());
        shipPackage.setProvince(ship.getProvince());
        shipPackage.setDistrict(ship.getDistrict());
        if (ship.getAddress() != null) {
            shipPackage.setAddress(ship.getAddress());
        }
        if (ship.getWeight() != null) {
            shipPackage.setWeight(ship.getWeight());
        }
        shipPackage.setValue(0);
        shipPackage.setTransport("");
        headers.set("Token", shipped.get().getToken());
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(GHTK_FEE_ENDPOINT)
                .queryParam("pick_province", shipPackage.getPickProvince()).queryParam("pick_district", shipPackage.getPickDistrict())
                .queryParam("province", shipPackage.getProvince()).queryParam("district", shipPackage.getDistrict())
                .queryParam("address", shipPackage.getAddress())
//                .queryParam("weight", shipPackage.getWeight())
                .queryParam("value", shipPackage.getValue())
                .queryParam("transport", shipPackage.getTransport()).build();

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<GHTKFeeDTO> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
                GHTKFeeDTO.class);

        return response;
    }

    // kiểm tra xem đã có khách hàng này chưa qua sđt
    private Customer checkCustomer(String phone, Long shopId) {
        List<Customer> customerOpt = repoCustomer.findByPhoneAndShopId(phone, shopId);
        if (customerOpt.size() > 0) {
            return customerOpt.get(0);
        }
        return null;
    }

    public Double formatStringToDouble(String data) {
        Double price = 0.0;
        String originPrice = data;
        if (originPrice != null && originPrice.length() > 0) {
            price = Double.parseDouble(originPrice);
        }
        return price;
    }

    public String status(int id) {
        HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
        hashMap.put(-1, "Hủy đơn hàng");
        hashMap.put(1, "Chưa tiếp nhận");
        hashMap.put(2, "Đã tiếp nhận");
        hashMap.put(3, "Đã lấy hàng/Đã nhập kho");
        hashMap.put(4, "Đã điều phối giao hàng/Đang giao hàng");
        hashMap.put(5, "Đã giao hàng/Chưa đối soát");
        hashMap.put(6, "Đã đối soát");
        hashMap.put(7, "Không lấy được hàng");
        hashMap.put(8, "Hoãn lấy hàng");
        hashMap.put(9, "Không giao được hàng");
        hashMap.put(10, "Delay giao hàng");
        hashMap.put(11, "Đã đối soát công nợ trả hàng");
        hashMap.put(12, "Đã điều phối lấy hàng/Đang lấy hàng");
        hashMap.put(13, "Đơn hàng bồi hoàn");
        hashMap.put(20, "Đang trả hàng (COD cầm hàng đi trả)");
        hashMap.put(21, "Đã trả hàng (COD đã trả xong hàng)");
        hashMap.put(123, "Shipper báo đã lấy hàng");
        hashMap.put(127, "Shipper (nhân viên lấy/giao hàng) báo không lấy được hàng");
        hashMap.put(128, "Shipper báo delay lấy hàng");
        hashMap.put(45, "Shipper báo đã giao hàng");
        hashMap.put(49, "Shipper báo không giao được giao hàng");
        hashMap.put(410, "Shipper báo delay giao hàng");
        return hashMap.get(id);
    }

    public int formatStringToInt(String data) {
        int price = 0;
        String originPrice = data;
        if (originPrice != null && originPrice.length() > 0) {
            price = Integer.parseInt(originPrice);
        }
        return price;
    }

}
