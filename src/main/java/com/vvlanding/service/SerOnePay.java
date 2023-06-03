package com.vvlanding.service;

import com.vvlanding.dto.payment.DtoOnePay;
import com.vvlanding.dto.payment.DtoPaymentOnePay;
import com.vvlanding.repo.RepoBill;
import com.vvlanding.repo.RepoPayment;
import com.vvlanding.repo.RepoShopInfo;
import com.vvlanding.table.Bill;
import com.vvlanding.table.Payment;
import com.vvlanding.table.ShopInfo;
import com.vvlanding.utils.Constant;
import com.vvlanding.utils.HmacSHA256OnePay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SerOnePay {

    private static final String url = "https://mtf.onepay.vn/paygate/vpcpay.op";
    private static final String vpc_Locale = "vn";
    private static final String vpc_ReturnURL = "http://localhost:3980/api/onepay/return";
    private static final String name = "onepay";

    @Autowired
    private RepoPayment repoPaymentOnePay;

    @Autowired
    private RepoBill repoBill;

    @Autowired
    private RepoShopInfo repoShopInfo;

    public String createOnePay(DtoOnePay dtoOnePay, Long shopId) {
        Optional<Payment> paymentOnePay = repoPaymentOnePay.findByShopInfoIdAndName(shopId, name);
        Optional<Bill> bill = repoBill.findByCodeBill(dtoOnePay.getOrderInfo());
        if (paymentOnePay.isPresent() && bill.isPresent()) {
            Payment onePay = paymentOnePay.get();
            Map<String, String> params = new HashMap<>();
            String price = bill.get().getTotalMoney().longValue() + "00";
            String random = getRandomNumber(9);
            params.put("vpc_Version", "2");
            params.put("vpc_Currency", "VND");
            params.put("vpc_Command", "pay");
            params.put("vpc_AccessCode", onePay.getValue3());
            params.put("vpc_Merchant", onePay.getValue2());
            params.put("vpc_Locale", vpc_Locale);
            params.put("vpc_ReturnURL", vpc_ReturnURL);
            params.put("vpc_MerchTxnRef", random);
            params.put("vpc_OrderInfo", bill.get().getCodeBill());
            params.put("vpc_Amount", price);
            params.put("vpc_TicketNo", bill.get().getIpAddress());
            params.put("AgainLink", dtoOnePay.getAgainLink());
            params.put("Title", dtoOnePay.getTitle());
            return url + "?" + HmacSHA256OnePay.hashAllFields(params, onePay.getValue1());
        }
        return null;
    }

    public String returnOnePay(Map<String, String> map) {
        try {
            String code = map.get("vpc_SecureHash");
            String status = map.get("vpc_TxnResponseCode");
            String orderInfo = map.get("vpc_OrderInfo");
            Optional<Bill> bill = repoBill.findByCodeBill(orderInfo);
            if (bill.isPresent()) {
                Optional<Payment> paymentOnePay = repoPaymentOnePay.findByShopInfoIdAndName(bill.get().getShop().getId(), name);
                map.remove("vpc_SecureHash");
                if (HmacSHA256OnePay.checkHMAC(map, paymentOnePay.get().getValue1()).equals(code)) {
                    if (status.equals("0")) {
                        return "0";
                    }
                }
            }
            return status;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String ipnOnePay(Map<String, String> map) {
        try {
            String code = map.get("vpc_SecureHash");
            String status = map.get("vpc_TxnResponseCode");
            String orderInfo = map.get("vpc_OrderInfo");
            String idPayment = map.get("vpc_TransactionNo");
            String price = map.get("vpc_Amount");
            String amount = price.substring(0, price.length() - 2);
            Optional<Bill> bill = repoBill.findByCodeBill(orderInfo);
            if (bill.isPresent()) {
                Optional<Payment> paymentOnePay = repoPaymentOnePay.findByShopInfoIdAndName(bill.get().getShop().getId(), name);
                map.remove("vpc_SecureHash");
                if (HmacSHA256OnePay.checkHMAC(map, paymentOnePay.get().getValue1()).equals(code)) {
                    if (status.equals("0")) {
                        Bill bills = bill.get();
                        bills.setPaymentId(idPayment);
                        bills.setPaidMoney(Double.valueOf(amount));
                        repoBill.save(bills);
                    }
                }
            }
            return "responsecode=1&desc=confirm-success";
        } catch (Exception e) {
            return "responsecode=1&desc=confirm-success";
        }
    }

    public ResponseEntity<?> configOnePay(DtoPaymentOnePay dto) {
        try {
            Optional<ShopInfo> shopInfo = repoShopInfo.findById(dto.getShopId());
            if (shopInfo.isPresent()) {
                Payment onePay = new Payment();
                onePay.setValue3(dto.getAccessCode());
                onePay.setValue1(dto.getHashCode());
                onePay.setValue2(dto.getMerchant());
                onePay.setName(name);
                onePay.setShopInfo(shopInfo.get());
                repoPaymentOnePay.save(onePay);
                return ResponseEntity.ok(Constant.res("", true));
            }
            return ResponseEntity.badRequest().body(Constant.res("không tìm thấy shop", false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Constant.res(e.getMessage(), false));
        }
    }

    public ResponseEntity<?> updateConfigOnePay(DtoPaymentOnePay dto) {
        try {
            Optional<Payment> paymentOnePay = repoPaymentOnePay.findById(dto.getId());
            if (!paymentOnePay.isPresent())
                return ResponseEntity.badRequest().body(Constant.res("không tìm thấy payment", false));
            Optional<ShopInfo> shopInfo = repoShopInfo.findById(dto.getShopId());
            if (shopInfo.isPresent()) {
                Payment onePay = paymentOnePay.get();
                onePay.setValue3(dto.getAccessCode());
                onePay.setValue1(dto.getHashCode());
                onePay.setValue2(dto.getMerchant());
                repoPaymentOnePay.save(onePay);
                return ResponseEntity.ok(Constant.res("", true));
            }
            return ResponseEntity.badRequest().body(Constant.res("không tìm thấy shop", false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Constant.res(e.getMessage(), false));
        }
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
}
