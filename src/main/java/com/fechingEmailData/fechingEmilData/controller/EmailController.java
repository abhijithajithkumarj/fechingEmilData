package com.fechingEmailData.fechingEmilData.controller;

import com.fechingEmailData.fechingEmilData.config.ConfigEmail;
import com.fechingEmailData.fechingEmilData.entity.FetchEmailData;
import com.fechingEmailData.fechingEmilData.repository.FetchEmailDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.search.FlagTerm;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/url")
public class EmailController {

    @Autowired
    private ConfigEmail configEmail;

    @Autowired
    private FetchEmailDataRepository fetchEmailDataRepository;

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
    public Session maileSession() {
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

    @PutMapping("/read")
    public ResponseEntity<String> readingEmail() {
        try {
            Session maileSession = maileSession();
            Store store = maileSession.getStore("imaps");
            store.connect(emailHost, emailUserName, emailPassword);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

            Stream<Message> messageStream = Arrays.stream(messages);

            messageStream.forEach(message -> {
                try {
                    String subject = message.getSubject();
                    String from = InternetAddress.toString(message.getFrom());
                    String to = InternetAddress.toString(message.getRecipients(Message.RecipientType.TO));
                    String receivedDateTime = message.getReceivedDate().toString();

                    FetchEmailData fetchEmailData = new FetchEmailData();
                    fetchEmailData.setSubject(subject);
                    fetchEmailData.setFromAddress(from);
                    fetchEmailData.setToAddress(to);
                    fetchEmailData.setDateTimeFormat(receivedDateTime);

                    fetchEmailDataRepository.save(fetchEmailData);

                    System.out.println(fetchEmailData);
                    System.out.println("MongoDB Document ID: " + fetchEmailData.getId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            inbox.close(false);
            store.close();

            return ResponseEntity.ok("Mail read completed");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }
}
