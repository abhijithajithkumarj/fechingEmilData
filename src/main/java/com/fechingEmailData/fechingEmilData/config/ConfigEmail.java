package com.fechingEmailData.fechingEmilData.config;


import jakarta.mail.Session;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import javax.mail.Store;
import java.util.Properties;

@Getter
@Configuration
public class ConfigEmail {


    @Value("${email.username}")
    private String emailUserName;

    @Value("${email.password}")
    private String emailPassword;

    @Value("${email.protocol}")
    private String emailProtocol;

    @Value("${email.host}")
    private String emailHost;

    @Value("${email.port}")
    private int emailPort;

    @Bean
    public jakarta.mail.Session maileSession() {
        Properties properties = new Properties();
        properties.setProperty("mail.store.protocol", emailProtocol);
        properties.setProperty("mail.imaps.host", emailHost);
        properties.setProperty("mail.imaps.port", String.valueOf(emailPort));
        properties.setProperty("mail.imaps.auth", "true");
        properties.setProperty("mail.imaps.starttls.enable", "true");
        properties.setProperty("mail.user", emailUserName);
        properties.setProperty("mail.password", emailPassword);

        return Session.getDefaultInstance(properties);
    }












}
