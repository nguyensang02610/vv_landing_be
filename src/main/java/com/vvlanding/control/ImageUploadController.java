package com.vvlanding.control;

import com.vvlanding.reponse.message.ResponseStatusMessage;
import com.vvlanding.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("upload")
public class ImageUploadController {
    @Autowired
    private StorageService storageService;

    // upload ảnh sản phẩm
    @PostMapping(value = "/shop/{shopID}")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile[] files, @PathVariable long shopID) {
        List<String> storedFilePaths = new ArrayList<String>();
        System.out.println(files);
        for(MultipartFile file : files){
            System.out.println(file.getName());
        }
        try {
            for (MultipartFile file : files) {
                String storedFilePath = storageService.storeShopImage(file, shopID);
                storedFilePaths.add(storedFilePath);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseStatusMessage(false, "Cannot store this file", e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseStatusMessage(true, "Upload file successfully", storedFilePaths),
                HttpStatus.ACCEPTED);
    }

    // trang chuyen doi anh sang base 64:  https://www.base64-image.de/
    @PostMapping(value = "/imagebase64/{shopid}")
    public ResponseEntity<?> handleFileUploadBase64(@RequestParam("file") String base64String, @PathVariable int shopid) {
        List<String> storedFilePaths = new ArrayList<String>();
        try {

                String storedFilePath = storageService.storeBase64Image(base64String, shopid);
                storedFilePaths.add(storedFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseStatusMessage(false, "Cannot store this file", e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseStatusMessage(true, "Upload file successfully", storedFilePaths),
                HttpStatus.ACCEPTED);
    }


    // upload ảnh shop
    @PostMapping(value = "/shop/logo")
    public ResponseEntity<?> storeShopLogo(@RequestParam("file") MultipartFile file) {
        List<String> storedFilePaths = new ArrayList<String>();
        try {
            String storedFilePath = storageService.storeShopLogo(file);
            storedFilePaths.add(storedFilePath);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseStatusMessage(false, "Cannot store this file", e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseStatusMessage(true, "Upload file successfully", storedFilePaths),
                HttpStatus.ACCEPTED);
    }

    // upload ảnh user
    @PostMapping(value = "/user/image")
    public ResponseEntity<?> userShopLogo(@RequestParam("file") MultipartFile file) {
        List<String> storedFilePaths = new ArrayList<String>();
        try {
            String storedFilePath = storageService.userImage(file);
            storedFilePaths.add(storedFilePath);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseStatusMessage(false, "Cannot store this file", e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseStatusMessage(true, "Upload file successfully", storedFilePaths),
                HttpStatus.ACCEPTED);
    }

    // upload ảnh landing
    @PostMapping(value = "landingpage/image/{shopID}")
    public ResponseEntity<?> image(@RequestParam("file") MultipartFile[] files,@PathVariable long shopID) {
        List<String> storedFilePaths = new ArrayList<String>();
        System.out.println(files);
        for(MultipartFile file : files){
            System.out.println(file.getName());
        }
        try {
            for (MultipartFile file : files) {
                String storedFilePath = storageService.themeImage(file, shopID);
                storedFilePaths.add(storedFilePath);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseStatusMessage(false, "Cannot store this file", e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseStatusMessage(true, "Upload file successfully", storedFilePaths),
                HttpStatus.ACCEPTED);
    }

}



