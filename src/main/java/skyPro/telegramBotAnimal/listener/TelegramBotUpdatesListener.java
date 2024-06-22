package skyPro.telegramBotAnimal.listener;


import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import skyPro.telegramBotAnimal.model.MenuBot;
import skyPro.telegramBotAnimal.repository.NotificationTaskRepository;

import java.util.List;

@Service
public class TelegramBotUpdatesListener extends TelegramLongPollingBot {
    private static final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final NotificationTaskRepository repository;
    private final MenuBot menuBot;

    public TelegramBotUpdatesListener(NotificationTaskRepository repository, MenuBot menuBot) {

        this.repository = repository;
        this.menuBot = menuBot;
    }

    @PostConstruct
    public void init() throws TelegramApiException {

        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(this);
    }


    @Override
    public void onUpdateReceived(Update update) {
        try {
        logger.info("Processing update: {}", update);
        var message = update.getMessage();
        if (message != null) {
            var text = update.getMessage().getText();
            var chatId = update.getMessage().getChatId().toString();
            if (text != null) {
                if ("/start".equals(text)) {

                        execute(new SendMessage(chatId, "Hello!"));

                } else if ("/menu".equals(text)) {

                    var sendMessage = new SendMessage(chatId, "1");
                    sendMessage.setReplyMarkup(menuBot.sendMainMenu());
                    execute(sendMessage);
                } else if ("Информация о приюте".equals(text)) {
                    // "Кнопка 1"
                    execute(new SendMessage(chatId, "инфо"));
                } else if ("Как взять животное из приюта".equals(text)) {
                    //  "Кнопка 2"
                    execute(new SendMessage(chatId, "взять животное"));
                }
            }
        }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotToken() {
        return "7365332306:AAF3my2PNLsx2zSa9usGNUHftYZeTgjBKFQ";
    }

    @Override
    public String getBotUsername() {
        return "bot";
    }
}


//    @Override
//    public void onUpdateReceived(org.telegram.telegrambots.meta.api.objects.Update update) {
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            String messageText = update.getMessage().getText();
//            long chatId = update.getMessage().getChatId();
//
//            // Обработка команд
//            if (messageText.equals("/start")) {
//                sendMainMenu(chatId);
//            } else if (messageText.equals("Option 1")) {
//                sendMessage(chatId, "You selected Option 1!");
//            } else if (messageText.equals("Option 2")) {
//                sendMessage(chatId, "You selected Option 2!");
//            } else {
//                sendMessage(chatId, "Invalid command.");
//            }
//        }
//    }
