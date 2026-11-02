package com.example.demo.rest.control;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        		 
}
