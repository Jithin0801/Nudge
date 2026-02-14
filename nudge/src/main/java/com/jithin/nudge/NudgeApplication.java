package com.jithin.nudge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import com.jithin.nudge.config.SecurityProperties;

@SpringBootApplication
@EnableConfigurationProperties(SecurityProperties.class)
@ComponentScan(basePackages = "com.jithin.nudge.*")
public class NudgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(NudgeApplication.class, args);
	}

}
