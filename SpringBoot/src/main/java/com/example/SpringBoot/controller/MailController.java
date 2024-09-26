package com.example.SpringBoot.controller;

import com.example.SpringBoot.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mail")
public class MailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send-email")
    public String sendEmail() {
        try {
            emailService.sendSimpleMessage("mahy3241@gmail.com", "Test Subject", "This is a test email.");
            return "Email sent successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error sending email: " + e.getMessage();
        }
    }
}
