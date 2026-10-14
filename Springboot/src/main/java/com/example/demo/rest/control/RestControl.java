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
        		 
}
