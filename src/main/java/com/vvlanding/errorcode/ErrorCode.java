package com.vvlanding.errorcode;

public class ErrorCode {
    public static final String WAREHOUSE_NOT_FOUND = "4000 - Không tìm thấy kho hàng";
    public static final String PRODUCT_NOT_FOUND = "4001 - Không tìm thấy sản phẩm";
    public static final String PRODUCT_VARIANT_NOT_FOUND = "4002 - Không tìm thấy variant sản phẩm";
    public static final String INSERT_IMPORT_TICKET_FAILED = "4003 - Lỗi lưu hóa đơn nhập";
    public static final String INSERT_ORDER_FAILED = "4004 - Lỗi đơn hàng";
    public static final String INSERT_ORDER_QUANTITY_NOT_ENOUGH = "4005 - Số lượng sản phẩm không đủ: ";

    public static final String EDIT_ORDER_FAILED = "4004 - Lỗi đơn hàng";
    public static final String EDIT_ORDER_QUANTITY_NOT_ENOUGH = "4005 - Số lượng sản phẩm không đủ: ";


    public static final String UPDATE_ORDER_STATUS_NOT_FOUND_ORDER_ID = "4005 - Không tìm thấy mã đơn hàng: ";
    public static final String UPDATE_ORDER_STATUS_STATUS_NOT_CORRECT = "4006 - Thay đổi trạng thái đơn hàng không đúng: ";


    public static final String SKU_EXIST = "4001 - Mã sản phẩm đã tồn tại";
}
