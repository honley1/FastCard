package com.honley.fastcard.utils;

import com.honley.fastcard.config.AdminsIdConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

@Component
public class SendApplication extends TelegramLongPollingBot {

    @Value("${BOT_TOKEN}")
    private String TOKEN;

    @Value("${ADMINS_ID}")
    private String adminsIdProperty;

    private final String[] ADMINS_ID = AdminsIdConfig.getAdminsId();

    @Override
    public String getBotUsername() {
        return "FastCardOfficialBot";
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // Implement your update handling logic here
    }

    public void sendApplication(String fullName, String phoneNumber, String username) {
        String text = String.format(
                "Новая заявка от: *%s*\nИмя: *%s*\nТелефон: *%s*",
                username, fullName, phoneNumber
        );

        for (String adminId : ADMINS_ID) {
            SendMessage message = new SendMessage();
            message.setChatId(adminId);
            message.setText(text);
            message.setParseMode("Markdown");

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}