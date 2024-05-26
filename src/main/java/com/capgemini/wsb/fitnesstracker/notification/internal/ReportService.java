package com.capgemini.wsb.fitnesstracker.notification.internal;

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
public class ReportService {
    private static final String TITLE = "Last week's training report";

    private final JavaMailSender javaMailSender;
    private final UserProvider userProvider;
    private final TrainingProvider trainingProvider;

   // @Scheduled(cron = "0 0 12 ? * 1")
   @Scheduled(cron = "0 * * * * *")
    public void generateReport() {
        log.info("Starting generation of training reports");
        final List<User> allUsers = userProvider.findAllUsers();
        allUsers.forEach(user -> {
            final SimpleMailMessage email = createEmail(user.getEmail(), TITLE, trainingProvider.findAllForUserId(user.getId()));
            log.info("Sending email to {}", user.getEmail());
            javaMailSender.send(email);
        });

        log.info("Generation of training reports finished");
    }

    private SimpleMailMessage createEmail(final String recipient, final String title, final List<Training> trainings) {
        final Date lastWeek = returnBeginningOfLastWeek();
        final Date yesterday = returnYesterday();
        final List<Training> lastWeekTrainings = trainings.stream().filter(training -> training.getStartTime().after(lastWeek) && training.getStartTime().before(yesterday)).toList();
        log.info("Creating email for {}", recipient);
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(title);
        email.setTo(recipient);
        final StringBuilder builder = new StringBuilder("""
                You had %s trainings last week,
                completing %s units of distance
                You have completed %s trainings overall.
                Below you can find detailed rundown of your trainings last week:
                ----
                """.formatted(lastWeekTrainings.size(),
                    trainings.isEmpty() ? 0 : lastWeekTrainings.stream().mapToDouble(Training::getDistance).sum(),
                trainings.size()
                ));
        lastWeekTrainings.forEach(training -> {
            builder.append("""
                training start: %s
                training end: %s
                activity type: %s
                distance: %s
                average speed: %s
                ----
                """.formatted(training.getStartTime(),
                training.getEndTime() == null ? "-" : training.getEndTime(),
                training.getActivityType(),
                training.getDistance(),
                training.getAverageSpeed()
            ));
        });
        email.setText(builder.toString());
        log.info("Email created");
        // Minimal requirement according to mr Weychan was to present data in the console, so here it is
        System.out.println(builder);
        return email;
    }

    private Date returnBeginningOfLastWeek() {
        final Calendar now = Calendar.getInstance();
        now.add(DAY_OF_MONTH, -7);
        return now.getTime();
    }

    private Date returnYesterday() {
        final Calendar now = Calendar.getInstance();
        now.add(DAY_OF_MONTH, -1);
        return now.getTime();
    }
}
