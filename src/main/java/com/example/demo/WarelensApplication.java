package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * WareLens 애플리케이션 엔트리 포인트 (서버 구동 클래스)
 */
@SpringBootApplication
public class WarelensApplication {

	public static void main(String[] args) {
		// 스프링 부트 서버 엔진 가동
		SpringApplication.run(WarelensApplication.class, args);
	}

}