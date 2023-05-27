package com.LTTBDD.ecommerce_app.common.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MailService {
    private static final String HOST = "smtp.gmail.com";
    private static final String PORT = "465";
    private static final String MAIL_SENDER_OWNER = "sonefast712@gmail.com";
    private static final String PASSWORD = "ypvmiyozzoazepuu";

    public Boolean sendForForgotPassword(String email, String subject, String content){
        try {
            Properties properties = System.getProperties();

            properties.setProperty("mail.smtp.host", HOST);
            properties.setProperty("mail.smtp.port", PORT);
            properties.setProperty("mail.smtp.ssl.enable", "true");
            properties.setProperty("mail.smtp.auth", "true");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(MAIL_SENDER_OWNER, PASSWORD);
                }
            });


            String to = email;

            MimeMessage message = new MimeMessage(session);
            message.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(email)));

            message.setSubject(subject);
            message.setText(content);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(message);
                    } catch (MessagingException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            thread.start();
            return true;
        }
        catch (AddressException e) {
            throw new RuntimeException(e);
        } catch (MessagingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
