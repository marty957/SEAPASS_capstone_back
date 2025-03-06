package com.example.capstone.controllers;

import com.example.capstone.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-email")
public class EmailTestController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public String sendTestEmail(@RequestParam String to) {
        emailService.sendNotifications(to, "Test Email", "Questa è una mail di prova.");
        return "Email inviata con successo a " + to;
    }
}
