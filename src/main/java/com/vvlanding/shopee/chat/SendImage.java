package com.vvlanding.shopee.chat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SendImage {
    private String file;
    private  String conversation_id;
    private  long shopeeId;
    private String partnerKeyV2;
    private long partnerIdV2;
    private Long ShopId;
}
