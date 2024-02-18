package com.fechingEmailData.fechingEmilData.serviceImp;

import com.fechingEmailData.fechingEmilData.entity.FetchEmailData;
import com.fechingEmailData.fechingEmilData.repository.FetchEmailDataRepository;
import com.fechingEmailData.fechingEmilData.service.FetchingService;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.search.FlagTerm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Properties;

@Service
public class FetchingEmailServiceImp implements FetchingService {

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

    @Bean
    public Session mailSession() {
        Properties properties = new Properties();
        properties.setProperty("mail.store.protocol", emailProtocol);
        properties.setProperty("mail.imaps.host", emailHost);
        properties.setProperty("mail.imaps.port", "993");
        properties.setProperty("mail.imaps.auth", "true");
        properties.setProperty("mail.imaps.starttls.enable", "true");
        properties.setProperty("mail.user", emailUserName);
        properties.setProperty("mail.password", emailPassword);

        return Session.getInstance(properties);
    }

    public boolean fetchUnreadMessages() {
        try {
            Store store = mailSession().getStore("imaps");
            store.connect(emailHost, emailUserName, emailPassword);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

            Arrays.stream(messages).forEach(message -> {
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

                    System.out.println("Mongodb document id: " + fetchEmailData.getId());
                    System.out.println("Subject: " + subject);
                    System.out.println("From: " + from);
                    System.out.println("To: " + to);
                    System.out.println("Received Date: " + receivedDateTime);
                    System.out.println("-----------------------------------");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });


            inbox.close(false);
            store.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
