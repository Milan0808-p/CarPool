package com.example.demo.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

//    public String uploadFile(MultipartFile file, String folderName, String fileName) {
//        try {
//            Map uploadResult = cloudinary.uploader().upload(
//                    file.getInputStream(),
//                    ObjectUtils.asMap(
//                            "folder", folderName,
//                            "public_id", fileName
//                    )
//            );
//
//            return uploadResult.get("secure_url").toString();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Failed to upload image: " + e.getMessage());
//        }
//    }




    //Optimized


    public String uploadFile(MultipartFile file, String folderName, String fileName) {
        File tempFile = null;
        try {
            // Convert MultipartFile to File
            tempFile = File.createTempFile("upload-", file.getOriginalFilename());
            file.transferTo(tempFile);

            Map uploadResult = cloudinary.uploader().upload(
                    tempFile,
                    ObjectUtils.asMap(
                            "folder", folderName,
                            "public_id", fileName,
                            "resource_type", "auto",
                            "transformation", "q_auto,f_auto"
                    )
            );

            return uploadResult.get("secure_url").toString();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload image: " + e.getMessage());
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
}
