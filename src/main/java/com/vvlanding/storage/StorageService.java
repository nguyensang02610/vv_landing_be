package com.vvlanding.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.nio.file.Path;
import java.util.stream.Stream;


public interface StorageService {
    void init();

    String storeShopImage(MultipartFile file, long shopid);

    String storeShopImageByte(byte[] pdfFile, int shopid);

    String storeAvatar(MultipartFile file);

    String storeBase64Image(String base64String, int shopid);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

    String storeShopLogo(MultipartFile file);

    String storeShopeeImage(String url, long shopID);

    String userImage(MultipartFile file);

    String themeImage(MultipartFile file, long shopid);

}
