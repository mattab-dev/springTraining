package com.capgemini.wsb.fitnesstracker.notification;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
@RequiredArgsConstructor
@Slf4j
public class ReportGenerator {

   // private final JavaMailSender javaMailSender;

    public void generateRaport() {

    }
}
