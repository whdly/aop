package com.whd.aop;

import javax.annotation.Resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class AopApplication {
	
	@RequestMapping("/")
	public String hello(){
		return "hello springboot!";
	}
	public static void main(String[] args) {
		SpringApplication.run(AopApplication.class, args);
	}

}
