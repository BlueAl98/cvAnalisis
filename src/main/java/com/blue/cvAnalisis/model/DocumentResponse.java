package com.blue.cvAnalisis.model;

public class DocumentResponse {

    private String fileName;
    private String extractedText;
    private long fileSize;

    public DocumentResponse(String fileName, String extractedText, long fileSize) {
        this.fileName = fileName;
        this.extractedText = extractedText;
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExtractedText() {
        return extractedText;
    }

    public void setExtractedText(String extractedText) {
        this.extractedText = extractedText;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return "DocumentResponse{" +
                "fileName='" + fileName + '\'' +
                ", extractedText='" + extractedText + '\'' +
                ", fileSize=" + fileSize +
                '}';
    }
}
