package com.tsts.presentation;

import com.tsts.services.EmailService;
import picocli.CommandLine;

@CommandLine.Command(name = "email", description = "Analyze commits for a specific email.")
public class EmailCommand implements Runnable {

    @CommandLine.Parameters(paramLabel = "EMAIL", description = "Email address to analyze commits for")
    private String email;

    @Override
    public void run() {
        EmailService emailService = new EmailService();
        emailService.showActivity(email);
    }

}
