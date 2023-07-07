package com.ecommerce.library.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class ImageUpload {
    private static final String UPLOAD_FOLDER = "F:\\Khoa-TonyRay\\learning\\projects\\ecommerce\\image-product";

    public static String handleFileUpload(MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                // Specify the directory where you want to save the uploaded file
                String uploadDir = UPLOAD_FOLDER;

                // Create the directory if it doesn't exist
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Generate a unique file name to prevent conflicts
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                // Create the file path
                String filePath = uploadDir + File.separator + fileName;

                // Save the file to the specified directory
                file.transferTo(new File(filePath));

                // Return a response or redirect to another page
                System.out.println("Successfully" + filePath);
                return fileName;

            } catch (IOException e) {
                // Handle file save failure
                e.printStackTrace();
                // Return an error response or redirect to an error page
                System.out.println("Fail");
                return "index";
            }
        } else {
            // Handle empty file upload
            // Return an error response or redirect to an error page
            System.out.println("Fail");
            return "index";
        }
    }

    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    public static boolean uploadImage(String uploadDir, MultipartFile image){
        boolean isUpload = false;
        try {
            Files.copy(image.getInputStream(),
                    Paths.get(uploadDir + File.separator, image.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
//            StringBuilder fileNames = new StringBuilder();
//            Path fileNameAndPath = Paths.get(UPLOAD_FOLDER, image.getOriginalFilename());
//            fileNames.append(image.getOriginalFilename());
//            Files.write(fileNameAndPath, image.getBytes());
            isUpload = true;

        }catch (Exception e){
            e.printStackTrace();
        }
        return isUpload;
    }
}
