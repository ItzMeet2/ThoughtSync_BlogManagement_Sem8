package com.mycompany.blogmanagement.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtil {
    private static final String SMTP_HOST = EmailConstants.SMTP_HOST;
    private static final String SMTP_PORT = EmailConstants.SMTP_PORT;
    private static final String EMAIL_USERNAME = EmailConstants.EMAIL_USERNAME;
    private static final String EMAIL_PASSWORD = EmailConstants.EMAIL_PASSWORD;

    public static void sendResetEmail(String toEmail, String resetLink) throws Exception {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", SMTP_HOST);
        prop.put("mail.smtp.port", SMTP_PORT);
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(EMAIL_USERNAME, "ThoughtSync"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Password Reset Request - ThoughtSync");

        String htmlContent = "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px;\">"
                + "<h2 style=\"color: #4a90e2; text-align: center;\">ThoughtSync Password Reset</h2>"
                + "<p>Hello,</p>"
                + "<p>You requested a password reset for your ThoughtSync account. Please click the button below to reset your password. This link will expire in 1 hour.</p>"
                + "<div style=\"text-align: center; margin: 30px 0;\">"
                + "<a href=\"" + resetLink + "\" style=\"background-color: #4a90e2; color: white; padding: 12px 24px; text-decoration: none; border-radius: 4px; font-weight: bold; display: inline-block;\">Reset Password</a>"
                + "</div>"
                + "<p>If the button doesn't work, copy and paste this link into your browser:</p>"
                + "<p><a href=\"" + resetLink + "\">" + resetLink + "</a></p>"
                + "<hr style=\"border: 0; border-top: 1px solid #e0e0e0; margin: 20px 0;\">"
                + "<p style=\"font-size: 0.8em; color: #888888;\">If you did not request this, you can safely ignore this email.</p>"
                + "</div>";

        message.setContent(htmlContent, "text/html; charset=utf-8");

        Transport.send(message);
    }
}
