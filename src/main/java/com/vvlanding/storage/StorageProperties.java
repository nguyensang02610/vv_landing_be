package com.vvlanding.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("storage")
@Getter
@Setter
public class StorageProperties {
    /**
     * Folder location for storing files
     */
    private String shopImageLocation = "product_images";

    private String themeImage64 = "theme-image64";

    private String shopLogoLocation = "shop_logo";

    private String avatarLocation = "member-avatars";

    private String themeImage = "theme-image";
}
