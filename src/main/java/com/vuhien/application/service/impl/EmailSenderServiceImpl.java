package com.vuhien.application.service.impl;

import com.vuhien.application.config.SpringMailConfig;
import com.vuhien.application.service.EmailSenderService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by VuHien96 on 02/08/2021 22:09
 */
@Component
@AllArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {
    private static final Logger logger = LoggerFactory.getLogger(EmailSenderServiceImpl.class);

    private final JavaMailSender javaMailSender;

    @Async
    @Override
    public void sendEmail(String to, String name, String link) {
        try {
            //Tạo một Mime Message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject("Xác nhận tài khoản");
            // Prepare the evaluation context
            final Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("link", link);
            // Create the HTML body using Thymeleaf
            final String htmlContent = SpringMailConfig.getTemplateEngine().process("mail-template.html", context);
            mimeMessage.setContent(htmlContent, "text/html");
            // Send Message!
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }
}
