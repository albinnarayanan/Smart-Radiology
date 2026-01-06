package com.smartradiology.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        //file names of current/ original file
        String originalFileName = file.getOriginalFilename();
        //generating a unique file name
        String randomId = UUID.randomUUID().toString();
        //org = mat.jpg , randId = 123 -> 123.jpg
        String fileName = randomId.concat(originalFileName
                .substring(originalFileName.lastIndexOf('.')));
        //using pathSeperator
        String filePath = path + File.separator + fileName;
        //check if path exists
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }
        //upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));

        return fileName;

    }



}
