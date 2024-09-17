package com.honley.fastcard.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${API_URL}")
    private String link;

    @Autowired
    public MailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendActivationMail(String to) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String subject = "Account activation on FastCard";
        String htmlContent = getActivationHtmlContent(link);

        helper.setFrom(username);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    public void sendResetPasswordMail(String to) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String subject = "Password Reset";
        String htmlContent = getResetPasswordHtmlContent(link);

        helper.setFrom(username);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    private String getActivationHtmlContent(String link) {
        return "<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "<head>\n"
                + "    <meta charset=\"UTF-8\">\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    <title>FastCard Account Activation</title>\n"
                + "    <style>\n"
                + "        body {\n"
                + "            font-family: Arial, sans-serif;\n"
                + "            background-color: #f4f4f4;\n"
                + "            color: #333333;\n"
                + "            margin: 0;\n"
                + "            padding: 0;\n"
                + "            line-height: 1.6;\n"
                + "        }\n"
                + "        .container {\n"
                + "            width: 80%;\n"
                + "            max-width: 600px;\n"
                + "            margin: 30px auto;\n"
                + "            background-color: #ffffff;\n"
                + "            border-radius: 8px;\n"
                + "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\n"
                + "            padding: 20px;\n"
                + "        }\n"
                + "        h1 {\n"
                + "            color: #000000;\n"
                + "            text-align: center;\n"
                + "        }\n"
                + "        p {\n"
                + "            margin-bottom: 20px;\n"
                + "        }\n"
                + "        .activation-button {\n"
                + "            display: block;\n"
                + "            width: fit-content;\n"
                + "            margin: 20px auto;\n"
                + "            padding: 10px 20px;\n"
                + "            background-color: #000000;\n"
                + "            color: #ffffff;\n"
                + "            text-decoration: none;\n"
                + "            border-radius: 4px;\n"
                + "            font-weight: bold;\n"
                + "            text-align: center;\n"
                + "        }\n"
                + "        .activation-button:hover {\n"
                + "            background-color: #303030;\n"
                + "        }\n"
                + "        .signature {\n"
                + "            margin-top: 40px;\n"
                + "            font-style: italic;\n"
                + "            text-align: center;\n"
                + "        }\n"
                + "        @media (max-width: 600px) {\n"
                + "            .container {\n"
                + "                width: 100%;\n"
                + "                padding: 15px;\n"
                + "            }\n"
                + "        }\n"
                + "    </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "    <div class=\"container\">\n"
                + "        <h1>Activate Your FastCard Account</h1>\n"
                + "        <p>Hello,</p>\n"
                + "        <p>Thank you for joining FastCard!</p>\n"
                + "        <p>Please activate your FastCard account by clicking the button below:</p>\n"
                + "        <a href=\"" + link + "\" class=\"activation-button\" target=\"_blank\" rel=\"noopener noreferrer\">Activate Account</a>\n"
                + "        <p>If the button doesn't work, you can also activate your account by following this link:</p>\n"
                + "        <p><a href=\"" + link + "\" target=\"_blank\" rel=\"noopener noreferrer\">" + link + "</a></p>\n"
                + "        <p class=\"signature\"><br>The FastCard Team</p>\n"
                + "    </div>\n"
                + "</body>\n"
                + "</html>";
    }

    private String getResetPasswordHtmlContent(String link) {
        return "<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "<head>\n"
                + "    <meta charset=\"UTF-8\">\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    <title>FastCard Password Reset</title>\n"
                + "    <style>\n"
                + "        body {\n"
                + "            font-family: Arial, sans-serif;\n"
                + "            background-color: #f4f4f4;\n"
                + "            color: #333333;\n"
                + "            margin: 0;\n"
                + "            padding: 0;\n"
                + "            line-height: 1.6;\n"
                + "        }\n"
                + "        .container {\n"
                + "            width: 80%;\n"
                + "            max-width: 600px;\n"
                + "            margin: 30px auto;\n"
                + "            background-color: #ffffff;\n"
                + "            border-radius: 8px;\n"
                + "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\n"
                + "            padding: 20px;\n"
                + "        }\n"
                + "        h1 {\n"
                + "            color: #000000;\n"
                + "            text-align: center;\n"
                + "        }\n"
                + "        p {\n"
                + "            margin-bottom: 20px;\n"
                + "        }\n"
                + "        .reset-button {\n"
                + "            display: block;\n"
                + "            width: fit-content;\n"
                + "            margin: 20px auto;\n"
                + "            padding: 10px 20px;\n"
                + "            background-color: #007bff;\n"
                + "            color: #ffffff;\n"
                + "            text-decoration: none;\n"
                + "            border-radius: 4px;\n"
                + "            font-weight: bold;\n"
                + "            text-align: center;\n"
                + "        }\n"
                + "        .reset-button:hover {\n"
                + "            background-color: #0056b3;\n"
                + "        }\n"
                + "        .signature {\n"
                + "            margin-top: 40px;\n"
                + "            font-style: italic;\n"
                + "            text-align: center;\n"
                + "        }\n"
                + "        @media (max-width: 600px) {\n"
                + "            .container {\n"
                + "                width: 100%;\n"
                + "                padding: 15px;\n"
                + "            }\n"
                + "        }\n"
                + "    </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "    <div class=\"container\">\n"
                + "        <h1>Reset Your FastCard Password</h1>\n"
                + "        <p>Hello,</p>\n"
                + "        <p>We received a request to reset your FastCard account password.</p>\n"
                + "        <p>Please reset your password by clicking the button below:</p>\n"
                + "        <a href=\"" + link + "\" class=\"reset-button\" target=\"_blank\" rel=\"noopener noreferrer\">Reset Password</a>\n"
                + "        <p>If the button doesn't work, you can also reset your password by following this link:</p>\n"
                + "        <p><a href=\"" + link + "\" target=\"_blank\" rel=\"noopener noreferrer\">" + link + "</a></p>\n"
                + "        <p>If you didn't request a password reset, please ignore this email or contact support if you have questions.</p>\n"
                + "        <p class=\"signature\"><br>The FastCard Team</p>\n"
                + "    </div>\n"
                + "</body>\n"
                + "</html>";
    }
}
