package com.hynix.hcp.mportal.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hynix.hcp.mportal.user.service.UserService;
import com.hynix.hcp.mportal.utils.WebClient;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private WebClient  webClient;
	
	@Override 
	public String sample() {
		
		//return "Hello Spring boot world";
		return webClient.doGet("http://www.daum.net");
	}
}
