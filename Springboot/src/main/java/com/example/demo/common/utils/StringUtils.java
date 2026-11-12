package com.example.demo.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

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

	public static void  schedulePattern() throws Exception {
		//String xmlStr = "<?xml version=\"1.0\" encoding=\"utf-8\"?><ews><header><svc>email</svc><user>X0178960</user><eMailAddr>skhy.X0178960@partner.sk.com</eMailAddr><act>get-folder-list</act><ux>[1000/O/WEN027867D02]</ux></header><body><content><folder uid=\"AQMkADI2YzcwY2E2LWIyZTItNGVmMC05MzUyLTE0M2ExZDI3ZjRmMAAuAAADxUspu621z0arFPpY5a0AvgEAEJ/j6W2MRUm9T4Qwva2F7wAAAgEIAAAA\" parent_uid=\"ROOT\" totalCount=\"0\" unreadCount=\"0\" wellKnownFolderName=\"MsgFolderRoot\">최상위 정보 저장소</folder><folder uid=\"AQMkADI2YzcwY2E2LWIyZTItNGVmMC05MzUyLTE0M2ExZDI3ZjRmMAAuAAADxUspu621z0arFPpY5a0AvgEAEJ/j6W2MRUm9T4Qwva2F7wAAAgEMAAAA\" parent_uid=\"AQMkADI2YzcwY2E2LWIyZTItNGVmMC05MzUyLTE0M2ExZDI3ZjRmMAAuAAADxUspu621z0arFPpY5a0AvgEAEJ/j6W2MRUm9T4Qwva2F7wAAAgEIAAAA\" totalCount=\"36\" unreadCount=\"1\" wellKnownFolderName=\"Inbox\">받은 편지함</folder><folder uid=\"AQMkADI2YzcwY2E2LWIyZTItNGVmMC05MzUyLTE0M2ExZDI3ZjRmMAAuAAADxUspu621z0arFPpY5a0AvgEAEJ/j6W2MRUm9T4Qwva2F7wAAAgEJAAAA\" parent_uid=\"AQMkADI2YzcwY2E2LWIyZTItNGVmMC05MzUyLTE0M2ExZDI3ZjRmMAAuAAADxUspu621z0arFPpY5a0AvgEAEJ/j6W2MRUm9T4Qwva2F7wAAAgEIAAAA\" totalCount=\"2\" unreadCount=\"0\" wellKnownFolderName=\"SentItems\">보낸 편지함</folder><folder uid=\"AQMkADI2YzcwY2E2LWIyZTItNGVmMC05MzUyLTE0M2ExZDI3ZjRmMAAuAAADxUspu621z0arFPpY5a0AvgEAEJ/j6W2MRUm9T4Qwva2F7wAAAgELAAAA\" parent_uid=\"AQMkADI2YzcwY2E2LWIyZTItNGVmMC05MzUyLTE0M2ExZDI3ZjRmMAAuAAADxUspu621z0arFPpY5a0AvgEAEJ/j6W2MRUm9T4Qwva2F7wAAAgEIAAAA\" totalCount=\"0\" unreadCount=\"0\" wellKnownFolderName=\"Outbox\">보낼 편지함</folder><folder uid=\"AQMkADI2YzcwY2E2LWIyZTItNGVmMC05MzUyLTE0M2ExZDI3ZjRmMAAuAAADxUspu621z0arFPpY5a0AvgEAEJ/j6W2MRUm9T4Qwva2F7wAAAgEPAAAA\" parent_uid=\"AQMkADI2YzcwY2E2LWIyZTItNGVmMC05MzUyLTE0M2ExZDI3ZjRmMAAuAAADxUspu621z0arFPpY5a0AvgEAEJ/j6W2MRUm9T4Qwva2F7wAAAgEIAAAA\" totalCount=\"1\" unreadCount=\"0\" wellKnownFolderName=\"Drafts\">임시 보관함</folder><folder uid=\"AQMkADI2YzcwY2E2LWIyZTItNGVmMC05MzUyLTE0M2ExZDI3ZjRmMAAuAAADxUspu621z0arFPpY5a0AvgEAEJ/j6W2MRUm9T4Qwva2F7wAAAgEiAAAA\" parent_uid=\"AQMkADI2YzcwY2E2LWIyZTItNGVmMC05MzUyLTE0M2ExZDI3ZjRmMAAuAAADxUspu621z0arFPpY5a0AvgEAEJ/j6W2MRUm9T4Qwva2F7wAAAgEIAAAA\" totalCount=\"0\" unreadCount=\"0\" wellKnownFolderName=\"JunkEmail\">정크 메일</folder><folder uid=\"AQMkADI2YzcwY2E2LWIyZTItNGVmMC05MzUyLTE0M2ExZDI3ZjRmMAAuAAADxUspu621z0arFPpY5a0AvgEAEJ/j6W2MRUm9T4Qwva2F7wAAAgEKAAAA\" parent_uid=\"AQMkADI2YzcwY2E2LWIyZTItNGVmMC05MzUyLTE0M2ExZDI3ZjRmMAAuAAADxUspu621z0arFPpY5a0AvgEAEJ/j6W2MRUm9T4Qwva2F7wAAAgEIAAAA\" totalCount=\"0\" unreadCount=\"0\" wellKnownFolderName=\"DeletedItems\">지운 편지함</folder></content><result><stat>OK</stat><msg/></result></body></ews>";
	    String xmlStr = "<?xml version=\"1.0\" encoding=\"utf-8\"?><ews><header><svc>calendar</svc><user>X0178960</user><eMailAddr>skhy.X0178960@partner.sk.com</eMailAddr><act>listrest</act><ux>[1000/O/THN01BA43D15]</ux></header><body><content><calendar><subject>근무교대표 cache 처리 수정</subject><uid>AAMkADI2YzcwY2E2LWIyZTItNGVmMC05MzUyLTE0M2ExZDI3ZjRmMABGAAAAAADFSym7rbXPRqsU+ljlrQC+BwAQn+PpbYxFSb1PhDC9rYXvAAAAAAENAAAQn+PpbYxFSb1PhDC9rYXvAABZW1AwAAA=</uid><startTime>2026-10-07 00:00:00</startTime><endTime>2026-10-08 00:00:00</endTime><itemType>Single</itemType><occurrenceIndex></occurrenceIndex><importance>Normal</importance><isAllDayEvent>True</isAllDayEvent><categories><category uid=\"9fd934b8-114e-4795-8e5d-300cbbc1400d\" name=\"주황색 범주\" color=\"Orange\" /></categories></calendar><calendar><subject>일정 확인</subject><uid>AAMkADI2YzcwY2E2LWIyZTItNGVmMC05MzUyLTE0M2ExZDI3ZjRmMABGAAAAAADFSym7rbXPRqsU+ljlrQC+BwAQn+PpbYxFSb1PhDC9rYXvAAAAAAENAAAQn+PpbYxFSb1PhDC9rYXvAABdXf7XAAA=</uid><startTime>2026-10-08 17:30:00</startTime><endTime>2026-10-08 18:30:00</endTime><itemType>Single</itemType><occurrenceIndex></occurrenceIndex><importance>Normal</importance><isAllDayEvent>False</isAllDayEvent><categories><category uid=\"4daf67be-ddd6-477e-999e-686b01e39c02\" name=\"없음\" color=\"None\" /></categories></calendar><calendar><subject>한글날 일정</subject><uid>AAMkADI2YzcwY2E2LWIyZTItNGVmMC05MzUyLTE0M2ExZDI3ZjRmMABGAAAAAADFSym7rbXPRqsU+ljlrQC+BwAQn+PpbYxFSb1PhDC9rYXvAAAAAAENAAAQn+PpbYxFSb1PhDC9rYXvAABdXf7YAAA=</uid><startTime>2026-10-09 17:30:00</startTime><endTime>2026-10-09 18:30:00</endTime><itemType>Single</itemType><occurrenceIndex></occurrenceIndex><importance>Normal</importance><isAllDayEvent>False</isAllDayEvent><categories><category uid=\"57122a41-20a1-47c3-9286-c993f0aea938\" name=\"없음\" color=\"None\" /></categories></calendar></content><result><stat>OK</stat><msg/></result></body></ews>"; 
		XmlParser xmlParser = new XmlParser();
		InputSource is = new InputSource(new StringReader(xmlStr));
		
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder Builder = factory.newDocumentBuilder();
		//Document document = Builder.parse(fileURL);
		Document document = Builder.parse(is);
		
		Element root = document.getDocumentElement(); //문서의 요소인 자식 노드에 직접 접근할 수 있도록 해주는 편의 속성입니다
		NodeList nodeList = root.getElementsByTagName("calendar");
		
		System.out.println("node list ::: " + nodeList.getLength());
		//NodeList nodeList = FormConverter.parser(is, "eMail");
	    	
	        long unreadCnt = 0;
	        
	        List <Map <String, String>> list = new ArrayList <Map<String,String>>();
	        Map  <String,Object>resultMap    = new HashMap<String,Object>();
	        	
	        for(int i = 0; i<nodeList.getLength(); i++) {
				Node recordNode = nodeList.item(i);
				 if (recordNode.getNodeType() == Node.ELEMENT_NODE) {
					 
					 Element recordElement = (Element) recordNode;
					 Map<String, String> map = new HashMap<String, String>();
					 
					 String subject       = (recordElement.getElementsByTagName("subject")).item(0).getTextContent(); //FormConverter.getElementTextByTagName(recordElement, "subject");
					 String startTime     = (recordElement.getElementsByTagName("startTime")).item(0).getTextContent(); //FormConverter.getElementTextByTagName(recordElement, "startTime");
					 String endTime       = (recordElement.getElementsByTagName("endTime")).item(0).getTextContent(); //FormConverter.getElementTextByTagName(recordElement, "endTime");
	                 String uid           = (recordElement.getElementsByTagName("uid")).item(0).getTextContent(); //FormConverter.getElementTextByTagName(recordElement, "uid");
	                 String importance    = (recordElement.getElementsByTagName("importance")).item(0).getTextContent(); //FormConverter.getElementTextByTagName(recordElement, "importance");
	                 String isAllDayEvent = (recordElement.getElementsByTagName("isAllDayEvent")).item(0).getTextContent(); //FormConverter.getElementTextByTagName(recordElement, "isAllDayEvent");
	                 NodeList  categories    = (NodeList)recordElement.getElementsByTagName("categories").item(0); //FormConverter.getElementTextByTagName(recordElement, "category");
	                 
	             	//NodeList propList   = (NodeList)recordElement.getElementsByTagName("properties").item(0);
	             	
	                 System.out.println("[" + subject + "][" + startTime + "][" + endTime + "]");
	                 
	                 if(categories.getLength() > 0) {
	                	 System.out.println("categories tags ");
	                	 for(int j=0; j < categories.getLength(); j++) {
	 						Element el = (Element)categories.item(j);
	 						System.out.println("uid : " + el.getAttribute("uid"));
	 						System.out.println("name: " + el.getAttribute("name"));
	 						System.out.println("color: " + el.getAttribute("color"));
	 					}
	                 } else {
	                	 System.out.println("categories is empty");
	                 }
	                 
	                 map.put("subject"      , subject);
	                 map.put("startTime"    , startTime);
	                 map.put("endTime"      , endTime);
		             map.put("uid"          , uid);
	                 map.put("importance"   , importance);
	                 map.put("isAllDayEvent", isAllDayEvent);
	                // map.put("category"     , category);
	                 
	                 
				 }
				 
			}
	        
	}


	
}
