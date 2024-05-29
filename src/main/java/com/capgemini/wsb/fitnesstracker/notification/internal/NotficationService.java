package com.capgemini.wsb.fitnesstracker.notification.internal;

import com.capgemini.wsb.fitnesstracker.mail.api.EmailDto;
import com.capgemini.wsb.fitnesstracker.mail.api.EmailProvider;
import com.capgemini.wsb.fitnesstracker.mail.api.EmailSender;
import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Calendar.DAY_OF_MONTH;

@EnableScheduling
@Service
@Data
@Slf4j
class NotficationService {
    private static final String TITLE = "Last week's training report";
    private final UserProvider userProvider;
    private final TrainingProvider trainingProvider;
    private final EmailProvider emailProvider;
    private final EmailSender emailSender;

    @Scheduled(cron = "0 0 12 ? * 1")// this cron is for sending emails as given in instructions, for testing other cron is used
   //@Scheduled(cron = "0 * * * * *")
    public void generateReport() {
        log.info("Starting generation of training reports");
        final List<User> allUsers = userProvider.findAllUsers();
        allUsers.forEach(user -> {
            final EmailDto email = emailProvider.createEmail(user.getEmail(), TITLE, trainingProvider.findAllForUserId(user.getId()));
            log.info("Sending email to {}", user.getEmail());
            emailSender.send(email);
        });

        log.info("Generation of training reports finished");
    }
}
