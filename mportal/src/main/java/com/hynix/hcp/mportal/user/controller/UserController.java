package com.hynix.hcp.mportal.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hynix.hcp.mportal.user.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/")
	public String  index() {
		return userService.sample();
	}

	
	@RequestMapping("/test")
	public String  curl() {
		return "Hello Spring boot world";
	}
}
