package com.example.demo.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class StringUtils {
 
  public  String encryptIflow(String orgStr) throws InvalidAlgorithmParameterException { 
		String secretKey = "1Xtu44Z1WnF6MxXq8xH5nyqYHXG2T8eH"; //properties.getProperty("skynet.iflowAESKey"); 
	                       // ZUWDvnrOObckGBkMRVINEhQIlSkBMhfC 
		SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "AES"); 
		String iv = secretKey.substring(0, 16); 
		Cipher cipher; 
		String message = ""; 
		try { 
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); 
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(iv.getBytes())); 
			byte[] encrypted = cipher.doFinal(orgStr.getBytes()); 
			message = Base64.encodeBase64String(encrypted); 
		} catch (Exception e) { 
			log.error("FailEncrypt" + e.getMessage()); 
			message = "FALSE"; //Constants.FALSE; 
		} 
	 
		return message; 
	} 
	 
  
	public String decryptIflow(String decStr) throws UnsupportedEncodingException, InvalidAlgorithmParameterException { 
		String secretKey = "1Xtu44Z1WnF6MxXq8xH5nyqYHXG2T8eH";    // properties.getProperty("skynet.iflowAESKey"); 
		String iv = secretKey.substring(0, 16); 
	 
		SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "AES"); 
		Cipher cipher; 
		String message = ""; 
		try { 
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); 
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv.getBytes())); 
	 
			byte[] byteStr = Base64.decodeBase64(decStr.getBytes()); 
			message = new String(cipher.doFinal(byteStr), "UTF-8"); 
	 
		} catch (Exception e) { 
			log.error("FailDecrypt" + e.getMessage()); 
			message = "FALSE"; //Constants.FALSE; 
		} 
	 
		return message; 
	}
}
