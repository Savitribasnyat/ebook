package com.bookrentalsystem.bks.utility;

import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

@Component
public class Fileutils {

    /*
     * Save the multipart file in new folder by creating the folder in desktop
     * And return the file path
     */
    public String saveMultipartFile(MultipartFile multipartFile) throws IOException {
        String dirPath = System.getProperty("user.home") + "/Desktop/Book_Rental_System";
        File directoryFile = new File(dirPath);
        if (!directoryFile.exists()) {
            directoryFile.mkdirs();
        }
        String originalFileName = multipartFile.getOriginalFilename();
        String filePath = dirPath + "/" + originalFileName;
        File myFile = new File(filePath);

        try {
            multipartFile.transferTo(myFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return filePath;
    }

    //this is used to extract the file type to check the file type
    public String photoValidation(MultipartFile multipartFile) {
        Tika tika = new Tika();
        return tika.detect(multipartFile.getOriginalFilename());
    }


    //convert image  into base64
    public String getBase64FormFilePath(String filePath) throws IOException {
        File file = new File(filePath);
        if(file.exists()){
            byte[] bytes = Files.readAllBytes(file.toPath());

            String base64Code = Base64.getEncoder().encodeToString(bytes);

            return "data:image/jpeg;base64,"+ base64Code;

        }else {
            return  null;
        }
    }



}
