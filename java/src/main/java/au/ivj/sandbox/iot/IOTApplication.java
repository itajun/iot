package au.ivj.sandbox.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableSpringDataWebSupport
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class IOTApplication {

	public static void main(String[] args) {
		SpringApplication.run(IOTApplication.class, args);
	}
}
