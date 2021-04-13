package com.trippyTravel.services;


import com.trippyTravel.models.Trip;

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
}
