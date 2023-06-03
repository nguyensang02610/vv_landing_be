package com.vvlanding.dto.shipped.ghtk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoShipped {
    private Long id;

    private String name;

    private String token;

    private String label;

    public void Shipped(Long id, String name , String token ,String label){
        this.id = id;
        this.name = name;
        this.token = token;
        this.label = label;
    }
}
