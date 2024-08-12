package com.blog.app.services.impl;

import com.blog.app.services.FileService;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

//public class FileServiceImpl implements FileService {
//    @Override
//    public String uploadImage(String path, MultipartFile file) throws IOException {
//
//        //fetching file name
//        String name=file.getOriginalFilename();
//
//        String randomID= UUID.randomUUID().toString();
//        String fileName=randomID.concat(name.substring(name.lastIndexOf(".")));
//
//        String filePath=path+ File.separator+fileName;
//
//        File file1=new File(path);
//
//        if(!file1.exists())
//            file1.mkdir();
//
//        Files.copy(file.getInputStream(), Paths.get(filePath));
//
//        return name;
//    }
//
//    @Override
//    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
//        return null;
//    }
//}
