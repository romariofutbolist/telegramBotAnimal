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
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import skyPro.telegramBotAnimal.model.MenuBot;
import skyPro.telegramBotAnimal.repository.NotificationTaskRepository;


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
                    SendMessage sendMessage = null;
                    if ("/start".equals(text)) {
                        // Используем execute из TelegramLongPollingBot
                        execute(new SendMessage(chatId, "Hello!"));

                    } else if ("/menu".equals(text)) {
                        sendMessage = new SendMessage(chatId, "выберите услугу");
                        // Добавляем ReplyMarkup к SendMessage
                        sendMessage.setReplyMarkup(menuBot.sendMainMenu());
                        // Используем execute из TelegramLongPollingBot
                        execute(sendMessage);
                    } else if ("Информация о приюте".equals(text)) {
                        // "Кнопка 1"
                        // Создаем новый объект SendMessage здесь:
                        sendMessage = new SendMessage(chatId, "Информация о приюте");
                        sendMessage.setReplyMarkup(menuBot.sendSubmenu1());
                        execute(sendMessage);



                    } else if ("Как взять животное из приюта".equals(text)) {
                        //  "Кнопка 2"
                        sendMessage = new SendMessage(chatId, "Как взять животное из приюта");
                        sendMessage.setReplyMarkup(menuBot.sendSubmenu2());
                        execute(sendMessage);


                    } else if ("Прислать отчет о питомце".equals(text)) {
                        // "Кнопка 3"
                        execute(new SendMessage(chatId, "Питомец чувствует себя хорошо"));
                    } else if ("Позвать волонтера".equals(text)) {
                        // "Кнопка 4"
                        execute(new SendMessage(chatId, "Здравствуйте, меня зовут Иван, я волонтер приюта, чем могу помочь?"));
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


