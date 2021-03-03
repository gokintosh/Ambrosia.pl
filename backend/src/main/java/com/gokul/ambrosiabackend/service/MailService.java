package com.gokul.ambrosiabackend.service;


import com.gokul.ambrosiabackend.exception.ActivationException;
import com.gokul.ambrosiabackend.model.NotificationEmail;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class MailService {


    JavaMailSender javaMailSender;
    MailBuilder mailBuilder;

    @Async
    void sendMail(NotificationEmail notificationEmail){
        MimeMessagePreparator messagePreparator=mimeMessage -> {
            MimeMessageHelper messageHelper=new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("activation@ambrosia.pl");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(mailBuilder.build(notificationEmail.getBody()));
        };
        try{
            javaMailSender.send(messagePreparator);
            System.out.println("Activation Email send");
        }catch(MailException e){
            throw new ActivationException("Error sending activation email to "+notificationEmail.getRecipient());
        }
    }
}
