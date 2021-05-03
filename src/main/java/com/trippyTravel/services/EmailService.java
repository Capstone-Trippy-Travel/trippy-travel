package com.trippyTravel.services;


import com.fasterxml.jackson.databind.JsonNode;
import com.trippyTravel.models.Trip;

import com.trippyTravel.models.User;
import com.trippyTravel.repositories.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service("emailService")
public class EmailService {
    @Autowired
    public JavaMailSender emailSender;

    @Value("${spring.mail.from}")
    private String from;

    public void prepareAndSend(Trip trips, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
//        msg.setTo(trips.getGroup().getOwner().getEmail());
        msg.setSubject(subject);
        msg.setText(body);
        try {
            this.emailSender.send(msg);
        } catch (MailException ex) {
            ex.printStackTrace();
        }
    }

    public void prepareAndSend1(User user,String email) {
        SimpleMailMessage msg = new SimpleMailMessage();
String message = "Your Username is:";
        msg.setFrom(from);
        msg.setTo(email);
        msg.setSubject(message);
        msg.setText("Your username is " + user.getUsername());
        try {
            this.emailSender.send(msg);
        } catch (MailException ex) {
            ex.printStackTrace();
        }
    }
//    public static JsonNode sendSimpleMessage() throws Unires{
//        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + YOUR_DOMAIN_NAME + "/messages"),
//			.basicAuth("api", API_KEY)
//                .queryString("from", "Excited User <USER@YOURDOMAIN.COM>")
//                .queryString("to", "artemis@example.com")
//                .queryString("subject", "hello")
//                .queryString("text", "testing")
//                .asJson();
//        return request.getBody();
//    }
//}
}
