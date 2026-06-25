package com.hynix.hcp.mportal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MportalApplicationTests {

	@Test
	void contextLoads() {
	}

}

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class SimpleHttpListener {

    public static void main(String[] args) throws IOException {
        // 1. 8080 포트로 HTTP 서버 생성 (백로그 큐 크기는 0으로 설정하여 시스템 기본값 사용)
        HttpServer server = HttpServer.create(new InetSocketAddress(9988), 0);

        // 2. 특정 경로(/api/data)로 들어오는 요청을 처리할 핸들러(리스너) 매핑
        server.createContext("/api/data", new MyHttpHandler());

        // 3. 기본 Executor 설정 (null 설정 시 기본 싱글 스레드로 동작, 필요시 쓰레드풀 지정 가능)
        server.setExecutor(null); 

        // 4. 서버 시작
        System.out.println("🚀 HTTP 리스너가 8080 포트에서 시작되었습니다...");
        server.start();
    }

    // 요청을 처리하는 핸들러 클래스
    static class MyHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // 요청 메서드 확인 (GET, POST 등)
            String method = exchange.getRequestMethod();
            System.out.println("📥 요청 수신! Method: " + method);

            // POST 요청일 경우 바디(Body) 데이터 읽기
            if ("POST".equalsIgnoreCase(method)) {
                try (InputStream is = exchange.getRequestBody()) {
                    String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    System.out.println("📄 수신된 Body 내용:\n" + body);
                }
            }

            // 응답 데이터 작성
            String response = "{\"status\": \"success\", \"message\": \"Data received sucessfully!\"}";
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

            // 헤더 설정 (Content-Type을 JSON으로 설정)
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
            
            // 응답 상태 코드(200 OK)와 본문 길이 설정
            exchange.sendResponseHeaders(200, responseBytes.length);

            // 실제 데이터 전송
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
            
            System.out.println("📤 응답 전송 완료!\n--------------------------------");
        }
    }
}


{"mandatoryParam":{"appId":"A000SHY029","appVer":"1.9.1","appResourceVer":"string","rule":"SKHY_NEW_NOTICE_BOARD_LIST","apMac":"string","deviceModel":"SM-G991N","companyUid":"SK.SKHY","osName":"Android","osVersion":"35","mdn":"01027451234","deviceNo":"e29bc1c4-7278-3e79-aa96-19b312b53aa2","encPw":"kgeNM+HqgJhcPLgHlb0U/Q==","authKey":"72fe9b3163b619ef4a6b3ab372a71ad82ebf2ed3","profileNo":"PNA8A6C35D0000019E66A826B6D4779C","signkey":"c3Z15SDkb39HPMuMCar6/YP5PazDTsBRLbjrv2PFnWM=","lang":"ko","groupCd":"SK"},"requestParams":{"serviceType":"1","pageNumber":"1","searchType":"0","itemsPerPage":"5","keyword":"string"}}

{"mandatoryParam":{"appId":"A000SHY029","appVer":"1.9.1","appResourceVer":"string","rule":"SKHY_NEW_NOTICE_BOARD_LIST","apMac":"string","deviceModel":"SM-G991N","companyUid":"SK.SKHY","osName":"Android","osVersion":"35","mdn":"01027451234","deviceNo":"e29bc1c4-7278-3e79-aa96-19b312b53aa2","encPw":"kgeNM+HqgJhcPLgHlb0U/Q==","authKey":"72fe9b3163b619ef4a6b3ab372a71ad82ebf2ed3","profileNo":"PNA8A6C35D0000019E66A826B6D4779C","signkey":"c3Z15SDkb39HPMuMCar6/YP5PazDTsBRLbjrv2PFnWM=","lang":"ko","groupCd":"SK"},"requestParams":{"serviceType":"1","pageNumber":"1","searchType":"0","itemsPerPage":"5","keyword":"string"}}





