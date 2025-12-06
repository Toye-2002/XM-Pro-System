package com.example.springboot.controller;


import cn.hutool.core.io.FileUtil;
import com.example.springboot.common.Result;
import com.example.springboot.exception.CustomException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 文件相关接口
@RestController
@RequestMapping("/files")
public class FileController {
    // 文件上传的目录的路径
    private static final String filePath = System.getProperty("user.dir") + "/files/";

    // 文件上传接口
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {  // 文件流的形式接收前端发过来的文件
        String originalFilename = file.getOriginalFilename();  // xxx.png
        if (!FileUtil.isDirectory(filePath)) {
           FileUtil.mkdir(filePath);
        }
        // 提供文件存储的完整路径
        // 给文件名 加一个唯一的标识（时间戳）
        String fileName = System.currentTimeMillis() + "_" + originalFilename;
        String realPath = filePath + fileName;  // 完整的文件路径
        try {
            FileUtil.writeBytes(file.getBytes(),realPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("500","文件上传失败");
        }
        // 返回一个网络路径
        String url = "http://localhost:9090/files/download/" + fileName;
        return Result.success(url);
    }

    // 文件下载
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) {
        try {
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            response.setContentType("application/octet-stream");
            OutputStream os = response.getOutputStream();
            String realPath = filePath + fileName;  // 完整的文件路径
            // 获得文件的字节数组
            byte[] bytes = FileUtil.readBytes(realPath);
            os.write(bytes);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("500","文件下载失败");
        }
    }

    // wangEditor编辑器文件上传接口
    @PostMapping("/wang/upload")
    public Map<String,Object> wangEditorUpload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();  // xxx.png
        if (!FileUtil.isDirectory(filePath)) {
            FileUtil.mkdir(filePath);
        }
        // 提供文件存储的完整路径
        // 给文件名 加一个唯一的标识（时间戳）
        String fileName = System.currentTimeMillis() + "_" + originalFilename;
        String realPath = filePath + fileName;  // 完整的文件路径
        try {
            FileUtil.writeBytes(file.getBytes(),realPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("500","文件上传失败");
        }
        // 返回一个网络路径
        String url = "http://localhost:9090/files/download/" + fileName;
        // wangEditor上传图片成功后，需要返回的参数
        Map<String,Object> resMap = new HashMap<>();
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> urlMap = new HashMap<>();
        urlMap.put("url",url);
        list.add(urlMap);
        resMap.put("errno",0);
        resMap.put("data",list);
        return resMap;
    }

}
