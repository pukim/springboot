package com.example.demo.common.vo;

public class FileUploadResponse {
	private String originalFile;
	private String fileUrl;
	
	FileUploadResponse(String originalFile, String fileUrl) {
		this.originalFile = originalFile;
		this.fileUrl  = fileUrl;
	}
}
