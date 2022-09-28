package com.rishabhsoft.bulkmessaging;

import com.rishabhsoft.bulkmessaging.controller.SESController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(value = {"com.rishabhsoft.bulkmessaging.service.SESService"})
public class BulkmessagingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BulkmessagingApplication.class, args);
	}

}
