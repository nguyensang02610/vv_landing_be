package com.vvlanding.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.vvlanding.dto.DtoOrder;
import com.vvlanding.dto.DtoOrderDetail;
import com.vvlanding.mapper.MapperBillDetail;
import com.vvlanding.repo.*;
import com.vvlanding.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {
    @Autowired
    SimpMessagingTemplate template;

    @Autowired
    SerCustomer serCustomer;

    @Autowired
    SerProduct serProduct;

    @Autowired
    SerProductVariations serProductVariations;

    @Autowired
    SerShopInfo serShopInfo;

    @Autowired
    RepoBillDetail repoBillDetail;

    @Autowired
    RepoBill repoBill;

    @Autowired
    RepoShopInfo repoShopInfo;

    @Autowired
    RepoCustomer repoCustomer;

    @Autowired
    RepoProduct repoProduct;

    @Autowired
    RepoStatus repoStatus;

    @Autowired
    RepoProductVariations repoProductVariations;

    @Autowired
    MapperBillDetail mapperBillDetail;

    @Autowired
    SerCheckIp serCheckIp;

    @Autowired
    RepoFirebase repoFirebase;

//    Long - QUEUE
    //    @EventListener

//    @RabbitListener(queues = MQConfig.QUEUE)
//    public void InsSentRabbit(DtoOrder dtoOrder) {
//        System.out.println("listenerBill----"+ dtoOrder.getWeight());
//        Map<String, Object> response = new HashMap<>();
//        try {
//            Bill order = new Bill();
//            // kiểm tra thông tin shop qua id shop filter
//            Optional<ShopInfo> opShopInfo = repoShopInfo.findById(dtoOrder.getShopId());
//            ShopInfo shopInfo = opShopInfo.get();
//            if (!opShopInfo.isPresent()) {
//                System.out.println("Không có shop ");
//            }
//            Optional<Product> opProduct = repoProduct.findById(dtoOrder.getProductId());
//            Product product = opProduct.get();
//            // kiểm tra sđt khách hàng  nếu có thì trả ra, nếu không thì thêm mới rồi trả ra
//            String customerName = dtoOrder.getCustomerName();
//            String customerFulladdress = dtoOrder.getCustomerFulladdress();
//            String customerPhone = dtoOrder.getCustomerPhone();
//            String customerProvince = dtoOrder.getCustomerProvince();
//            String customerDistrict = dtoOrder.getCustomerDistrict();
//            String customerWard = dtoOrder.getCustomerWard();
//            Long shopId = dtoOrder.getShopId();
//            Customer cus = checkCustomer(customerPhone, shopId);
//            if (cus == null) {
//                Customer newCustomer = new Customer();
//                newCustomer.setTitle(customerName);
//                newCustomer.setPhone(customerPhone);
//                newCustomer.setDistrict(customerDistrict);
//                newCustomer.setProvince(customerProvince);
//                newCustomer.setWard(customerWard);
//                newCustomer.setAddress(customerFulladdress);
//                newCustomer.setActive(true);
//                newCustomer.setShopId(shopId);
//                cus = repoCustomer.save(newCustomer);
//            }
//
//            Double shipFee = formatStringToDouble(dtoOrder.getShipFee());
//            Double totalMoney = dtoOrder.getTotalMoney();
//            Double shopShipMoney = totalMoney + shipFee;
//            Double paidMoney = formatStringToDouble(dtoOrder.getPaidMoney());
//            Double discountPercent = formatStringToDouble(dtoOrder.getDiscountPercent());
//            Double discountMoney = formatStringToDouble(dtoOrder.getDiscountMoney());
//            Double cashMoney = dtoOrder.getTotalMoney();
//            String codeBill = product.getSku() + new Date().getTime();
//
//            order.setShop(shopInfo);
//            order.setCustomer(cus);
//            order.setCodeBill(codeBill);
//            order.setTotalMoney(totalMoney);
//            order.setShopShipMoney(shopShipMoney);
//            order.setPaidMoney(paidMoney);
//            order.setDiscountPercent(discountPercent);
//            order.setDiscountMoney(discountMoney);
//            order.setCashMoney(cashMoney);
//            order.setInnerNote(dtoOrder.getInnerNote());
//            order.setPrintNote(dtoOrder.getPrintNote());
//            order.setWeight(dtoOrder.getWeight());
//            order.setShipFee(shipFee);
//            order.setShipPartner(dtoOrder.getShipPartner());
//            order.setShipType("2");
//            order.getShipType();
//            order.setStatus("Đơn mới");
//            order.setIsActive(false);
//            order.setChannel(dtoOrder.getChannel());
//            Optional<StatusShipped> statusShipped = repoStatus.findById(1L);
//            order.setStatus(statusShipped.get().getStatusName());
//            order.setStatusShipped(statusShipped.get());
//            Bill copoder = repoBill.save(order);
//            List<DtoOrderDetail> dtoOrderDetailList = dtoOrder.getOrderDetails();
//            List<DtoOrderDetail> dataOrderDetails = createListOrderDetail(dtoOrderDetailList, copoder, dtoOrder);
//            dtoOrder.setOrderDetails(dataOrderDetails);
//            dtoOrder.setId(copoder.getId());
//            template.convertAndSend("/topic/message/"+dtoOrder.getShopId(), dtoOrder.getId());
//            System.out.println("Khác hàng "+dtoOrder.getCustomerName()+" vừa đặt hàng");
//        } catch (Exception ex) {
//            System.out.println("Thêm thất bại ");
//        }
//    }


    // kiểm tra xem đã có khách hàng này chưa qua sđt
    private Customer checkCustomer(String phone, Long shopId) {
        List<Customer> customerOpt = repoCustomer.findByPhoneAndShopId(phone, shopId);
        if (customerOpt.size() > 0) {
            return customerOpt.get(0);
        }
        return null;
    }

    public Double formatStringToDouble (String data){
        Double price = 0.0;
        String originPrice = data;
        if (originPrice != null && originPrice.length() > 0) {
            price = Double.parseDouble(originPrice);
        }
        return price;
    }


    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "qwertyuiopasdfghjklzxcvbnm0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    //Công - Order LandingPage
    public Object InsSent(DtoOrder dtoOrder) {
        Map<String, Object> response = new HashMap<>();
        try {
            Bill order = new Bill();

            // kiểm tra thông tin shop qua id shop filter
            Optional<ShopInfo> opShopInfo = repoShopInfo.findById(dtoOrder.getShopId());
            ShopInfo shopInfo = opShopInfo.get();
            if (!opShopInfo.isPresent()) {
                response.put("message", "Không tìm thấy thông tin shop !!");
                response.put("success", false);
                return response;
            }
            if (serCheckIp.checkIp(dtoOrder.getIpAddress()) == false){
//                response.put("message","ip của bạn đã bị chặn");
                response.put("success", false);
                return response;
            }
//            Optional<Product> opProduct = repoProduct.findById(dtoOrder.getProductId());
//            Product product = opProduct.get();

            // kiểm tra sđt khách hàng  nếu có thì trả ra, nếu không thì thêm mới rồi trả ra
            String customerName = dtoOrder.getCustomerName();
            String customerFulladdress = dtoOrder.getCustomerFulladdress();
            String customerPhone = dtoOrder.getCustomerPhone();
            String customerProvince = dtoOrder.getCustomerProvince();
            String customerDistrict = dtoOrder.getCustomerDistrict();
            String customerWard = dtoOrder.getCustomerWard();
            Long shopId = dtoOrder.getShopId();
            Customer cus = checkCustomer(customerPhone, shopId);
            if (cus == null) {
                Customer newCustomer = new Customer();
                newCustomer.setTitle(customerName);
                newCustomer.setPhone(customerPhone);
                newCustomer.setDistrict(customerDistrict);
                newCustomer.setProvince(customerProvince);
                newCustomer.setWard(customerWard);
                newCustomer.setAddress(customerFulladdress);
                newCustomer.setActive(true);
                newCustomer.setShopId(shopId);
                cus = repoCustomer.save(newCustomer);
            }
            Double shipFee = formatStringToDouble(dtoOrder.getShipFee());
            Double totalMoney = dtoOrder.getTotalMoney();
            Double shopShipMoney = totalMoney + shipFee;
            Double cashMoney = dtoOrder.getTotalMoney();
            order.setShop(shopInfo);
            order.setCustomer(cus);
            order.setCodeBill(new Date().getTime() + getRandomNumber(3));
            order.setTotalMoney(totalMoney);
            order.setShopShipMoney(shopShipMoney);
            order.setPaidMoney(dtoOrder.getPaidMoney());
            order.setDiscountPercent(dtoOrder.getDiscountPercent());
            order.setDiscountMoney(dtoOrder.getDiscountMoney());
            order.setCashMoney(cashMoney);
            order.setInnerNote(dtoOrder.getInnerNote());
            order.setPrintNote(dtoOrder.getPrintNote());
            order.setWeight(dtoOrder.getWeight());
            order.setShipFee(shipFee);
            order.setShipPartner(dtoOrder.getShipPartner());
            order.setIpAddress(dtoOrder.getIpAddress());
            order.setShipType("2");
            order.getShipType();
            order.setStatus("Đơn mới");
            order.setIsActive(false);
            order.setViewStatus(false);
            order.setChannel("LD - Hệ thống");
            Optional<StatusShipped> statusShipped = repoStatus.findById(1L);
            order.setStatus(statusShipped.get().getStatusName());
            order.setStatusShipped(statusShipped.get());
            Bill copoder = repoBill.save(order);
            List<DtoOrderDetail> dtoOrderDetailList = dtoOrder.getOrderDetails();
            List<DtoOrderDetail> dataOrderDetails = createListOrderDetail(dtoOrderDetailList, copoder);
            //json FireBase
            dtoOrder.setOrderDetails(dataOrderDetails);
            dtoOrder.setId(copoder.getId());
            dtoOrder.setCodeBill(copoder.getCodeBill());
            dtoOrder.setShopShipMoney(copoder.getShopShipMoney());
            dtoOrder.setPaidMoney(copoder.getPaidMoney());
            dtoOrder.setDiscountMoney(copoder.getDiscountMoney());
            dtoOrder.setDiscountPercent(copoder.getDiscountPercent());
            dtoOrder.setCashMoney(copoder.getCashMoney());
            dtoOrder.setShipPartner(copoder.getShipPartner());
            dtoOrder.setStatus(copoder.getStatus());
            dtoOrder.setIsActive(copoder.getIsActive());
            dtoOrder.setCreatedDate(copoder.getCreatedDate());
            template.convertAndSend("/topic/message/"+dtoOrder.getShopId(), dtoOrder.getId());

            Optional<Firebase> firebase = repoFirebase.findByShopInfoId(dtoOrder.getShopId());
            if (firebase.isPresent()) {
                String id = String.valueOf(copoder.getId());
                Message message = Message.builder()
                        .setToken(firebase.get().getToken())
                        .setNotification(new Notification(copoder.getCustomer().getPhone(), copoder.getStatus()))
                        .putData("Id", id)
                        .putData("Channel", copoder.getChannel())
                        .putData("customerPhone", copoder.getCustomer().getPhone())
                        .putData("status", copoder.getStatus())
                        .build();
                try {
                    FirebaseMessaging.getInstance().send(message);
                    response.put("message", "Gửi thông báo FireBase thành công");
                } catch (FirebaseMessagingException e) {
                    response.put("message", "Gửi thông báo FireBase thất bại, kiểm tra fcm token");
                }
            }

            response.put("data", dtoOrder);
            response.put("success", true);
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            response.put("data", ex);
            response.put("success", false);
            return response;
        }
    }

    // ins bill hệ thống
    public Object InsSent2(DtoOrder dtoOrder) {
        Map<String, Object> response = new HashMap<>();
        try {
            Bill order = new Bill();
            // kiểm tra thông tin shop qua id shop filter
            Optional<ShopInfo> opShopInfo = repoShopInfo.findById(dtoOrder.getShopId());
            ShopInfo shopInfo = opShopInfo.get();
            if (!opShopInfo.isPresent()) {
                response.put("message", "Không tìm thấy thông tin shop !!");
                response.put("success", false);
                return response;
            }

//            Optional<Product> opProduct = repoProduct.findById(dtoOrder.getProductId());
//            Product product = opProduct.get();

            // kiểm tra sđt khách hàng  nếu có thì trả ra, nếu không thì thêm mới rồi trả ra
            String customerName = dtoOrder.getCustomerName();
            String customerFulladdress = dtoOrder.getCustomerFulladdress();
            String customerPhone = dtoOrder.getCustomerPhone();
            String customerProvince = dtoOrder.getCustomerProvince();
            String customerDistrict = dtoOrder.getCustomerDistrict();
            String customerWard = dtoOrder.getCustomerWard();
            Long shopId = dtoOrder.getShopId();
            Customer cus = checkCustomer(customerPhone, shopId);
            if (cus == null) {
                Customer newCustomer = new Customer();
                newCustomer.setTitle(customerName);
                newCustomer.setPhone(customerPhone);
                newCustomer.setDistrict(customerDistrict);
                newCustomer.setProvince(customerProvince);
                newCustomer.setWard(customerWard);
                newCustomer.setAddress(customerFulladdress);
                newCustomer.setActive(true);
                newCustomer.setShopId(shopId);
                cus = repoCustomer.save(newCustomer);
            }
            Double shipFee = formatStringToDouble(dtoOrder.getShipFee());
            Double totalMoney = dtoOrder.getTotalMoney();
            Double shopShipMoney = totalMoney + shipFee;
            Double cashMoney = dtoOrder.getTotalMoney();
            order.setShop(shopInfo);
            order.setCustomer(cus);
            order.setCodeBill(new Date().getTime() + getRandomNumber(3));
            order.setTotalMoney(totalMoney);
            order.setShopShipMoney(shopShipMoney);
            order.setPaidMoney(dtoOrder.getPaidMoney());
            order.setDiscountPercent(dtoOrder.getDiscountPercent());
            order.setDiscountMoney(dtoOrder.getDiscountMoney());
            order.setCashMoney(cashMoney);
            order.setInnerNote(dtoOrder.getInnerNote());
            order.setPrintNote(dtoOrder.getPrintNote());
            order.setWeight(dtoOrder.getWeight());
            order.setShipFee(shipFee);
            order.setShipPartner(dtoOrder.getShipPartner());
            order.setShipType("2");
            order.getShipType();
            order.setStatus("Đơn mới");
            order.setIsActive(false);
            order.setViewStatus(false);
            order.setChannel("LD - Hệ thống");
            Optional<StatusShipped> statusShipped = repoStatus.findById(1L);
            order.setStatus(statusShipped.get().getStatusName());
            order.setStatusShipped(statusShipped.get());
            Bill copoder = repoBill.save(order);
            List<DtoOrderDetail> dtoOrderDetailList = dtoOrder.getOrderDetails();
            List<DtoOrderDetail> dataOrderDetails = createListOrderDetail(dtoOrderDetailList, copoder);
            //json FireBase
            dtoOrder.setOrderDetails(dataOrderDetails);
            dtoOrder.setId(copoder.getId());
            dtoOrder.setCodeBill(copoder.getCodeBill());
            dtoOrder.setShopShipMoney(copoder.getShopShipMoney());
            dtoOrder.setPaidMoney(copoder.getPaidMoney());
            dtoOrder.setDiscountMoney(copoder.getDiscountMoney());
            dtoOrder.setDiscountPercent(copoder.getDiscountPercent());
            dtoOrder.setCashMoney(copoder.getCashMoney());
            dtoOrder.setShipPartner(copoder.getShipPartner());
            dtoOrder.setStatus(copoder.getStatus());
            dtoOrder.setIsActive(copoder.getIsActive());
            dtoOrder.setCreatedDate(copoder.getCreatedDate());
            template.convertAndSend("/topic/message/"+dtoOrder.getShopId(), dtoOrder.getId());

            Optional<Firebase> firebase = repoFirebase.findByShopInfoId(dtoOrder.getShopId());
            if (firebase.isPresent()) {
                String id = String.valueOf(copoder.getId());
                Message message = Message.builder()
                        .setToken(firebase.get().getToken())
                        .setNotification(new Notification(copoder.getCustomer().getPhone(), copoder.getStatus()))
                        .putData("Id", id)
                        .putData("Channel", copoder.getChannel())
                        .putData("customerPhone", copoder.getCustomer().getPhone())
                        .putData("status", copoder.getStatus())
                        .build();
                try {
                    FirebaseMessaging.getInstance().send(message);
                    response.put("message", "Gửi thông báo FireBase thành công");
                } catch (FirebaseMessagingException e) {
                    response.put("message", "Gửi thông báo FireBase thất bại, kiểm tra fcm token");
                }
            }

            response.put("data", dtoOrder);
            response.put("success", true);

            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            response.put("data", ex);
            response.put("success", false);
            return response;
        }
    }



    private ProductVariations checkVariations(Long id){
        List<ProductVariations> productVariations = repoProductVariations.findALLById(id);
        if(productVariations.size() > 0){
            return productVariations.get(0);
        }
        return null;
    }

    // tạo chi tiết order
    public List<DtoOrderDetail> createListOrderDetail(List<DtoOrderDetail> data, Bill bill) {
        List<DtoOrderDetail> dtoOrderDetailList = new ArrayList<>();
        for (DtoOrderDetail dtoOrderDetail : data) {
            BillDetail orderDetail = new BillDetail();
            Optional<Product> opProduct = repoProduct.findById(dtoOrderDetail.getProductId());
            Product product = opProduct.get();

            Optional<ProductVariations> opProductVariations = repoProductVariations.findById(dtoOrderDetail.getVariantId());
            ProductVariations variations = opProductVariations.get();

            Long idchek = dtoOrderDetail.getVariantId();
            ProductVariations variations2 = checkVariations(idchek);
            Double quantity = dtoOrderDetail.getQuantity();
            orderDetail.setPrice(variations2.getSaleprice());
            orderDetail.setQuantity(quantity);
            orderDetail.setMoney(variations2.getSaleprice()*quantity);
            orderDetail.setProduct(product);
            orderDetail.setVariant(variations);
            orderDetail.setBills(bill);
            orderDetail.setProperties(dtoOrderDetail.getProperties());
            orderDetail.setChannel("LD - Hệ thống");
            BillDetail Detail = repoBillDetail.save(orderDetail);
            orderDetail.setId(Detail.getId());
            //json FireBase
            DtoOrderDetail dtoData = new DtoOrderDetail();
            dtoData.setId(Detail.getId());
            dtoData.setProductId(Detail.getProduct().getId());
            dtoData.setProductTitle(Detail.getProduct().getTitle());
            dtoData.setProductImages(Detail.getProduct().getImages());
            dtoData.setVariantId(Detail.getVariant().getId());
            dtoData.setQuantity(Detail.getQuantity());
            dtoData.setProperties(Detail.getProperties());
            dtoData.setPrice(Detail.getPrice());
            dtoData.setMoney(Detail.getMoney());
            dtoData.setWeightProduct(Detail.getVariant().getWeight());
            dtoData.setChannel(Detail.getChannel());
//                DtoOrderDetail newDetail = mapperBillDetail.toDto(orderDetail);
            dtoOrderDetailList.add(dtoData);


        }
        return dtoOrderDetailList;
    }

}



