package com.indigo.indvalid.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.context.annotation.Import;
import com.indigo.indvalid.jms.IndvalidJmsConfig;

@SpringBootApplication
@Import({ IndvalidJmsConfig.class, RestConfig.class})
public class IndvalidRestApp {
	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(IndvalidRestApp.class);
		springApplication.addListeners(new ApplicationPidFileWriter());
		springApplication.run(args);
	}
}
