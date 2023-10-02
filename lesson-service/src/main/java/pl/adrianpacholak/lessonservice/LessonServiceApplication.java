package pl.adrianpacholak.lessonservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class LessonServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LessonServiceApplication.class, args);
	}

}
