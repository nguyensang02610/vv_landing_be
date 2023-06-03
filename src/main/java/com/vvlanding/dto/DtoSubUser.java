package com.vvlanding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DtoSubUser {
    private String username;
    private String email;
    private String password;
    private String title;
    private String phone;
    private String image;
    private List<String> authors;

}
