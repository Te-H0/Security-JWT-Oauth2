package me.teho.SecurityJwtOauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SecurityJwtOauth2Application {

    public static void main(String[] args) {
        SpringApplication.run(SecurityJwtOauth2Application.class, args);

    }

}
