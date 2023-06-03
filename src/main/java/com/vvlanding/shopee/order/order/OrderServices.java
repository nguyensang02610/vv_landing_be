package com.vvlanding.shopee.order.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vvlanding.shopee.Common;
import com.vvlanding.shopee.Iconstants;
import com.vvlanding.shopee.chat.ResponseDTO;
import com.vvlanding.shopee.order.Shipping.RequestDropoff;
import com.vvlanding.shopee.order.Shipping.RequestPickup;
import com.vvlanding.shopee.order.Shipping.RequestShipOrder;
import com.vvlanding.shopee.order.Shipping.ResponseShippingParameter;
import com.vvlanding.shopee.service.shopee.ShopeeService;
import com.vvlanding.shopee.webhook.shopee.ChannelSources;
import com.vvlanding.shopee.webhook.shopee.MapOrderStatus;
import com.vvlanding.repo.*;
import com.vvlanding.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
public class OrderServices {

    private final long partnerID = Iconstants.partner_id;

    private final String partnerKey = Iconstants.partner_key;

    private final long partnerID_Order = Iconstants.partner_id_order_v2;

    private final String partnerKey_Order = Iconstants.partner_key_order_v2;

    @Autowired
    RepoShopInfo repoShopInfo;

    @Autowired
    RepoCustomer repoCustomer;

    @Autowired
    RepoStatus repoStatus;

    @Autowired
    RepoBill repoBill;

    @Autowired
    RepoBillDetail repoBillDetail;

    @Autowired
    RepoProductVariations repoProductVariations;

    @Autowired
    ShopeeService shopeeService;

    @Autowired
    RepoChannelShopee repoChannelShopee;

    @Autowired
    RepoOrderChannel repoOrderChannel;

    @Autowired
    RepoProductShopee repoProductShopee;

    @Autowired
    SimpMessagingTemplate template;


    public List<Orders> fetchOrders(ShopInfo shopInfo, Long shopeeId) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        Date currentDate = new Date();
        Instant currentInstant = currentDate.toInstant();
        Instant thirtyDayBefore = currentInstant.minus(Duration.ofDays(60));

        List<Orders> orders = new ArrayList<>();
        try {
            long apiTimeStamp = Common.getCurrentTime();

            Instant endInstant = currentDate.toInstant();

            Instant pageFromInstant = thirtyDayBefore;

            while (pageFromInstant.isBefore(endInstant)) {

                Instant now = Instant.now();

                Instant pageToInstant = pageFromInstant.plus(Duration.ofDays(10));

                if (pageToInstant.isAfter(now)) {
                    pageToInstant = now;
                }

                Date pageFromDate = Date.from(pageFromInstant);

                Date pageToDate = Date.from(pageToInstant);

                long startTime = Common.date2Timestamp(pageFromDate);

                long endTime = Common.date2Timestamp(pageToDate);

                System.out.println(dateFormat.format(pageFromDate) + "=>" + dateFormat.format(pageToDate));

                OrderGetListRequestDTO orderGetListRequestDTO = new OrderGetListRequestDTO(partnerID, shopeeId,
                        apiTimeStamp, "ALL", startTime, endTime);

                OrderGetListResponseDTO resDTO = null;
                int cnt = 0;
                while (resDTO == null && cnt < 10) {
                    cnt++;
                    try {
                        resDTO = Common.callAPI(partnerKey, orderGetListRequestDTO, Iconstants.GET_LIST_ORDER,
                                OrderGetListResponseDTO.class);
                    } catch (Exception e) {
                        resDTO = null;
                    }
                }

                if (resDTO != null && resDTO.getOrders() != null && resDTO.getOrders().size() > 0) {
                    List<Orders> resOrders = resDTO.getOrders();

                    for (Orders od : resOrders) {
                        String ordersn = od.getOrdersn();
                        Optional<Bill> findByShopeeOrdersn = repoBill.findByOrderCode(ordersn);
                        if (!findByShopeeOrdersn.isPresent()) {
                            orders.add(od);
                        }
                    }
                }
                pageFromInstant = pageToInstant;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    public List<OrderDetailsResponseDTO> fetchListOrderDetail(ShopInfo shop, long shopeeShopId, List<Orders> orders) throws JsonProcessingException {

        int listSize = orders.size();

        System.out.println("list order items: " + listSize);
        int cnt = 0;
        List<OrderDetailsResponseDTO> responseDTO = new ArrayList<>();
        while (cnt < listSize) {
            List<Orders> subList;
            List<String> ordersnList = new ArrayList<String>();
            if (cnt + 50 < listSize) {
                subList = orders.subList(cnt, cnt + 50);
                cnt = cnt + 50;
            } else {
                subList = orders.subList(cnt, listSize);
                cnt = listSize;
            }

            for (Orders od : subList) {
                ordersnList.add(od.getOrdersn());
            }
            OrderDetailsResponseDTO response = getDetail(shop, shopeeShopId, ordersnList);
            responseDTO.add(response);
        }
        return responseDTO;
    }

    public OrderDetailsResponseDTO getDetail(ShopInfo shopInfo, Long shopeeShopId, List<String> ordersnList) {

        long timestamp = Common.getCurrentTime();
        OrderDetailsRequestDTO orderDetailsRequestDTO = new OrderDetailsRequestDTO(ordersnList, partnerID, shopeeShopId, timestamp);
        OrderDetailsResponseDTO resDTO = null;
        int retries = 0;
        while (resDTO == null && retries < 10) {
            retries++;
            try {
                resDTO = Common.callAPI(partnerKey, orderDetailsRequestDTO, Iconstants.GET_DETAIL_ORDER,
                        OrderDetailsResponseDTO.class);
            } catch (Exception e) {
                resDTO = null;
            }
        }
        if (resDTO != null && resDTO.getOrders() != null && resDTO.getOrders().size() > 0) {
            for (int i = 0; i < resDTO.getOrders().size(); i++) {
                Optional<Bill> bill = repoBill.findByOrderCode(resDTO.getOrders().get(i).ordersn);
                if (bill.isPresent()) {
                    continue;
                }
                Bill order = new Bill();
                OrderDetail orderDetail = resDTO.getOrders().get(i);
                Customer cus = checkCustomer(orderDetail.recipient_address.getPhone(), shopInfo.getId());
                if (cus == null) {
                    Customer newCustomer = new Customer();
                    newCustomer.setTitle(orderDetail.recipient_address.getName());
                    newCustomer.setPhone(orderDetail.recipient_address.getPhone());
                    newCustomer.setDistrict(orderDetail.recipient_address.getDistrict());
                    newCustomer.setProvince(orderDetail.recipient_address.getCity());
                    newCustomer.setWard(orderDetail.recipient_address.getTown());
                    newCustomer.setAddress(orderDetail.recipient_address.getFull_address());
                    newCustomer.setActive(true);
                    newCustomer.setShopId(shopInfo.getId());
                    cus = repoCustomer.save(newCustomer);
                }

                int weight = 0;
                for (int j = 0; j < orderDetail.getItems().size(); j++) {
                    weight = weight + ((int) orderDetail.getItems().get(j).weight) * 1000;
                }
                order.setShop(shopInfo);
                order.setCustomer(cus);
                String codeBill = orderDetail.ordersn + new Date().getTime();
                order.setCodeBill(codeBill);
                order.setTotalMoney(orderDetail.total_amount - orderDetail.estimated_shipping_fee); // tiền hàng
                order.setShopShipMoney(orderDetail.total_amount);// tổng tiền khách phải trả
//                order.setPaidMoney(orderDetail.total_amount); // tiền khách đã thanh toán
                order.setPaidMoney(0.0);
                order.setDiscountPercent(0.0);
                order.setDiscountMoney(0.0);
                order.setCashMoney(orderDetail.total_amount - orderDetail.estimated_shipping_fee); // số tiền bên ship thu hộ
                order.setOrderCode(orderDetail.ordersn);
                order.setInnerNote(orderDetail.message_to_seller);
                order.setPrintNote(orderDetail.note);
                order.setWeight(orderDetail.weight);
                order.setShipFee(orderDetail.estimated_shipping_fee);// tiền ship ước tính
                order.setShipPartner(orderDetail.shipping_carrier);
                order.setShipType(orderDetail.getPayment_method());
                Optional<StatusShipped> statusShipped = repoStatus.findByStatusName(MapOrderStatus.map(orderDetail.order_status, ChannelSources.SHOPEE));
                order.setStatus(MapOrderStatus.map(orderDetail.order_status, ChannelSources.SHOPEE));
                order.setStatusShipped(statusShipped.get());
                order.setChannel("Shopee");
                order.setIsActive(false);
                Bill copoder = repoBill.save(order);

                Optional<ChannelShopee> channelShopee = repoChannelShopee.findByShopeeShopId(shopeeShopId);


                List<BillDetail> billDetails = new ArrayList<>();
                for (ItemOrder b : orderDetail.items) {

                    OrderShopee orderShopee = new OrderShopee();
                    orderShopee.setOrdersn(resDTO.getOrders().get(i).getOrdersn());
                    orderShopee.setOrderStatus(resDTO.getOrders().get(i).getOrder_status());
                    orderShopee.setChannelShopee(channelShopee.get());
                    orderShopee.setBill(copoder);
                    orderShopee.setName(channelShopee.get().getShopeeShopName());
                    repoOrderChannel.save(orderShopee);

                    BillDetail billDetail = new BillDetail();
                    double money = b.variation_original_price * b.variation_quantity_purchased;
                    billDetail.setMoney(money);
                    billDetail.setPrice(b.variation_original_price);
                    billDetail.setChannel("Shopee");
                    billDetail.setProperties(b.variation_name);
                    Optional<ProductVariations> opProductVariations = repoProductVariations.findBySku(b.variation_sku);
                    if (opProductVariations.isPresent()) {
                        ProductVariations variations = opProductVariations.get();
                        Product product = opProductVariations.get().getProducts();
                        billDetail.setProduct(product);
                        billDetail.setVariant(variations);
                        billDetail.setBills(copoder);
                        billDetail.setQuantity(Double.valueOf(b.variation_quantity_purchased));
                        billDetails.add(billDetail);
                    }
                }
                repoBillDetail.saveAll(billDetails);
            }
        }
        return resDTO;
    }

    public List<Orders> fetchOrdersV2(ShopInfo shopInfo, Long shopeeShopId) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        Date currentDate = new Date();
        Instant currentInstant = currentDate.toInstant();
        Instant thirtyDayBefore = currentInstant.minus(Duration.ofDays(60));
        List<Orders> orders = new ArrayList<>();

        try {
            Instant endInstant = currentDate.toInstant();

            Instant pageFromInstant = thirtyDayBefore;

            while (pageFromInstant.isBefore(endInstant)) {

                Instant now = Instant.now();

                Instant pageToInstant = pageFromInstant.plus(Duration.ofDays(10));

                if (pageToInstant.isAfter(now)) {
                    pageToInstant = now;
                }

                Date pageFromDate = Date.from(pageFromInstant);

                Date pageToDate = Date.from(pageToInstant);

                long startTime = Common.date2Timestamp(pageFromDate);

                long endTime = Common.date2Timestamp(pageToDate);
                String cursor = "";
                String url = "?time_range_field=create_time" + "&time_from=" + startTime + "&time_to=" + endTime + "&page_size=100" + "&cursor=" + cursor;
                ResponseDTO responseDTO = getListOrderV2(url, shopeeShopId);
                if (responseDTO.isSuccess() == false) {
                    return null;
                }
                if (responseDTO != null && responseDTO.getResponse().getOrder_list() != null && responseDTO.getResponse().getOrder_list().size() > 0) {
                    cursor = responseDTO.getResponse().getNext_cursor();
                    for (Orders od : responseDTO.getResponse().getOrder_list()) {
                        String ordersn = od.getOrder_sn();
                        Optional<Bill> findByShopeeOrdersn = repoBill.findByOrderCode(ordersn);
                        if (!findByShopeeOrdersn.isPresent()) {
                            orders.add(od);
                        }
                    }
                }
                pageFromInstant = pageToInstant;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    public ResponseDTO getListOrderV2(String url, Long shopeeId) {
//        String host = "https://partner.test-stable.shopeemobile.com";
//        String path = "/api/v2/order/get_order_list";
        String host = "https://partner.shopeemobile.com";
        String path = "/api/v2/order/get_order_list";
        String access_token = shopeeService.getToken(shopeeId, partnerKey_Order, partnerID_Order, 2L);
        if (access_token.equals("invalid")) {
            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setSuccess(false);
            return responseDTO;
        }
        int cnt = 0;
        ResponseDTO response = null;
        while (response == null && cnt < 2) {
            cnt++;
            try {
                response = Common.callAPIV2(url, host, path, access_token, String.valueOf(shopeeId), partnerKey_Order, String.valueOf(partnerID_Order), null, ResponseDTO.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        response.setSuccess(true);
        return response;
    }

    public void fetchListOrderDetailV2(ShopInfo shop, long shopeeShopId, List<Orders> orders) {
        int listSize = orders.size();
        int cnt = 0;
        while (cnt < listSize) {
            List<Orders> subList;
            List<String> ordersnList = new ArrayList<String>();
            if (cnt + 10 < listSize) {
                subList = orders.subList(cnt, cnt + 10);
                cnt = cnt + 10;
            } else {
                subList = orders.subList(cnt, listSize);
                cnt = listSize;
            }
            for (Orders od : subList) {
                ordersnList.add(od.getOrder_sn());
            }
            OrderDetailsResponseDTO response = getDetailV2(shop, shopeeShopId, ordersnList);
            if (response.isSuccess() == false) break;
        }
    }

    public OrderDetailsResponseDTO getDetailV2(ShopInfo shopInfo, Long shopeeShopId, List<String> ordersnList) {
//        String host = "https://partner.test-stable.shopeemobile.com";
//        String path = "/api/v2/order/get_order_detail";
        String host = "https://partner.shopeemobile.com";
        String path = "/api/v2/order/get_order_detail";
        String url = "";
        String access_token = shopeeService.getToken(shopeeShopId, partnerKey_Order, partnerID_Order, 2L);
        if (access_token.equals("invalid")) {
            OrderDetailsResponseDTO order = new OrderDetailsResponseDTO();
            order.setSuccess(false);
            return order;
        }
        ObjectMapper mapper = new ObjectMapper();
        for (String o : ordersnList) {
            url = url + o + ",";
        }
        String st = "&response_optional_fields=buyer_user_id,buyer_username,estimated_shipping_fee,recipient_address,actual_shipping_fee,note,item_list,credit_card_number,buyer_cancel_reason,cancel_by,cancel_reason,actual_shipping_fee_confirmed,total_amount,buyer_username,package_list,shipping_carrier,payment_method";
        String str = "?order_sn_list=" + url.substring(0, url.length() - 1) + st;
        OrderDetailsResponseDTO resDTO = null;
        int retries = 0;
        while (resDTO == null && retries < 5) {
            retries++;
            try {
                resDTO = Common.callAPIV2(str, host, path, access_token, String.valueOf(shopeeShopId), partnerKey_Order, String.valueOf(partnerID_Order), null, OrderDetailsResponseDTO.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (resDTO != null && resDTO.getResponse().getOrder_list() != null && resDTO.getResponse().getOrder_list().size() > 0) {
            for (int i = 0; i < resDTO.getResponse().getOrder_list().size(); i++) {
                Optional<Bill> bill = repoBill.findByCodeBill(resDTO.getResponse().getOrder_list().get(i).getOrder_sn());
                Bill order = new Bill();
                bill.ifPresent(value -> order.setId(value.getId()));
                if (bill.isPresent()){
                    if (!bill.get().getBillDetails().isEmpty() && bill.get().getBillDetails().size() > 0){
                        repoBillDetail.deleteAll(bill.get().getBillDetails());
                    }
                }
                ResponseShipping shipping = getShipping(resDTO.getResponse().getOrder_list().get(i).getOrder_sn(), shopeeShopId);
                OrderDetailV2 orderDetail = resDTO.getResponse().getOrder_list().get(i);
                String phone = orderDetail.getRecipient_address().getPhone();
                String phoneCus = "";
                String arr = "84";
                if (arr.equals(phone.substring(0, 2))) {
                    String array = "0" + phone.substring(2, phone.length());
                    phoneCus = array;
                } else {
                    phoneCus = phone;
                }
                Customer cus = checkCustomer(phoneCus, shopInfo.getId());
                if (cus == null) {
                    Customer newCustomer = new Customer();
                    newCustomer.setTitle(orderDetail.getRecipient_address().getName());
                    newCustomer.setPhone(phoneCus);
                    try {
                        newCustomer.setAddress(orderDetail.getRecipient_address().getFull_address());
                        newCustomer.setDistrict(orderDetail.getRecipient_address().getCity());
                        newCustomer.setWard(orderDetail.getRecipient_address().getDistrict());
                        newCustomer.setProvince(orderDetail.getRecipient_address().getState());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    newCustomer.setActive(true);
                    newCustomer.setShopId(shopInfo.getId());
                    cus = repoCustomer.save(newCustomer);
                }
                double weight = 0;
                double moneys = 0;
                if (orderDetail.getItem_list().size() > 0 && orderDetail.getItem_list() != null) {
                    for (int j = 0; j < orderDetail.getItem_list().size(); j++) {
                        weight = weight + (orderDetail.getItem_list().get(j).weight) * 1000;
                        moneys = moneys + orderDetail.getItem_list().get(j).getModel_discounted_price() * orderDetail.getItem_list().get(j).getModel_quantity_purchased();
                    }
                    order.setTotalMoney(moneys);
                } else {
                    order.setTotalMoney(orderDetail.getTotal_amount() - orderDetail.getEstimated_shipping_fee());
                }
                order.setShop(shopInfo);
                order.setCustomer(cus);
                order.setCodeBill(orderDetail.getOrder_sn());
                order.setShopShipMoney(orderDetail.getTotal_amount());
                order.setPaidMoney(0.0);
                order.setDiscountPercent(0.0);
                order.setDiscountMoney(0.0);
                order.setCashMoney(orderDetail.getTotal_amount());
                if (shipping != null && shipping.getResponse() != null) {
                    order.setOrderCode(shipping.getResponse().getShipping_document_info().getTracking_number() + "/" + shipping.getResponse().getShipping_document_info().getRecipient_sort_code().getFirst_recipient_sort_code());
                }
                long time = orderDetail.getCreate_time() * 1000;
                Date date = new Date();
                date.setTime(time);
                order.setCreatedDate(date);
                order.setInnerNote(orderDetail.getNote());
                order.setPrintNote(orderDetail.getMessage_to_seller());
                order.setWeight((int) weight);
                order.setShipFee(orderDetail.getEstimated_shipping_fee());
                order.setShipPartner(orderDetail.getShipping_carrier());
                order.setShipType(orderDetail.getPayment_method());
                Optional<StatusShipped> statusShipped = repoStatus.findByStatusName(MapOrderStatus.map(orderDetail.getOrder_status(), ChannelSources.SHOPEE));
                Optional<ChannelShopee> channelShopee = repoChannelShopee.findByShopeeShopId(shopeeShopId);
                order.setStatus(MapOrderStatus.map(orderDetail.getOrder_status(), ChannelSources.SHOPEE));
                order.setStatusShipped(statusShipped.get());
                order.setShipPartner(orderDetail.getShipping_carrier());
                order.setChannel("Shopee" + " - " + channelShopee.get().getShopeeShopName());
                if (orderDetail.isCod() == true) {
                    order.setShipType("2");
                }
                order.setIsActive(false);
                order.setViewStatus(false);
                Bill copoder = repoBill.save(order);
                OrderShopee orderShopee = new OrderShopee();
                Optional<OrderShopee> orderShopee1 = repoOrderChannel.findByOrdersn(resDTO.getResponse().getOrder_list().get(i).getOrder_sn());
                orderShopee1.ifPresent(shopee -> orderShopee.setId(shopee.getId()));
                orderShopee.setOrdersn(resDTO.getResponse().getOrder_list().get(i).getOrder_sn());
                orderShopee.setOrderStatus(copoder.getStatus());
                orderShopee.setChannelShopee(channelShopee.get());
                orderShopee.setBill(copoder);
                orderShopee.setName(channelShopee.get().getShopeeShopName());
                orderShopee.setUpdateTime(new Date(orderDetail.getUpdate_time() * 1000));
                orderShopee.setBuyerUserId(String.valueOf(resDTO.getResponse().getOrder_list().get(i).getBuyer_user_id()));
                orderShopee.setPhone(phoneCus);
                repoOrderChannel.save(orderShopee);
                Set<BillDetail> billDetails = new HashSet<>();

                for (ItemOrder b : orderDetail.getItem_list()) {
                    BillDetail billDetail = new BillDetail();
                    double money = b.getModel_discounted_price() * b.getModel_quantity_purchased();
                    billDetail.setMoney(money);
                    billDetail.setPrice(b.getModel_discounted_price());
                    billDetail.setChannel("Shopee" + " - " + channelShopee.get().getShopeeShopName());
                    if (b.getModel_name() == null) {
                        billDetail.setProperties(b.getItem_name());
                    } else {
                        billDetail.setProperties(b.getModel_name());
                    }
                    if (b.getModel_id() != 0) {
                        Optional<ProductVariations> opProductVariations = repoProductVariations.findByBarcode(String.valueOf(b.getModel_id()));
                        if (opProductVariations.isPresent()) {
                            ProductVariations variations = opProductVariations.get();
                            Product product = opProductVariations.get().getProducts();
                            billDetail.setProduct(product);
                            billDetail.setVariant(variations);
                            billDetail.setBills(copoder);
                            billDetail.setQuantity(Double.valueOf(b.getModel_quantity_purchased()));
                            billDetails.add(billDetail);
                        }
                    } else {
                        Optional<ProductVariations> opProductVariations = repoProductVariations.findByBarcode(String.valueOf(b.getItem_id()));
                        ProductVariations variations = null;
                        Product product = null;
                        if (opProductVariations.isPresent()) {
                            variations = opProductVariations.get();
                            product = opProductVariations.get().getProducts();
                        }
                        billDetail.setProduct(product);
                        billDetail.setVariant(variations);
                        billDetail.setBills(copoder);
                        billDetail.setQuantity(Double.valueOf(b.getModel_quantity_purchased()));
                        billDetails.add(billDetail);
                    }
                }
                repoBillDetail.saveAll(billDetails);
                template.convertAndSend("/topic/webhookShopeeBill2/" + order.getShop().getId(), order.getId());
            }
        }
        resDTO.setSuccess(true);
        return resDTO;
    }

    public ResponseEntity cancelOrder(Long billId) {
        Map<String, Object> response = new HashMap<>();
        String host = "https://partner.test-stable.shopeemobile.com";
        String path = "/api/v2/order/cancel_order";
        Optional<OrderShopee> orderShopee = repoOrderChannel.findByBillId(billId);
        if (orderShopee.isPresent()) {
            CancelOrder cancelOrder = new CancelOrder();
            long time = Common.getCurrentTime();
            cancelOrder.setOrdersn(orderShopee.get().getOrdersn());
            cancelOrder.setCancel_reason("OUT_OF_STOCK");
            cancelOrder.setPartner_id(partnerID);
            cancelOrder.setShopid(orderShopee.get().getChannelShopee().getShopeeShopId());
            cancelOrder.setTimestamp(time);
            CancelOrderResDTO resDTO = null;
            int retries = 0;
            while (resDTO == null && retries < 5) {
                retries++;
                try {
//                    resDTO = Common.callAPIV2(Iconstants.CANCEL_ORDER_ENDPOINT,partnerKey, cancelOrder, , CancelOrderResDTO.class);
                    ObjectMapper mapper = new ObjectMapper();
                } catch (Exception e) {
                    resDTO = null;
                }
            }
            if (resDTO != null) {
                Bill bill = orderShopee.get().getBill();
                Optional<StatusShipped> statusShipped = repoStatus.findById(10L);
                bill.setStatus(statusShipped.get().getStatusName());
                bill.setStatusShipped(statusShipped.get());
                repoBill.save(bill);
                OrderShopee orderShopee1 = orderShopee.get();
                orderShopee1.setOrderStatus(statusShipped.get().getStatusName());
                repoOrderChannel.save(orderShopee1);

                response.put("message", "Đã hủy");
                response.put("message shopee", resDTO.getMsg());
                response.put("success", true);
                return new ResponseEntity(response, HttpStatus.OK);
            }
            response.put("message", resDTO.getMsg());
            response.put("success", false);
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        response.put("success", false);
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    public ResponseShipping getShipping(String order, Long shopeeShopId) {
        String host = "https://partner.shopeemobile.com";
        String access_token = shopeeService.getToken(shopeeShopId, partnerKey_Order, partnerID_Order, 2L);
        String paths = "/api/v2/logistics/get_shipping_document_info";
        String urls = "?order_sn=" + order;
        ResponseShipping shipping = null;
        int a = 0;
        while (shipping == null && a < 2) {
            a++;
            try {
                shipping = Common.callAPIV2(urls, host, paths, access_token, String.valueOf(shopeeShopId), partnerKey_Order, String.valueOf(partnerID_Order), null, ResponseShipping.class);
            } catch (Exception e) {
                shipping = null;
                e.printStackTrace();
            }
        }
        return shipping;
    }

    public ResponseEntity shipOrder(String order_sn, Long shopeeId) {
        String host = "https://partner.shopeemobile.com";
        String path = "/api/v2/logistics/get_shipping_parameter";
        String path_ship = "/api/v2/logistics/ship_order";
        String access_token = shopeeService.getToken(shopeeId, partnerKey_Order, partnerID_Order, 2L);
        String url = "?order_sn=" + order_sn;
        try {
            ResponseShippingParameter res = Common.callAPIV2(url, host, path, access_token, String.valueOf(shopeeId), partnerKey_Order, String.valueOf(partnerID_Order), null, ResponseShippingParameter.class);
            RequestShipOrder order = new RequestShipOrder();
            RequestPickup pickup = null;
            RequestDropoff dropoff = null;
            if (res.getResponse().getInfo_needed().getDropoff().length > 1 && res.getResponse().getInfo_needed().getDropoff() != null) {
                String branch_id = null;
                String sender_real_name = null;
                for (String s : res.getResponse().getInfo_needed().getDropoff()) {
                    if (s.equals("branch_id")) {
                        branch_id = s;
                    } else if (s.equals("sender_real_name")) {
                        sender_real_name = s;
                    }
                }
                if (branch_id != null) {
                    dropoff.setBranch_id(res.getResponse().getDropoff().getBranch_list()[0].getBranch_id());
                }
            }
            if (res.getResponse().getInfo_needed().getPickup().length > 1 && res.getResponse().getInfo_needed().getPickup() != null) {
                String address_id = null;
                String pickup_time_id = null;
                for (String s : res.getResponse().getInfo_needed().getPickup()) {
                    if (s.equals("address_id")) {
                        address_id = s;
                    } else if (s.equals("pickup_time_id")) {
                        pickup_time_id = s;
                    }
                }
                if (address_id != null) {
                    pickup.setAddress_id(Long.valueOf(res.getResponse().getPickup().getAddress_list()[0].getAddress_id()));
                }
                if (pickup_time_id != null) {
                    pickup.setPickup_time_id(res.getResponse().getPickup().getAddress_list()[0].getTime_slot_list()[0].getPickup_time_id());
                }
            }
            order.setOrdersn(order_sn);
            if (pickup != null) order.setPickup(pickup);
            if (dropoff != null) order.setDropoff(dropoff);
            ResponseShipping responseShipping = Common.callAPIV2(null, host, path_ship, access_token, String.valueOf(shopeeId), partnerKey_Order, String.valueOf(partnerID_Order), order, ResponseShipping.class);
            return new ResponseEntity(responseShipping, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    private Customer checkCustomer(String phone, Long shopId) {
        List<Customer> customerOpt = repoCustomer.findByPhoneAndShopId(phone, shopId);
        if (customerOpt.size() > 0) {
            return customerOpt.get(0);
        }
        return null;
    }


}
