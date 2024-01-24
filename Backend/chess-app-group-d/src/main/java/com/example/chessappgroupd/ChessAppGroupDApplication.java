package com.example.chessappgroupd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ChessAppGroupDApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChessAppGroupDApplication.class, args);
	}

}
