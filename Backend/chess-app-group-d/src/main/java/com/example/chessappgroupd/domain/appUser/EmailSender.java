package com.example.chessappgroupd.domain.appUser;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.context.annotation.Bean;
//EmailSender
//A Bean of JavaMailSender
public class EmailSender {
    @Bean
    public static JavaMailSender getJavaMailSender() {
        return new JavaMailSenderImpl();
    }
}