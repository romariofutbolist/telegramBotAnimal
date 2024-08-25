package skyPro.telegramBotAnimal.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramBot;

@Service
public class TelegramBotService {
    private final TelegramBot telegramBot;

    public TelegramBotService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
//    TelegramBot bot = new TelegramBot(token);
//        bot.execute(new DeleteMyCommands());
//        return bot;
    public void sendMessage(long chatId, String message) {
        // Используйте telegramBot для отправки сообщения
        // telegramBot.sendMessage(chatId, message);
    }

    public void execute(SendMessage sendMessage) {
    }
}


