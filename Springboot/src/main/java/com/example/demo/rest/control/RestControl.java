package com.example.demo.rest.control;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.vo.Response;

@RestController
@RequestMapping("/api/demo") 
public class RestControl {

	@GetMapping
    public Map getDemo(@RequestBody Map params) {
		Map result = new HashMap();
		
		result.put("id", "1000");
        result.put("name", "killer");

        return result;
	}


	    public CompletableFuture<ResponseEntity<Response>> getMailList(@RequestBody SearchParams params) throws Exception {
    	Response <RawValue> response = new Response <RawValue>();
    	Locale locale = LocaleContextHolder.getLocale();
    	
    	return homeService.getMailList(params)
                .thenApply(result -> {
                    log.info("Controller: 결과 가공 및 반환 (Thread: {})", Thread.currentThread().getName());
                    response.setResult(Constants.SUCCESS_COMMON); 
	          	    response.setResultMessage(messageService.getMessage(Constants.SUCCESS_COMMON, locale));
	          	    response.setResponse(new RawValue(result));
	          	    
                    return ResponseEntity.ok(response);
         });
    }

	    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping(value = "/api/upload/files", consumes = {"multipart/form-data"})
    public ResponseEntity<List<FileUploadResponse>> uploadFiles(
        //  @RequestPart("data") ItemRequestDto requestDto,
    	//    @RequestPart("data") SearchParams params,
            @RequestPart("files") List<MultipartFile> files) {

        List<FileUploadResponse> responses = new ArrayList<>();

		 LocalDate date = LocalDate.now();
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
       
              String today = date.format("yyyyMMdd");
        
        
        File targetDir = new File(uploadDir, today);
        // 디렉토리 존재하지 않을 경우 생성
        if(!targetDir.exists()) {
        	targetDir.mkdir();
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            // 파일명 중복 방지를 위한 UUID 처리
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if(originalFilename != null && originalFilename.contains(".")) {
            	extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            
            String storeFilename = String.format("%s%s", UUID.randomUUID(), extension);
	
	        log.info("{} ===> {}", originalFilename, storeFilename);
	        
	        try {    
                // 물리 디렉토리에 파일 저장
            	/*
                Path savePath = Paths.get(uploadDir).resolve(storeFilename);
                file.transferTo(savePath.toFile());
				*/
            	File destFile = new File(targetDir, storeFilename);
            	file.transferTo(destFile);
            	
                // HTTP 접근 가능한 URL 생성
                String fileSubPath = String.format("%s/%s", today, storeFilename);
                
                log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                log.info(fileSubPath);
                log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                
                String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                		                                    .path("/files/")
                		                                    .path(fileSubPath)
                		                                    .toUriString();
                
                responses.add(new FileUploadResponse(originalFilename, fileUrl));

            } catch (IOException e) {
                throw new RuntimeException("파일 저장 실패: " + originalFilename, e);
            }
        }

        return ResponseEntity.ok(responses);
    }
}
