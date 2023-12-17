package com.edoardo.bbs;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.function.Function;

@SpringBootApplication
public class BusinessBankSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(BusinessBankSystemApplication.class, args);
	}
}