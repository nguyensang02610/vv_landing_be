package com.vvlanding.shopee.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageResult {
    private int page_size;

    private boolean more;

    private NextCursor next_cursor;

    private String next_offset;
}
