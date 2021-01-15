package com.example.filedemo.common;

import com.spire.pdf.FileFormat;

/**
 * description: FileTypeEnum <br>
 * date: 2021/1/14 下午2:31 <br>
 * author: kuan <br>
 * version: 1.0 <br>
 */
public enum  FileTypeEnum {
    //Spire.PDF for Java 还支持将 PDF 文档高质量地转换为 XPS、图片、Excel、SVG、Word、HTML 和 PDF/A 格式
    DOCX("001",".docx",FileFormat.DOCX),
    DOC("002",".doc",FileFormat.DOC),
    HTML("003",".html",FileFormat.HTML),
    XPS("004",".xps",FileFormat.XPS),
    XLSX("005",".xlsx",FileFormat.XLSX),
    PCL("006",".pcl",FileFormat.PCL),
    SVG("007",".svg",FileFormat.SVG),
    IMAGE_PNG("008", ".png",FileFormat.IMAGEFROMPS),
    IMAGE_BMP("009",".bmp",FileFormat.IMAGEFROMPS),
    IMAGE_JPEG("010",".jpeg",FileFormat.IMAGEFROMPS),
    IMAGE_EMF("011",".emf",FileFormat.IMAGEFROMPS),
    IMAGE_TIFF("012",".tiff",FileFormat.IMAGEFROMPS),
    PDF("013",".pdf",FileFormat.PDF);

    private String code;
    private String name;
    private FileFormat fileFormat;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileFormat getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(FileFormat fileFormat) {
        this.fileFormat = fileFormat;
    }

    FileTypeEnum(String code, String name, FileFormat fileFormat) {
        this.code = code;
        this.name = name;
        this.fileFormat = fileFormat;
    }

    public static FileTypeEnum getByTypeCode(String code){
        for (FileTypeEnum fileTypeEnum : FileTypeEnum.values()) {
            if (fileTypeEnum.getCode().equals(code)) {
                return fileTypeEnum;
            }
        }
        return null;
    }
}
