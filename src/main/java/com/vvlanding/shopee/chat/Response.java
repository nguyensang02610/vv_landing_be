package com.vvlanding.shopee.chat;

import com.vvlanding.shopee.order.order.Orders;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Response {

    private PageResult page_result;

    private List<MessageResponse> messages = new ArrayList<>();

    private List<Conversations> conversations = new ArrayList<>();

    private MessageContent content;

    private boolean more;
    private String next_cursor;
    private List<Orders> order_list = new ArrayList<>();
}
