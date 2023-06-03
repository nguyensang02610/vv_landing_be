package com.vvlanding.shopee.webhook.shopee;

import java.util.stream.Stream;

public enum OrderStatus {

    NEW_ORDER(0), APPROVED(1), READY_TO_SHIP(2), SHIPPING(3), DELIVERED(4), TO_RETURN(5), RETURNED(6),
    CROSS_CHECKED(7), DELAY(8), CANCELLED(9);

    private int status;

    OrderStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getText() {
        switch (this.status) {
            case 0:
                return "NEW_ORDER";

            case 1:
                return "APPROVED";

            case 2:
                return "READY_TO_SHIP";

            case 3:
                return "SHIPPING";

            case 4:
                return "DELIVERED";

            case 5:
                return "TO_RETURN";

            case 6:
                return "RETURNED";

            case 7:
                return "CROSS_CHECKED";

            case 8:
                return "DELAY";

            case 9:
                return "CANCELLED";
        }
        return null;
    }

    public String getText(int status) {
        switch (status) {
            case 0:
                return "NEW_ORDER";

            case 1:
                return "APPROVED";

            case 2:
                return "READY_TO_SHIP";

            case 3:
                return "SHIPPING";

            case 4:
                return "DELIVERED";

            case 5:
                return "TO_RETURN";

            case 6:
                return "RETURNED";

            case 7:
                return "CROSS_CHECKED";

            case 8:
                return "DELAY";

            case 9:
                return "CANCELLED";
        }
        return null;
    }

    public String getVNText() {
        switch (this.status) {
            case 0:
                return "Đơn mới";

            case 1:
                return "Đã xác nhận";

            case 2:
                return "Gửi vận chuyển";

            case 3:
                return "Đang giao";

            case 4:
                return "Đã giao";

            case 5:
                return "Chờ hoàn";

            case 6:
                return "Đã hoàn";

            case 7:
                return "Đã đối soát";

            case 8:
                return "Delay giao hàng";

            case 9:
                return "Đã hủy";
        }
        return null;
    }

    public static OrderStatus of(int status) {
        return Stream.of(OrderStatus.values()).filter(p -> p.getStatus() == status).findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

