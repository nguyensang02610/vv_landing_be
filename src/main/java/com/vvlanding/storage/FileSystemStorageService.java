package com.vvlanding.storage;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.UUID;
import java.util.stream.Stream;


@Service
public class FileSystemStorageService implements StorageService {
    private final Path rootLocation;

    private final Path avatarLocation;

    private final Path themeImage;

    private final Path shopLogoLocation;

    private final Path themeImage64;

    private static String separator = "/";

    private static Path Diachi = Paths.get("/opt/ImagesTomcat/" );

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getShopImageLocation());

        this.avatarLocation = Paths.get(properties.getAvatarLocation());

        this.shopLogoLocation = Paths.get(properties.getShopLogoLocation());

        this.themeImage = Paths.get(properties.getThemeImage());

        this.themeImage64 = Paths.get(properties.getThemeImage64());
    }

    // hung pham user image
    @Override
    public String userImage(MultipartFile file) {
        String extension = null;
        String originalFilename = file.getOriginalFilename().toLowerCase();
        if (originalFilename.endsWith(".jpg")) {
            extension = ".jpg";
        }
        if (originalFilename.endsWith(".png")) {
            extension = ".png";
        }
        if (originalFilename.endsWith(".jpeg")) {
            extension = ".jpeg";
        }
        if (extension == null) {
            throw new StorageException("Failed to store file " + originalFilename);
        }

        try {
            Path memberFolder = Diachi.resolve(this.avatarLocation);
            if (!Files.exists(memberFolder)) {
                Files.createDirectories(memberFolder);
            }

            String fileName = UUID.randomUUID().toString() + extension;

            Path newFilePath = memberFolder.resolve(fileName);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, newFilePath, StandardCopyOption.REPLACE_EXISTING);
            }

            String relativePath = Diachi + separator + this.avatarLocation + separator + fileName;

            return relativePath;

        } catch (IOException e) {
            throw new StorageException("Failed to store file " + originalFilename, e);
        }
    }


    @Override
    public String themeImage(MultipartFile file, long shopID) {
        String extension = null;
        String originalFilename = file.getOriginalFilename().toLowerCase();
        if (originalFilename.endsWith(".jpg")) {
            extension = ".jpg";
        }
        if (originalFilename.endsWith(".png")) {
            extension = ".png";
        }
        if (originalFilename.endsWith(".jpeg")) {
            extension = ".jpeg";
        }
        if (extension == null) {
            throw new StorageException("Failed to store file " + originalFilename);
        }

        try {
            Path memberFolder = Diachi.resolve(this.themeImage.resolve(String.valueOf(shopID))) ;
            if (!Files.exists(memberFolder)) {
                Files.createDirectories(memberFolder);
            }

            String fileName = UUID.randomUUID().toString() + extension;

            Path newFilePath = memberFolder.resolve(fileName);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, newFilePath, StandardCopyOption.REPLACE_EXISTING);
            }

            String relativePath = Diachi + separator + this.themeImage + separator + shopID + separator + fileName;

            return relativePath;

        } catch (IOException e) {
            throw new StorageException("Failed to store file " + originalFilename, e);
        }
    }

    @Override
    public String storeShopImage(MultipartFile file, long shopID) {
        String extension = null;
        String originalFilename = file.getOriginalFilename().toLowerCase();
        if (originalFilename.endsWith(".jpg")) {
            extension = ".jpg";
        }
        if (originalFilename.endsWith(".png")) {
            extension = ".png";
        }
        if (originalFilename.endsWith(".jpeg")) {
            extension = ".jpeg";
        }
        if (extension == null) {
            throw new StorageException("Failed to store file " + originalFilename);
        }

        try {

            Path memberFolder = Diachi.resolve(this.rootLocation.resolve(String.valueOf(shopID)));
            if (!Files.exists(memberFolder)) {
                Files.createDirectories(memberFolder);
            }

            String fileName = UUID.randomUUID().toString() + extension;

            Path newFilePath = memberFolder.resolve(fileName);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, newFilePath, StandardCopyOption.REPLACE_EXISTING);
            }

            String relativePath = Diachi + separator + this.rootLocation + separator + shopID + separator + fileName;

            return relativePath;

        } catch (IOException e) {
            throw new StorageException("Failed to store file " + originalFilename, e);
        }
    }


    @Override
    public String storeShopImageByte(byte[] pdfFile, int shopid) {

        try {
            Path memberFolder = Diachi.resolve(this.rootLocation.resolve(String.valueOf(shopid)));
            if (!Files.exists(memberFolder)) {
                Files.createDirectories(memberFolder);
            }

            String fileName = UUID.randomUUID().toString() + ".jpg";
            Path newFilePath = memberFolder.resolve(fileName);


            try (FileOutputStream fos = new FileOutputStream(newFilePath.toString())) {
                fos.write(pdfFile);
                //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
            }
            String relativePath = Diachi + separator + this.rootLocation + separator + shopid + separator + fileName;

            return relativePath;

        } catch (IOException e) {
            throw new StorageException("Failed to store file ", e);
        }
    }


    @Override
    public String storeAvatar(MultipartFile file) {
        String extension = null;
        String originalFilename = file.getOriginalFilename().toLowerCase();
        if (originalFilename.endsWith(".jpg")) {
            extension = ".jpg";
        }
        if (originalFilename.endsWith(".png")) {
            extension = ".png";
        }
        if (originalFilename.endsWith(".jpeg")) {
            extension = ".jpeg";
        }
        if (extension == null) {
            throw new StorageException("Failed to store file " + originalFilename);
        }

        try {
            String fileName = UUID.randomUUID().toString() + extension;

            Path newFilePath = this.avatarLocation.resolve(fileName);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, newFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            String relativePath = Diachi + separator + this.avatarLocation + separator + fileName;

            return relativePath;

        } catch (IOException e) {
            throw new StorageException("Failed to store file " + originalFilename, e);
        }
    }

    @Override
    public String storeBase64Image(String base64String, int shopid) {
        try {
            Path memberFolder =Diachi.resolve(this.themeImage64.resolve(String.valueOf(shopid)));
            ;
            if (!Files.exists(memberFolder)) {
                Files.createDirectories(memberFolder);
            }

            String fileName = UUID.randomUUID().toString() + ".jpg";

            Path newFilePath = memberFolder.resolve(fileName);
            File outputFile = new File(newFilePath.toAbsolutePath().toString());

            String replaceString = base64String.replace("data:image/png;base64,", "");
            replaceString = replaceString.replace("data:image/jpeg;base64,", "");
            replaceString = replaceString.replace("data:image/jpg;base64,", "");

            byte[] decodedBytes = Base64.getDecoder().decode(replaceString);
            FileUtils.writeByteArrayToFile(outputFile, decodedBytes);

            String relativePath = Diachi + separator + this.themeImage64 + separator + shopid + separator + fileName;

            return relativePath;
        } catch (IOException e) {
            throw new StorageException("Failed to store file", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public String storeShopLogo(MultipartFile file) {
        String extension = null;
        String originalFilename = file.getOriginalFilename().toLowerCase();
        if (originalFilename.endsWith(".jpg")) {
            extension = ".jpg";
        }
        if (originalFilename.endsWith(".png")) {
            extension = ".png";
        }
        if (originalFilename.endsWith(".jpeg")) {
            extension = ".jpeg";
        }
        if (extension == null) {
            throw new StorageException("Failed to store file " + originalFilename);
        }

        try {
            Path memberFolder = Diachi.resolve(this.shopLogoLocation);
            if (!Files.exists(memberFolder)) {
                Files.createDirectories(memberFolder);
            }

            String fileName = UUID.randomUUID().toString() + extension;

            Path newFilePath = memberFolder.resolve(fileName);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, newFilePath, StandardCopyOption.REPLACE_EXISTING);
            }

            String relativePath = Diachi + separator + this.shopLogoLocation + separator + fileName;

            return relativePath;

        } catch (IOException e) {
            throw new StorageException("Failed to store file " + originalFilename, e);
        }
    }

    @Override
    public String storeShopeeImage(String url, long shopID) {

        try {
            Path memberFolder = Diachi.resolve(this.rootLocation.resolve(String.valueOf(shopID)));
            if (!Files.exists(memberFolder)) {
                Files.createDirectories(memberFolder);
            }

            String fileName = UUID.randomUUID().toString() + ".jpg";

            Path newFilePath = memberFolder.resolve(fileName);

            try (InputStream in = new URL(url).openStream()) {
                Files.copy(in, newFilePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String relativePath = Diachi + separator + this.rootLocation + separator + shopID + separator + fileName;

            return relativePath;

        } catch (IOException e) {
            throw new StorageException("Failed to store file " + url, e);
        }
    }
}
