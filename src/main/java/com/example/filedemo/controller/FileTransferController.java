package com.example.filedemo.controller;

import com.example.filedemo.common.FileTypeEnum;
import com.example.filedemo.payload.TransferFileResponse;
import com.example.filedemo.service.FileTransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * description: FileTransferController <br>
 * date: 2021/1/14 上午10:27 <br>
 * author: kuan <br>
 * version: 1.0 <br>
 */
@RestController
public class FileTransferController {

    private static final Logger logger = LoggerFactory.getLogger(FileTransferController.class);

    @Autowired
    private FileTransferService fileTransferService;

    @PostMapping("/file/transfer")
    public TransferFileResponse transferFile(@RequestParam("file") MultipartFile file, @RequestParam("file2Type") String file2Type){
//        logger.info("文件转换参数:{}",transferFileParam);
//        MultipartFile file = transferFileParam.getFile();
//        String fileType = transferFileParam.getFile2Type();
        String fileName = fileTransferService.transferFile(file,file2Type);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/file/download/")
                .path(fileName)
                .toUriString();

        return new TransferFileResponse(fileName,fileDownloadUri,file.getContentType(),file.getSize());
    }

    @GetMapping("/file/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileTransferService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @RequestMapping(value = "/file/type/list")
    public List<Map<String, String>> transferFile(){
        List<Map<String, String>> list = new ArrayList<>();
        for (FileTypeEnum fileType : FileTypeEnum.values()) {
            Map<String, String> map = new HashMap<>();
            map.put(fileType.getCode(),fileType.getName());
            list.add(map);
        }
        return list;
    }

}
