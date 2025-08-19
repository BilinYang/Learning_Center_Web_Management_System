package com.bilin.controller;

import com.bilin.entity.AliyunOSSProperties;
import com.bilin.entity.Result;
import com.bilin.utils.AliyunOSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;


//@Slf4j
//@RestController
//public class UploadController {
//    @PostMapping("/upload")
//    public Result uplaod(String username, Integer age, MultipartFile file) throws Exception {
//        log.info("Upload file parameters: {}, {}, {}", username, age, file);
//        String originalFilename = file.getOriginalFilename();
//        String newFileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
//        file.transferTo(new File("/Users/Ivy/Desktop/Shenlan/" + newFileName));
//        return Result.success();
//    }
//}


@Slf4j
@RestController
public class UploadController {

//    @Value("${aliyun.oss.bucket}")
//    private String bucketName;
//    @Value("${aliyun.oss.endpoint}")
//    private String endPoint;

    @Autowired
    private AliyunOSSProperties aliyunOSSProperties;

    @PostMapping("/upload")
    public Result uplaod(MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        log.info("Original file name: {}", originalFilename);
        String extName = file.getOriginalFilename().substring(originalFilename.lastIndexOf("."));
        String url = AliyunOSSUtils.upload(aliyunOSSProperties.getEndpoint(), aliyunOSSProperties.getBucket(), file.getBytes(), extName);
        return Result.success(url);
    }
}
