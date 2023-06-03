package com.vvlanding.shopee.webhook.shopee;

public class MapOrderStatus {

    public static String map(String anotherStatus, String channel) {
        switch (channel) {
            case ChannelSources.SHOPEE: {
                return mapShopee(anotherStatus);
            }
        }
        return null;
    }

    private static String mapShopee(String status) {
        switch (status) {
            case "UNPAID":
                return OrderStatus.NEW_ORDER.getVNText();

            case "APPROVED":
                return OrderStatus.READY_TO_SHIP.getVNText();

            case "READY_TO_SHIP":
                return OrderStatus.READY_TO_SHIP.getVNText();

            case "RETRY_SHIP":
                return OrderStatus.DELAY.getVNText();

            case "SHIPPED":
                return OrderStatus.SHIPPING.getVNText();

            case "TO_CONFIRM_RECEIVE":
                return OrderStatus.SHIPPING.getVNText();

            case "IN_CANCEL":
                return OrderStatus.CANCELLED.getVNText();

            case "CANCELLED":
                return OrderStatus.CANCELLED.getVNText();

            case "TO_RETURN":
                return OrderStatus.TO_RETURN.getVNText();

            case "COMPLETED":
                return OrderStatus.CROSS_CHECKED.getVNText();

            case "PROCESSED":
                return OrderStatus.READY_TO_SHIP.getVNText();

            case "INVOICE_PENDING":
                return OrderStatus.READY_TO_SHIP.getVNText();
        }
        return null;
    }
}
