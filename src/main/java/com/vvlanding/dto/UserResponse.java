package com.vvlanding.dto;

import com.vvlanding.table.ShopUserRole;
import com.vvlanding.table.User;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserResponse {

    private Long id;

    private String username;

    private Boolean active;

    private String image;

    private String title;

    private String phone;

    private String email;

    private String role;

    public UserResponse(Long id, String username,Boolean active,
                         String email, String phone, String image, String title,
                         String role) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.username = username;
        this.active = active;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    public static UserResponse create(User user, String role) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getActive(),
                user.getEmail(),
                user.getPhone(),
                user.getImage(),
                user.getTitle(),
                role
        );
    }
}
