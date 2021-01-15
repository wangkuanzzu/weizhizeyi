package com.example.filedemo.entity;

import org.springframework.web.multipart.MultipartFile;

/**
 * description: UploadFileParam <br>
 * date: 2021/1/14 上午10:20 <br>
 * author: kuan <br>
 * version: 1.0 <br>
 */
public class TransferFileParam {

    MultipartFile file;

    String file2Type;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getFile2Type() {
        return file2Type;
    }

    public void setFile2Type(String file2Type) {
        this.file2Type = file2Type;
    }
}
