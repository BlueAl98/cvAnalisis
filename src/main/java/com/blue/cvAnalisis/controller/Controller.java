package com.blue.cvAnalisis.controller;

import com.blue.cvAnalisis.model.EmailRequest;
import com.blue.cvAnalisis.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {

    private final EmailService emailService;

    public Controller(EmailService emailService) {
        this.emailService = emailService;
    }


    @GetMapping
    public EmailRequest testEmail() {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo("alblue9817@gmail.com");
        emailRequest.setSubject("Test Email");
        emailRequest.setBody("This is blue demon");
      return  emailService.sendEmailRequest(emailRequest);

    }

}
