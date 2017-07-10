package au.ivj.sandbox.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableSpringDataWebSupport
public class IOTApplication {

	public static void main(String[] args) {
		SpringApplication.run(IOTApplication.class, args);
	}
}
