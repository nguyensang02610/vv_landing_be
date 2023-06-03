package com.vvlanding.shopee;


import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestFBPage {
    @NotNull
    private String fbUserID;
    @NotNull
    private String fbUsername;
    @NotNull
    private String posUserID;
    @NotNull
    private String shopID;
    private List<FBPage> fbPages = new ArrayList<>();

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class FBPage {
        @NotNull
        private String id;
        @NotNull
        private String name;
    }
}
