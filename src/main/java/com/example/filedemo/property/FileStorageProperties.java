package com.example.filedemo.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;

    private String uploadDirPdf2Word;

    public String getUploadDirPdf2Word() {
        return uploadDirPdf2Word;
    }

    public void setUploadDirPdf2Word(String uploadDirPdf2Word) {
        this.uploadDirPdf2Word = uploadDirPdf2Word;
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
