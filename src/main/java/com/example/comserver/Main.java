package com.example.comserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {

	public static Logger logger = LoggerFactory.getLogger(Main.class);
	public static ConfigurableApplicationContext context;
	
	public static void main(String[] args) throws Exception {
		context = SpringApplication.run(Main.class, args);
		Server server = context.getBean(Server.class);
		server.init();
	}

}
