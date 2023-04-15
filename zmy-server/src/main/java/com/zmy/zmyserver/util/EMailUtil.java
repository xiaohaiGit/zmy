package com.zmy.zmyserver.util;

import com.zmy.zmyserver.exception.EmailException;
import com.zmy.zmyserver.modle.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;

@Slf4j
@Component
public class EMailUtil {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private PasswordUtil passwordUtil;


    public boolean forgotPassword(User user) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper minehelper = new MimeMessageHelper(message, true);
            minehelper.setFrom(mailProperties.getUsername());
            minehelper.setTo(user.getEmail());
            minehelper.setSubject("智慧空调平台 forgot password process");

            Context context = new Context();
            context.setVariable("user", user);

            String forgotToken = passwordUtil.forgotPassword(user.getEmail());
            String recoverUrl = "http://localhost/recover-password.html?forgot-token=" + forgotToken;
            context.setVariable("recoverUrl", recoverUrl);
            minehelper.setText(templateEngine.process("forgot-password", context), true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("send email failed!", e);
            throw new EmailException();
        }

        log.info("send mail success!");
        return true;
    }

}
