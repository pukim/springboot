package com.hynix.hcp.mportal.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

@Component
@Slf4j
public class WebClient {
   
    private int    status;
    
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
            
            log.debug("::GET Response Status::");
            
            //response의 status 코드 출력
            log.debug("status {}", httpResponse.getStatusLine().getStatusCode());
            this.status = httpResponse.getStatusLine().getStatusCode();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpResponse.getEntity().getContent()));
     
            String inputLine;
            
     
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            
            reader.close();
     
            //Print result
            log.debug(response.toString());
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
            
            log.debug("::POST Response Status::");
            
            //response의 status 코드 출력
            log.debug("status {}", httpResponse.getStatusLine().getStatusCode());
            this.status = httpResponse.getStatusLine().getStatusCode();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpResponse.getEntity().getContent()));
     
            String inputLine;
            
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            
            reader.close();
     
            //Print result
            log.debug(response.toString());
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
            
            log.debug("::POST Response Status::");
            
            //response의 status 코드 출력
            log.debug("status {}" , httpResponse.getStatusLine().getStatusCode());
            this.status = httpResponse.getStatusLine().getStatusCode();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpResponse.getEntity().getContent()));
     
            String inputLine;
            
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            
            reader.close();
     
            //Print result
            log.debug(response.toString());
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
            
            log.debug("::PUT Response Status::");
            
            //response의 status 코드 출력
            log.debug("status {} ", httpResponse.getStatusLine().getStatusCode());
            this.status = httpResponse.getStatusLine().getStatusCode();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpResponse.getEntity().getContent()));
     
            String inputLine;
            
     
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            
            reader.close();
     
            //Print result
            log.debug(response.toString());
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
            
            log.debug("::PUT Response Status::");
            
            //response의 status 코드 출력
            log.debug("status {} ", httpResponse.getStatusLine().getStatusCode());
            this.status = httpResponse.getStatusLine().getStatusCode();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpResponse.getEntity().getContent()));
     
            String inputLine;
            
     
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            
            reader.close();
     
            //Print result
            log.debug(response.toString());
            httpClient.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return response.toString();
    }    
    
    public int getStatus() {
        return this.status;
    }
}