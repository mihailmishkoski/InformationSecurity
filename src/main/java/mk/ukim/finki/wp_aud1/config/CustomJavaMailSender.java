package mk.ukim.finki.wp_aud1.config;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

public class CustomJavaMailSender extends JavaMailSenderImpl {

    public CustomJavaMailSender() {
        super();
        this.setHost("smtp.gmail.com");
        this.setPort(587);
        this.setUsername("dmgameplays829@gmail.com");
        this.setPassword("drmixkiller5555515"); // Use your app password or account password

        // Additional configuration for SSL, TLS, etc.
        Properties props = this.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true"); // Enable debug mode to see more details

        // You may need to adjust these properties based on your specific requirements and environment
    }
}
