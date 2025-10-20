package io.mountblue.blogger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BloggerApplication {

	public static void main(String[] args) {
		io.mountblue.blogger.GrantPrivileges.main(null);
		
		SpringApplication.run(BloggerApplication.class, args);
	}

}
