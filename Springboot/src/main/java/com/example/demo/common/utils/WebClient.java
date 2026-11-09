package com.example.demo.common.utils;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WebClient {
    private int    status;
    
	@Value("${logStash.host}")
	private String logStashHost;
	
	@Value("${logStash.port}")
	private int logStashPort;
	
	
    /*
     * GET 통신
     */
    public String doGet(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        
        StringBuffer response = new StringBuffer();
        
        try {
            
            HttpGet httpRequest = new HttpGet(url);
        
           //get 요청
            CloseableHttpResponse httpResponse = httpClient.execute(httpRequest);
            
            log.info("::GET Response Status::");
            
            //response의 status 코드 출력
            log.info("status {}", httpResponse.getStatusLine().getStatusCode());
            this.status = httpResponse.getStatusLine().getStatusCode();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpResponse.getEntity().getContent()));
     
            String inputLine;
            
     
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            
            reader.close();
     
            //Print result
            log.info(response.toString());
            httpClient.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return response.toString();
    }
    
    /*
     * GET 통신
     */
    public String doGet(String url, Map<String,String>header) {
    	 
       
        /**
         CloseableHttpClient httpClient = HttpClients.createDefault();

    	if(url.contains("https://news.skhynix.co.kr")) {
        	httpClient = createHttpClient();
        	log.info("news.skhynix.co.kr 인증서 확인");
        } else if(url.indexOf("https") == 0) {
    	    httpClient = getTrustingHttpClient();
    	    log.info("https 모두 OK");
        } 
        **/
    	
    	CloseableHttpClient httpClient = url.indexOf("https") == 0 ? getTrustingHttpClient() : HttpClients.createDefault();
    	
        StringBuffer response = new StringBuffer();
        
        try {
        	
            HttpGet httpRequest = new HttpGet(url);
        
            // Header 설정 
            for(Map.Entry<String, String> entry : header.entrySet()) {
            	httpRequest.setHeader(entry.getKey(), entry.getValue());
            }
            
           //get 요청
            CloseableHttpResponse httpResponse = httpClient.execute(httpRequest);
            
            log.info("::GET Response Status::");
            
            //response의 status 코드 출력
            log.info("status {}", httpResponse.getStatusLine().getStatusCode());
            this.status = httpResponse.getStatusLine().getStatusCode();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpResponse.getEntity().getContent()));
     
            String inputLine;
            
     
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            
            reader.close();
     
            //Print result
            log.info(response.toString());
            httpClient.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return response.toString();
       
    }
    
    
    /*
     * POST 통신
     */
    public String doPost(String url, String params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        StringBuffer response          = new StringBuffer();
        
        try {
            
            HttpPost httpRequest = new HttpPost(url);
            StringEntity se      = new StringEntity(params);
            
            httpRequest.setEntity(se);
            httpRequest.setHeader("Accept"      , "application/json");
            httpRequest.setHeader("Content-type", "application/json");
            
            
           //post 요청
            CloseableHttpResponse httpResponse = httpClient.execute(httpRequest);
            
            log.info("::POST Response Status::");
            
            //response의 status 코드 출력
            log.info("status {}", httpResponse.getStatusLine().getStatusCode());
            this.status = httpResponse.getStatusLine().getStatusCode();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpResponse.getEntity().getContent()));
     
            String inputLine;
            
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            
            reader.close();
     
            //Print result
            httpClient.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return response.toString();
    }
    
    
    /*
     * POST 통신
     * url : 호출 URL 
     * queryParams : queryParam으로 전달할 데이터 
     * dataType : "FORM", "JSON" 
     * payload  : body나 FORM으로 전달할 값 
     * header   : Header 설정 값 
     */
    public String doPost(String url, String queryParams, String dataType, Map<String,Object> payload, Map<String,String>header) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        StringBuffer response          = new StringBuffer();
        
        try {
            
            HttpPost httpRequest      = new HttpPost(url+queryParams);
            
            if("FORM".equals(dataType)) {
            	List <NameValuePair> formValue = new ArrayList(); 
                 
            	for (Map.Entry<String, Object> entry : payload.entrySet()) {
                     
                     Object value = entry.getValue() != null ? entry.getValue() : "";
                     formValue.add(new BasicNameValuePair(entry.getKey(), (String)value));
                }
            	
            	StringEntity entity = new UrlEncodedFormEntity(formValue, StandardCharsets.UTF_8);
                httpRequest.setEntity(entity);
                
            } else if ("JSON".equals(dataType)) {
            	ObjectMapper mapper = new ObjectMapper();
	            String jsonStr = mapper.writeValueAsString(payload);
	            StringEntity entity = new StringEntity(jsonStr);
	            
            	httpRequest.setEntity(entity);
            } else if("XML".equals(dataType)) {
            	String reqBody = (String)payload.get("xml");
            	StringEntity entity  = new StringEntity(reqBody);
                  
            	httpRequest.setEntity(entity);
            }
            
            
            // Header 설정 
            for(Map.Entry<String, String> entry : header.entrySet()) {
            	httpRequest.setHeader(entry.getKey(), entry.getValue());
            }
            
           //post 요청
            CloseableHttpResponse httpResponse = httpClient.execute(httpRequest);
            
            log.info("::POST Response Status::");
            
            //response의 status 코드 출력
            log.info("status {}", httpResponse.getStatusLine().getStatusCode());
            this.status = httpResponse.getStatusLine().getStatusCode();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpResponse.getEntity().getContent()));
     
            String inputLine;
            
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            
            reader.close();
     
            httpClient.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return response.toString();
    }
    

    /*
     * POST 통신
     */
     public String doPost(String url, Map<String,String> header, String params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        StringBuffer response          = new StringBuffer();
        
        try {
            
            HttpPost httpRequest = new HttpPost(url);
            StringEntity se      = new StringEntity(params);
            
            httpRequest.setEntity(se);
            httpRequest.setHeader("Accept"      , "application/json");
            httpRequest.setHeader("Content-type", "application/json");
            
            if(header != null) {
                Iterator<String> keyList = header.keySet().iterator();
                while(keyList.hasNext()) {
                    String key = keyList.next();
                    httpRequest.setHeader(key, header.get(key));
                }
            }
            
           //post 요청
            CloseableHttpResponse httpResponse = httpClient.execute(httpRequest);
            
            this.status = httpResponse.getStatusLine().getStatusCode();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpResponse.getEntity().getContent()));
     
            String inputLine;
            
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            
            reader.close();
     
            httpClient.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return response.toString();
    }
    /*
     * PUT 통신
     */
    public String doPut(String url, String params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        StringBuffer response          = new StringBuffer();
        
        try {
            
            HttpPut httpRequest = new HttpPut(url);
            StringEntity se     = new StringEntity(params);
            
            httpRequest.setEntity(se);
            httpRequest.setHeader("Accept"      , "application/json");
            httpRequest.setHeader("Content-type", "application/json");
            
           //post 요청
            CloseableHttpResponse httpResponse = httpClient.execute(httpRequest);
            
            this.status = httpResponse.getStatusLine().getStatusCode();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpResponse.getEntity().getContent()));
     
            String inputLine;
            
     
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            
            reader.close();
     
            httpClient.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return response.toString();
    }
    
    /*
     * DELETE 통신
     */
    public String doDelete(String url, String params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        StringBuffer response          = new StringBuffer();
        
        try {
            
            HttpDelete httpRequest = new HttpDelete(url);
            StringEntity se        = new StringEntity(params);
            
           // httpRequest.setEntity(se);
            httpRequest.setHeader("Accept"      , "application/json");
            httpRequest.setHeader("Content-type", "application/json");
            
           //post 요청
            CloseableHttpResponse httpResponse = httpClient.execute(httpRequest);
            
            this.status = httpResponse.getStatusLine().getStatusCode();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpResponse.getEntity().getContent()));
     
            String inputLine;
            
     
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            
            reader.close();
     
            httpClient.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return response.toString();
    }    
    
    // 로그처리용 
    public void sendUdpStream(JsonObject logObj) {
    	 try (DatagramSocket socket = new DatagramSocket()) {
    		 
             InetAddress address = InetAddress.getByName(logStashHost);
             
             byte[] buffer = logObj.toString().getBytes(StandardCharsets.UTF_8);

             DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, logStashPort);
             socket.send(packet); // 데이터 전송

             log.info("sendStash::: done !!!!");
         } catch (Exception e) {
             log.error("opensearch log send error :::  {}", e.getMessage());
         }
    }
    
    public int getStatus() {
        return this.status;
    }
    
    // 모든 https 인증 OK
    private CloseableHttpClient getTrustingHttpClient() {
        try {
            SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(null, (chain, authType) -> true)
                    .build();
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
            return HttpClients.custom()
                    .setSSLSocketFactory(csf)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error creating trusting HTTP client", e);
        }
    }

    // news.skhynix.co.kr 인증서 
	private  CloseableHttpClient createHttpClient() {
    	StringBuffer certPem = new StringBuffer();
    	
//    	// openssl s_client -connect news.skhynix.co.kr:443 -servername news.skhynix.co.kr
//    	certPem.append("-----BEGIN CERTIFICATE-----\n");
//    	certPem.append("MIIFFjCCA/6gAwIBAgIUQkNTTl2H+infZiLpAAAAAVS1YOIwDQYJKoZIhvcNAQEL\n");
//    	certPem.append("BQAwgZExCzAJBgNVBAYTAktSMRQwEgYDVQQIDAtHeWVvbmdnaS1kbzEPMA0GA1UE\n");
//    	certPem.append("BwwGSWNoZW9uMREwDwYDVQQKDAhTSyBoeW5peDEUMBIGA1UECwwLSVQgU2VjdXJp\n");
//    	certPem.append("dHkxDjAMBgNVBAMMBWh5bml4MSIwIAYJKoZIhvcNAQkBFhNuZXR3b3JrQHNraHlu\n");
//    	certPem.append("aXguY29tMB4XDTI2MDIxNzAwMDAwMFoXDTI3MDMxODIzNTk1OVowHTEbMBkGA1UE\n");
//    	certPem.append("AxMSbmV3cy5za2h5bml4LmNvLmtyMIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIB\n");
//    	certPem.append("igKCAYEAnfsSkhTKsmO85RfEUxq3Nl74FDPq4gRrvP2hEa4gvaWFo1OiQRxr+qJ5\n");
//    	certPem.append("IiBNt4AjdGEsM/WWc6RzJIrLmhVFRRl8xycL4VLsnR1nmX73B2PC/JNjDgrFNtMN\n");
//    	certPem.append("pYug0diOe2ts97rT1Cv3hrV5EGrtYLkQxKkyM0Yet61G9zKWGBeGdkQVuEHX5wgv\n");
//    	certPem.append("Bj5KrQ9+PoLuAylGHHlMd8riDzkMrj/4Xz5ViY39U/LvrQI4R1K8gEQLpGlaBwuR\n");
//    	certPem.append("K43FHlCFWFcKrmJ+v4/jEvjRMwvlhENRGyK+Amhs1SpzZmmjDvgH4V3bTguGeZxW\n");
//    	certPem.append("k244ZQ6NAiEQiwY7akrHuRG1kkcseUAhIl3WaBcRG7yQWjQQihyjxNMZ+RDflUJX\n");
//    	certPem.append("dspHPLMWRfjRXFnkHwH0m909FAth2q9Io1VmyBSrzl3iEnnY4fp+HQTCpCYjvlt0\n");
//    	certPem.append("Z0BMFjXAy4PFDjPbbf72A9QWQ9T62KKke4p+a/7HsP8NnQyyZqfC/TqFbtB0m47w\n");
//    	certPem.append("RC7Gip37AgMBAAGjggFXMIIBUzAJBgNVHRMEAjAAMAsGA1UdDwQEAwIF4DAfBgNV\n");
//    	certPem.append("HSMEGDAWgBQub4ilnnkxmMJAF8/ysPuC0AwxwzAdBgNVHREEFjAUghJuZXdzLnNr\n");
//    	certPem.append("aHluaXguY28ua3IwEwYDVR0lBAwwCgYIKwYBBQUHAwEwZwYJYIZIAYb4QgENBFoW\n");
//    	certPem.append("WDgyNTE1RjUwMUUwMzM3RkE0Njg3MzJFMEQ3Q0MwNEE0RjM2ODg2Q0NGZWIgMTcg\n");
//    	certPem.append("MDA6MDA6MDAgMjAyNiBHTVRNYXIgMTggMjM6NTk6NTkgMjAyNyBHTVQwewYDVR0g\n");
//    	certPem.append("BHQwcjBwBgRVHSAAMGgwZgYIKwYBBQUHAgIwWhpYODI1MTVGNTAxRTAzMzdGQTQ2\n");
//    	certPem.append("ODczMkUwRDdDQzA0QTRGMzY4ODZDQ0ZlYiAxNyAwMDowMDowMCAyMDI2IEdNVE1h\n");
//    	certPem.append("ciAxOCAyMzo1OTo1OSAyMDI3IEdNVDANBgkqhkiG9w0BAQsFAAOCAQEAo5rpuaJM\n");
//    	certPem.append("1ljPg7dBhpcEdfEqznfjyn7O7wb3dxU1y21yF7gIUCjl88+n7aB/RDO0mD/0l9cH\n");
//    	certPem.append("Z+3XlEW0ihHXKce7yBjeS85oRiLthmSoxBvYUMaUi9qKfpU8FWMOMtjUiouek1Ly\n");
//    	certPem.append("8wxK29VB6nAh2bpLYxSUC0VADDi5lpsGfXucU9RyGraakT/CSWyZ7rQoHXjLRk0H\n");
//    	certPem.append("XbOQmQuryvPyqz44YjKA3bNqYX5LHuFeYypbNfxCa4VT9Fzitefq1Ju3d1634FOD\n");
//    	certPem.append("R+ukL3PEdRpcEmoEMgmARPUiMG5hgULzSzmZCY+JXOkvV3vijsUpnnAx4LW492PS\n");
//    	certPem.append("THWDueXyyfTuHA==\n");
//    	certPem.append("-----END CERTIFICATE-----\n");
    	

    	CloseableHttpClient httpClient = HttpClients.createDefault();
    	
   	    try {
   	      	    	
   		   InputStream inputStream = getClass().getClassLoader().getResourceAsStream("certs/news_server.pem");
   	       if(inputStream == null) {
   			   throw new IllegalArgumentException("파일을 찾을 수 없음");
   		   }
   	       String pemContent = new String(inputStream.readAllBytes());
   	       
   	       log.info("==================================================================");
   	       log.info(pemContent);
   	       log.info("-----------------------------------------------------------");
   	       log.info("{}", new ByteArrayInputStream(pemContent.getBytes(StandardCharsets.UTF_8)));
   	       
 	    	// 1. CertificateFactory를 이용해 PEM 문자열을 Certificate 객체로 변환
	        CertificateFactory cf = CertificateFactory.getInstance("X.509");
	       
	        Certificate cert = cf.generateCertificate(
	               //new ByteArrayInputStream(certPem.toString().getBytes(StandardCharsets.UTF_8))
	        	   new ByteArrayInputStream(pemContent.getBytes(StandardCharsets.UTF_8))
	        	           		
	        );
	       
	        // 2. 메모리 상에 임시 KeyStore 생성 및 인증서 등록
	        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        keyStore.load(null, null); // 빈 키스토어 초기화
	        keyStore.setCertificateEntry("embedded-ca-cert", cert);
	
	        // 3. SSLContext 생성 및 TrustStore 연결
	        SSLContext sslContext = SSLContextBuilder.create()
	                .loadTrustMaterial(keyStore, null)
	                .build();
	
	        // 4. HttpClient 생성 및 적용
//	        return HttpClients.custom()
//	                .setSSLContext(sslContext)
//	                .build();
	        HttpClientBuilder   httpBuilder = HttpClients.custom();
	    	httpBuilder.setSSLContext(sslContext);    
	    	httpClient = httpBuilder.build();
		       	    	
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	return httpClient;
    }
    	
    	private String getCdnData(String cdnUrl) {
	   Map <String, String> headMap = new HashMap <String, String>();
	    
       headMap.put("key", cdnToken);
      // String url = String.format("%s/%s", cdnUrl, seq); 
        CloseableHttpClient httpClient = cdnUrl.indexOf("https") == 0 ? webClient.getTrustingHttpClient() : HttpClients.createDefault();
    	
        String contentString = "";
        
        try {
        	
            HttpGet httpRequest = new HttpGet(cdnUrl);

            
            // Header 설정 
            httpRequest.setHeader("key", cdnToken);
            
            /*
            if(cdnUrl.indexOf("cdn.skhynix.com") > 0) {
            	httpRequest.setHeader("key", "RAbXc5t6KCl+/QFrLB2NZmWQijM569kci8r9YEDYdPw=");
            } else {
            	httpRequest.setHeader("key", cdnToken);
            }
            */
            
           //get 요청
            CloseableHttpResponse httpResponse = httpClient.execute(httpRequest);
            
            int  status = httpResponse.getStatusLine().getStatusCode();
            
            if(status == 200) {
            	HttpEntity entity = httpResponse.getEntity();
            	byte[] contents = EntityUtils.toByteArray(entity);
            	contentString = Base64.getEncoder().encodeToString(contents);
            }
            
            httpClient.close();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return contentString;
        
	}
}
