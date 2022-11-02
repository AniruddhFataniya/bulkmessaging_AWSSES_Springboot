package com.rishabhsoft.bulkmessaging;

import com.rishabhsoft.bulkmessaging.controller.SESController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
//@ComponentScan(value = {"com.rishabhsoft.bulkmessaging.service.CloudWatchService"})
public class BulkmessagingApplication {

	@PostConstruct
	public void init(){
		// Setting Spring Boot SetTimeZone
		TimeZone.setDefault(TimeZone.getTimeZone("IST"));
	}

	public static void main(String[] args) {
		SpringApplication.run(BulkmessagingApplication.class, args);
	}

}
