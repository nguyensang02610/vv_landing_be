package com.vvlanding.payload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private  Object user;
    public JwtAuthenticationResponse(String accessToken,Object user) {
        this.user = user;
        this.accessToken = accessToken;
    }
}
