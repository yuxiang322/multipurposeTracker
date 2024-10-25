package com.xiang.multipurposeTracker.service;

import com.xiang.multipurposeTracker.DTO.ContactRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ContactRequestService {

    private final JavaMailSender emailSender;

    @Autowired
    public ContactRequestService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void handleContactRequest(ContactRequest contactRequest) {

        // Prepare the email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("lgzai96@gmail.com"); // Recipient's email
        message.setSubject("Contact Request from " + contactRequest.getName());
        message.setText("Request contact for Manager: " + contactRequest.getManagerName() + "\n"
                + " Manager Contact: " + contactRequest.getManagerContact() + "\n"
                + "Requester from Organization: " + contactRequest.getOrganization() + "\n"
                + "Requester Email: " + contactRequest.getEmail() + "\n"
                + "Message: " + contactRequest.getMessage());

        // Send the email
        emailSender.send(message);
    }
}
