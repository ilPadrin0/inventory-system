package com.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
		
		System.out.println("\n🚀 서버가 성공적으로 실행되었습니다! (http://localhost:8080) 😊");
	}

}
