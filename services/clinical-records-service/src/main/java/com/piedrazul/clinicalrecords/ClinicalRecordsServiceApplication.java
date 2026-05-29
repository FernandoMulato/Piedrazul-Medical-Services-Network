package com.piedrazul.clinicalrecords;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients 
public class ClinicalRecordsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClinicalRecordsServiceApplication.class, args);
	}

} 
