package com.vvlanding.service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vvlanding.dto.DtoOrder;
import com.vvlanding.dto.DtoOrderDetail;
import com.vvlanding.dto.shipped.ghn.*;
import com.vvlanding.dto.shipped.ghn.GHNResponseDTO;
import com.vvlanding.repo.*;
import com.vvlanding.table.*;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.*;

@Service
public class SerGHN {

    private static final String GHN_ORDER = "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create";

    private static final String Return_Order = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/switch-status/return";

    private static final String DeliveryAgain = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/switch-status/storing";

    private static final String GHN_ORDER_UPDATE_TEST = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/update";

    private static final String GHN_ORDER_TEST = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create";

    private static final String GHN_CANCEL = "https://online-gateway.ghn.vn/shiip/public-api/v2/switch-status/cancel";

    private static final String GHN_FEE = "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";

    private static final String GHN_DETAIL = "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/detail";

    private static final String GHN_GET_LIST_SHOP = "https://online-gateway.ghn.vn/shiip/public-api/v2/shop/all";

    private static RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private RepoShipped repoShipped;

    @Autowired
    private RepoBill repoBill;

    @Autowired
    private RepoCustomer repoCustomer;

    @Autowired
    RepoGHN repoGHN;

    @Autowired
    private RepoMapGHN repoMapGHN;

    @Autowired
    private DistrictRepository repoDistrict;

    @Autowired
    private WardRepository repoWard;

    @Autowired
    private RepoStatus repoStatus;

    @Autowired
    private RepoProduct repoProduct;

    @Autowired
    private RepoShopGhn repoShopGhn;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private RepoShopInfo repoShopInfo;

    @Autowired
    SimpMessagingTemplate template;

    // tạo đơn
    public ResponseEntity createOrder(Long billId, String name, Long shopId, Long shopGhnId) throws RestClientException, URISyntaxException {
        Map<String, Object> responses = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        Optional<Bill> bill = repoBill.findById(billId);
        if (bill.get().getIsActive() == false) {
            responses.put("message", "Active false");
            responses.put("success", "false");
            return new ResponseEntity(responses, HttpStatus.BAD_REQUEST);
        }
        Optional<Shipped> shipped = repoShipped.findByNameAndShopInfoId(name, shopId);
        ShopInfo shopInfo = shipped.get().getShopInfo();
        headers.set("Token", shipped.get().getToken());
        headers.set("shopGhnId", String.valueOf(shopGhnId));
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper mapper = new ObjectMapper();
        Bill bills = bill.get();
        DtoOrder dtoOrder = new DtoOrder();
        dtoOrder.setId(bills.getId());
        dtoOrder.setShopId(shopId);
        dtoOrder.setShipType(bills.getShipType());
        dtoOrder.setInnerNote(bills.getInnerNote());
        dtoOrder.setPrintNote(bills.getPrintNote());
        dtoOrder.setCustomerName(bills.getCustomer().getTitle());
        dtoOrder.setCustomerPhone(bills.getCustomer().getPhone());
        dtoOrder.setCustomerProvince(bills.getCustomer().getProvince());
        dtoOrder.setCustomerDistrict(bills.getCustomer().getDistrict());
        dtoOrder.setCustomerWard(bills.getCustomer().getWard());
        dtoOrder.setCustomerFulladdress(bills.getCustomer().getAddress());
        dtoOrder.setCashMoney(bills.getCashMoney());
        dtoOrder.setWeight(bills.getWeight());
        List<DtoOrderDetail> BillDetail = new ArrayList();
        for (BillDetail b : bills.getBillDetails()) {
            DtoOrderDetail dtoBillDetail = new DtoOrderDetail();
            dtoBillDetail.setProductId(b.getProduct().getId());
            dtoBillDetail.setVariantId(b.getVariant().getId());
            dtoBillDetail.setProductTitle(b.getProduct().getTitle());
            dtoBillDetail.setProperties(b.getProperties());
            dtoBillDetail.setChannel(b.getChannel());
            dtoBillDetail.setQuantity(b.getQuantity());
            BillDetail.add(dtoBillDetail);
        }
        dtoOrder.setOrderDetails(BillDetail);

        GHNOrder ghnOrder = convertToBill(dtoOrder, shopInfo);
        try {
            mapper = mapper.setSerializationInclusion(Include.NON_NULL);
            mapper = mapper.setSerializationInclusion(Include.NON_DEFAULT);

            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ghnOrder));

            HttpEntity<String> entity = new HttpEntity<String>(mapper.writeValueAsString(ghnOrder), headers);
            GHNResponseDTO response = restTemplate.postForObject(new URI(GHN_ORDER), entity,
                    GHNResponseDTO.class);

            if (response != null) {
                System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
            }
            if (response.getCode() == 200) {
                try {
                    bills.setShipFee(formatStringToDouble(response.getData().getTotalFee()));
                    bills.setOrderCode(response.getData().getOrderCode());
                    bills.setShipPartner("GHN");
                    bills.setShopShipMoney(bills.getTotalMoney() + bills.getShipFee());
                    Optional<StatusShipped> statusShipped = repoStatus.findById(3L);
                    bills.setStatus(statusShipped.get().getStatusName());
                    bills.setStatusShipped(statusShipped.get());
                    repoBill.save(bills);
                    GHNOrderResponse ghnOrderResponse = new GHNOrderResponse();
                    ghnOrderResponse.setOrderCode(response.getData().getOrderCode());
                    ghnOrderResponse.setExpectedDeliveryTime(response.getData().getExpectedDeliveryTime());
                    ghnOrderResponse.setSortCode(response.getData().getSortCode());
                    ghnOrderResponse.setTotalFee(response.getData().getTotalFee());
                    ghnOrderResponse.setTransType(response.getData().getTransType());
                    ghnOrderResponse.setWardEncode(response.getData().getWardEncode());
                    repoGHN.save(ghnOrderResponse);
                    MapGHN mapGHN = new MapGHN();
                    mapGHN.setBill(bills);
                    mapGHN.setGhnOrderResponse(ghnOrderResponse);
                    mapGHN.setShipped(shipped.get());
                    repoMapGHN.save(mapGHN);
                    template.convertAndSend("/topic/transportGHN/"+shopId, bills.getId());

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }


    //hủy đơn hàng
    public ResponseEntity<GHNCancelResponseDTO> cancel(Long id, Long shopId, String shopGhnId) throws JsonProcessingException, URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        ObjectMapper mapper = new ObjectMapper();
        GHNOrderCodeList orderCode = new GHNOrderCodeList();
        List<String> arr = new ArrayList<>();
        Optional<MapGHN> mapGHN = repoMapGHN.findByBillId(id);
        if (mapGHN.isPresent()){
            headers.set("token", mapGHN.get().getShipped().getToken());
            headers.set("shopGhnId", shopGhnId);
            headers.setContentType(MediaType.APPLICATION_JSON);

            mapper = mapper.setSerializationInclusion(Include.NON_NULL);
            mapper = mapper.setSerializationInclusion(Include.NON_DEFAULT);

            arr.add(mapGHN.get().getGhnOrderResponse().getOrderCode());
            orderCode.setOrderCodes(arr);
            System.out.println(mapper.writeValueAsString(orderCode));
            HttpEntity<String> entity = new HttpEntity<String>(mapper.writeValueAsString(orderCode), headers);
            ResponseEntity<GHNCancelResponseDTO> response = restTemplate.exchange(new URI(GHN_CANCEL), HttpMethod.POST, entity, GHNCancelResponseDTO.class);
            if (response.getBody().getCode() == 200 || response.getBody().getMessage().contains("OK")){
                Optional<Bill> bill = repoBill.findByShopIdAndId(shopId,id);
                Bill bill1 = bill.get();
                Optional<StatusShipped> statusShipped = repoStatus.findById(10L);
                bill1.setStatusShipped(statusShipped.get());
                bill1.setStatus(statusShipped.get().getStatusName());
                repoBill.save(bill1);
                template.convertAndSend("/topic/cancelGHN/"+shopId, bill1.getId());
                return response;
            }
        }
        return null;
    }
    // giao lại đơn hàng
    public GHNCancelResponseDTO deliveryAgain(Long billId, Long shopGhnId){
        HttpHeaders headers = new HttpHeaders();
        ObjectMapper mapper = new ObjectMapper();
        GHNOrderCodeList orderCode = new GHNOrderCodeList();
        Optional<MapGHN> mapGHN = repoMapGHN.findByBillId(billId);
        List<String> arr = new ArrayList<>();
        headers.set("token", mapGHN.get().getShipped().getToken());
        headers.set("shopGhnId", String.valueOf(shopGhnId));
        headers.setContentType(MediaType.APPLICATION_JSON);
        mapper = mapper.setSerializationInclusion(Include.NON_NULL);
        mapper = mapper.setSerializationInclusion(Include.NON_DEFAULT);
        arr.add(mapGHN.get().getGhnOrderResponse().getOrderCode());
        orderCode.setOrderCodes(arr);
        try {
            HttpEntity<String> entity = new HttpEntity<String>(mapper.writeValueAsString(orderCode), headers);
            ResponseEntity<GHNCancelResponseDTO> response = restTemplate.exchange(new URI(DeliveryAgain), HttpMethod.POST, entity, GHNCancelResponseDTO.class);
            return response.getBody();
        }catch (Exception e){
            return null;
        }
    }



    //Tính tiền vận chuyển
    public ResponseEntity<GHNFeeResponseDTO> fee(FeeGHN fee, Long billId, String name, Long shopId, Long shopGhnId) throws JsonProcessingException, URISyntaxException {

        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();

        Optional<Shipped> shipped = repoShipped.findByNameAndShopInfoId(name, shopId);
        Optional<Bill> bill = repoBill.findById(billId);
        headers.set("token", shipped.get().getToken());
        headers.set("shopGhnId", String.valueOf(shopGhnId));
        headers.setContentType(MediaType.APPLICATION_JSON);
        mapper = mapper.setSerializationInclusion(Include.NON_NULL);
        mapper = mapper.setSerializationInclusion(Include.NON_DEFAULT);
        FeeGHN feeGHN = new FeeGHN();
        Customer customer = bill.get().getCustomer();
        ShopInfo shopInfo = bill.get().getShop();
        if (fee.getFromDistrictName() != null && fee.getFromFromProvinceName() != null) {
            AddressDistrict district = getAddress(fee.getFromFromProvinceName(), fee.getFromDistrictName());
            feeGHN.setFromDistrictId(formatStringToInt(district.getId()));
        } else {
            AddressDistrict fromDistrict = getAddress(shopInfo.getProvince(), shopInfo.getDistrict());
            feeGHN.setFromDistrictId(formatStringToInt(fromDistrict.getId()));
        }
        AddressDistrict toDistrict = getAddress(customer.getProvince(), customer.getDistrict());
        AddressWard toWard = getWard(toDistrict.getId(), customer.getWard());
        if (feeGHN.getToWardName() != null) {
            Optional<AddressWard> addressWard = repoWard.findByNameIgnoreCase(feeGHN.getToWardName());
            feeGHN.setToWardCode(addressWard.get().getCode());
        }
        feeGHN.setToDistrictId(formatStringToInt(toDistrict.getId()));
        feeGHN.setToWardCode(toWard.getCode());
        feeGHN.setWeight(bill.get().getWeight());
        feeGHN.setServiceTypeId(2);
        HttpEntity<String> entity = new HttpEntity<String>(mapper.writeValueAsString(feeGHN), headers);
//		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(feeGHN));

        ResponseEntity<GHNFeeResponseDTO> responseDTOResponseEntity = restTemplate.exchange(new URI(GHN_FEE), HttpMethod.POST, entity, GHNFeeResponseDTO.class);

        return responseDTOResponseEntity;
    }
    //tính tiền vận chuyển khách hàng
    public ResponseEntity<GHNFeeResponseDTO> feeCustomer(FeeGHN fee, String name, Long shopId, Long shopGhnId) throws JsonProcessingException, URISyntaxException {

        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();

        Optional<Shipped> shipped = repoShipped.findByNameAndShopInfoId(name, shopId);
        Optional<ShopInfo> shopInfo = repoShopInfo.findById(shopId);
        headers.set("token", shipped.get().getToken());
        headers.set("shopGhnId", String.valueOf(shopGhnId));
        headers.setContentType(MediaType.APPLICATION_JSON);
        mapper = mapper.setSerializationInclusion(Include.NON_NULL);
        mapper = mapper.setSerializationInclusion(Include.NON_DEFAULT);
        FeeGHN feeGHN = new FeeGHN();
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(fee));
        AddressDistrict fromDistrict = getAddress(shopInfo.get().getProvince(), shopInfo.get().getDistrict());
        feeGHN.setFromDistrictId(formatStringToInt(fromDistrict.getId()));

        AddressDistrict toDistrict = getAddress(fee.getToProvince(), fee.getToDistrictName());
        AddressWard toWard = getWard(toDistrict.getId(), fee.getToWardName());
        feeGHN.setToDistrictId(formatStringToInt(toDistrict.getId()));
        feeGHN.setToWardCode(toWard.getCode());
        feeGHN.setWeight(fee.getWeight());
        feeGHN.setServiceTypeId(2);
        HttpEntity<String> entity = new HttpEntity<String>(mapper.writeValueAsString(feeGHN), headers);
//		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(feeGHN));

        ResponseEntity<GHNFeeResponseDTO> responseDTOResponseEntity = restTemplate.exchange(new URI(GHN_FEE), HttpMethod.POST, entity, GHNFeeResponseDTO.class);

        return responseDTOResponseEntity;
    }

    // trạng thái
    public ResponseEntity<GHNOrderDetailDTO> orderDetail(Long id) throws URISyntaxException, JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        GHNOrderCode orderCode = new GHNOrderCode();
        ObjectMapper mapper = new ObjectMapper();

        mapper = mapper.setSerializationInclusion(Include.NON_NULL);
        mapper = mapper.setSerializationInclusion(Include.NON_DEFAULT);

        Optional<MapGHN> mapGHN = repoMapGHN.findByBillId(id);

        headers.set("token", mapGHN.get().getShipped().getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        orderCode.setOrder_code(mapGHN.get().getGhnOrderResponse().getOrderCode());

        HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(orderCode), headers);
        System.out.println(mapper.writeValueAsString(orderCode));
        ResponseEntity<GHNOrderDetailDTO> response = restTemplate.exchange(new URI(GHN_DETAIL), HttpMethod.POST, entity, GHNOrderDetailDTO.class);
        String status = response.getBody().getData().getStatus();
        Optional<Bill> bill = repoBill.findById(id);
        Bill bills = bill.get();
        if (status.contains("ready_to_pick")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(1L);
            bills.setStatusShipped(statusShipped.get());
            bills.setStatus("Mới tạo đơn hàng");
            repoBill.save(bills);
        } else if (status.contains("picking")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(2L);
            bills.setStatusShipped(statusShipped.get());
            bills.setStatus("Nhân viên đang lấy hàng");
            repoBill.save(bills);
        } else if (status.contains("cancel")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(10L);
            bills.setStatusShipped(statusShipped.get());
            bills.setStatus("Hủy đơn hàng");
            repoBill.save(bills);
        } else if (status.contains("money_collect_picking") || status.contains("picked") || status.contains("delivering") || status.contains("money_collect_delivering")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(4L);
            bills.setStatusShipped(statusShipped.get());
            bills.setStatus("Đang Giao");
            repoBill.save(bills);
        } else if (status.contains("storing") || status.contains("transporting") || status.contains("sorting")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(3L);
            bills.setStatusShipped(statusShipped.get());
            bills.setStatus("Gửi vận chuyển");
            repoBill.save(bills);
        } else if (status.contains("delivered")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(5L);
            bills.setStatusShipped(statusShipped.get());
            bills.setStatus("Nhân viên đã giao hàng thành công");
            repoBill.save(bills);
        } else if (status.contains("delivery_fail")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(9L);
            bills.setStatusShipped(statusShipped.get());
            bills.setStatus("Nhân viên giao hàng thất bại");
            repoBill.save(bills);
        } else if (status.contains("exception")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(9L);
            bills.setStatusShipped(statusShipped.get());
            bills.setStatus("Đơn hàng ngoại lệ không nằm trong quy trình");
            repoBill.save(bills);
        } else if (status.contains("damage")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(9L);
            bills.setStatusShipped(statusShipped.get());
            bills.setStatus("Hàng bị hư hỏng");
            repoBill.save(bills);
        } else if (status.contains("lost")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(9L);
            bills.setStatusShipped(statusShipped.get());
            bills.setStatus("Hàng bị mất");
            repoBill.save(bills);
        } else if (status.contains("return_fail")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(6L);
            bills.setStatusShipped(statusShipped.get());
            bills.setStatus("Nhân viên trả hàng thất bại");
            repoBill.save(bills);
        } else if (status.contains("waiting_to_return") || status.contains("return_transporting") || status.contains("return_sorting") || status.contains("returning")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(6L);
            bills.setStatusShipped(statusShipped.get());
            bills.setStatus("Nhân viên đang đi trả hàng");
            repoBill.save(bills);
        } else if (status.contains("return") || status.contains("returned")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(7L);
            bills.setStatusShipped(statusShipped.get());
            bills.setStatus("Mới tạo đơn hàng");
            repoBill.save(bills);
        }
        return response;
    }

    public ResponseEntity webhook(GHNWebhook webhook) {
        Map<String, Object> response = new HashMap<>();
        Optional<MapGHN> mapGHN = repoMapGHN.findByGhnOrderResponse_OrderCode(webhook.getOrderCode());
        if (!mapGHN.isPresent()) {
            response.put("mesager", "order code không tồn tại !!!!");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        Bill bills = mapGHN.get().getBill();
        bills.setStatus(webhook.getDescription());
        bills.setShipFee(webhook.getTotalFee());
        String status = webhook.getStatus();
        if (status.contains("ready_to_pick")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(1L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("picking")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(2L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("cancel")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(10L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("money_collect_picking") || status.contains("picked") || status.contains("delivering") || status.contains("money_collect_delivering")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(4L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("storing") || status.contains("transporting") || status.contains("sorting")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(3L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("delivered")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(5L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("delivery_fail") || status.contains("exception") || status.contains("damage") || status.contains("lost")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(9L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("return_fail") || status.contains("return")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(6L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("waiting_to_return") || status.contains("return_transporting") || status.contains("return_sorting") || status.contains("returning")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(6L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        } else if (status.contains("returned")) {
            Optional<StatusShipped> statusShipped = repoStatus.findById(7L);
            bills.setStatusShipped(statusShipped.get());
            repoBill.save(bills);
        }
        response.put("data", webhook);
        response.put("success", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private GHNOrder convertToBill(DtoOrder dtoOrder, ShopInfo shopInfo) {

        GHNOrder ghnOrder = new GHNOrder();
        AddressDistrict toDistrict = getAddress(dtoOrder.getCustomerProvince(), dtoOrder.getCustomerDistrict());

        AddressDistrict returnDistrict = getAddress(shopInfo.getProvince(), shopInfo.getDistrict());

        AddressWard toWard = getWard(toDistrict.getId(), dtoOrder.getCustomerWard());

        ghnOrder.setToDistrictId(formatStringToInt(toDistrict.getId()));

        ghnOrder.setToWardCode(toWard.getCode());
        ghnOrder.setPaymentTypeId(formatStringToInt(dtoOrder.getShipType()));
        ghnOrder.setRequiredNote(dtoOrder.getPrintNote());
        ghnOrder.setNote(dtoOrder.getInnerNote());
        ghnOrder.setReturnPhone(shopInfo.getPhone());
        ghnOrder.setReturnAddress(returnDistrict.getName());
        ghnOrder.setReturnDistrictId(formatStringToInt(returnDistrict.getId()));
        ghnOrder.setReturnWardCode("");
        ghnOrder.setClientOrderCode("");
        ghnOrder.setToPhone(dtoOrder.getCustomerPhone());
        ghnOrder.setToName(dtoOrder.getCustomerName());
        ghnOrder.setToAddress(dtoOrder.getCustomerFulladdress());
        ghnOrder.setCodAmount(dtoOrder.getCashMoney().intValue());
        ghnOrder.setContent(dtoOrder.getPrintNote());
        ghnOrder.setWeight(dtoOrder.getWeight());
        ghnOrder.setInsuranceValue(0);
        ghnOrder.setServiceTypeId(2);
        List<GHNProduct> ghnProducts = new ArrayList<>();
        for (int i = 0; i < dtoOrder.getOrderDetails().size(); i++) {
            GHNProduct ghnProduct = new GHNProduct();
            Optional<Product> product = repoProduct.findById(dtoOrder.getOrderDetails().get(i).getProductId());
            ghnProduct.setName(product.get().getTitle());
            ghnProduct.setCode(product.get().getSku());
            ghnProduct.setQuantity(dtoOrder.getOrderDetails().get(i).getQuantity().intValue());
            if (dtoOrder.getOrderDetails().get(i).getPrice() != null) {
                ghnProduct.setPrice(dtoOrder.getOrderDetails().get(i).getPrice().intValue());
            }
            ghnProduct.setVariantId(dtoOrder.getOrderDetails().get(i).getVariantId());
            ghnProduct.setProperties(dtoOrder.getOrderDetails().get(i).getProperties());
            ghnProduct.setChannel(dtoOrder.getOrderDetails().get(i).getChannel());
            ghnProducts.add(ghnProduct);
        }
        ghnOrder.setItems(ghnProducts);
        return ghnOrder;
    }
    public ResponseEntity getShop(Shipped shipped,String token,Long type) throws JsonProcessingException, URISyntaxException {
        if (type == 1){
            deleteShopGhn(shipped);
        }

        GetShopRequest request = new GetShopRequest(0,100,"");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(request));
        HttpEntity<String> entity = new HttpEntity<String>(mapper.writeValueAsString(request), headers);

        GetShopDTO response = restTemplate.postForObject(new URI(GHN_GET_LIST_SHOP), entity,
                GetShopDTO.class);
        List<ShopGHN> ghnList = new ArrayList<>();
        List<GHNGetShop> getShops = response.getData().getShops();
        for (GHNGetShop d : getShops) {
            Optional<ShopGHN> shopGHN1 = repoShopGhn.findByShopGhnId(d.get_id());
            if (!shopGHN1.isPresent()){
                ShopGHN shopGHN = new ShopGHN();
                shopGHN.setShopGhnId(d.get_id());
                shopGHN.setNameShop(d.getName());
                shopGHN.setPhone(d.getPhone());
                shopGHN.setStatus(d.getStatus());
                shopGHN.setAddress(d.getAddress());
                shopGHN.setDistrictId(d.getDistrict_id());
                shopGHN.setWardCode(d.getWard_code());
                shopGHN.setCreatedDate(d.getCreated_date());
                shopGHN.setShipped(shipped);
                shopGHN.setShopInfo(shipped.getShopInfo());
                ghnList.add(shopGHN);
            }
        }
        repoShopGhn.saveAll(ghnList);
        return new ResponseEntity(response,HttpStatus.OK);
    }
    private void deleteShopGhn(Shipped shipped){
        List<ShopGHN> shopGHN = repoShopGhn.findAllByShopInfoId(shipped.getShopInfo().getId());
        repoShopGhn.deleteAll(shopGHN);
    }

    public ResponseEntity getShopGhn(Long shopId){
        Map<String,Object> response = new HashMap<>();
        List<ShopGHN> shopGHNS = repoShopGhn.findAllByShopInfoId(shopId);
        List<GHNGetShopDTO> ghnGetShopDTO = new ArrayList<>();
        for (ShopGHN s:shopGHNS) {
            GHNGetShopDTO ghnGetShop = new GHNGetShopDTO(s.getShopGhnId(),s.getNameShop(),s.getPhone(),s.getAddress());
            ghnGetShopDTO.add(ghnGetShop);
        }
        response.put("data",ghnGetShopDTO);
        return new ResponseEntity(response,HttpStatus.OK);
    }

    // kiểm tra xem đã có khách hàng này chưa qua sđt
    private Customer checkCustomer(String phone,Long shopId) {
        List<Customer> customerOpt = repoCustomer.findByPhoneAndShopId(phone, shopId);
        if (customerOpt.size() > 0) {
            return customerOpt.get(0);
        }
        return null;
    }

    public int formatStringToInt(String data) {
        int price = 0;
        String originPrice = data;
        if (originPrice != null && originPrice.length() > 0) {
            price = Integer.parseInt(originPrice);
        }
        return price;
    }

    public Double formatStringToDouble(String data) {
        Double price = 0.0;
        String originPrice = data;
        if (originPrice != null && originPrice.length() > 0) {
            price = Double.parseDouble(originPrice);
        }
        return price;
    }

    public AddressDistrict getAddress(String province, String district) {

        Optional<AddressProvince> addressProvince = provinceRepository.findByNameIgnoreCase(province);
        List<AddressDistrict> addressDistricts = addressProvince.get().getDistricts();
        List<AddressDistrict> find = repoDistrict.findByName(district);
        for (int i = 0; i < find.size(); i++) {
            if (addressDistricts.contains(find.get(i))) {
                return find.get(i);
            }
        }
        return null;
    }

    public AddressWard getWard(String districtId, String ward) {
        Optional<AddressDistrict> addressDistrict = repoDistrict.findById(districtId);
        List<AddressWard> addressWards = addressDistrict.get().getWards();
        List<AddressWard> find = repoWard.findByName(ward);
        for (int i = 0; i < find.size(); i++) {
            if (addressWards.contains(find.get(i))) {
                return find.get(i);
            }
        }
        return null;
    }

}
