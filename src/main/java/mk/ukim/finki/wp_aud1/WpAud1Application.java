package mk.ukim.finki.wp_aud1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })

public class WpAud1Application {

    public static void main(String[] args) {

        SpringApplication.run(WpAud1Application.class, args);
    }

}