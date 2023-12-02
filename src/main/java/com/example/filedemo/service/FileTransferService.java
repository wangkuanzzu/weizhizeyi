package com.example.filedemo.service;

import com.example.filedemo.common.enumm.FileTypeEnum;
import com.example.filedemo.exception.FileStorageException;
import com.example.filedemo.exception.MyFileNotFoundException;
import com.example.filedemo.property.FileStorageProperties;

import com.spire.pdf.PdfDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileTransferService {

    private final Path fileStorageLocation;

    private final Path fileTransferLocation;

    @Autowired
    public FileTransferService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        this.fileTransferLocation = Paths.get(fileStorageProperties.getTransferDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
            Files.createDirectories(this.fileTransferLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String transferFile(MultipartFile file, String fileType) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            if(!fileName.contains(".pdf")){
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            FileTypeEnum fileTypeEnum = FileTypeEnum.getByTypeCode(fileType);
            if(fileTypeEnum == null){
                throw new FileStorageException("Sorry! FileType contains invalid path sequence " + fileType);
            }
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // 读取pdf文件
            PdfDocument pdf = new PdfDocument();
            pdf.loadFromFile(targetLocation.toString());
            //去除水印
//            PdfPageBase ppb = pdf.getPages().insert(0);
//            pdf.getPages().removeAt(0);
//            pdf.saveToFile(this.fileStorageLocation.resolve("997.pdf").toString());

            //pdf转换成目标格式文件
            String transferName = fileName.replace(FileTypeEnum.PDF.getName(),fileTypeEnum.getName());
            pdf.saveToFile(this.fileTransferLocation.resolve(transferName).toString(), fileTypeEnum.getFileFormat());
            pdf.close();

//            Document document = new Document();
////            document.loadFromFile(this.fileTransferLocation.resolve(transferName).toString(), FileFormat.Docx);
////            document.replace("Evaluation Warning : The document was created with Spire.PDF for Java.","",false,true);
////            document.saveToFile(this.fileTransferLocation.resolve("t.docx").toString());
////            document.close();


            return transferName;

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileTransferLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

}
