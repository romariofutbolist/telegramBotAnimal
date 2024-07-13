package skyPro.telegramBotAnimal.listener;


import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updates.GetUpdates;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import skyPro.telegramBotAnimal.model.MenuBot;
import skyPro.telegramBotAnimal.model.Pet;
import skyPro.telegramBotAnimal.repository.NotificationTaskRepository;
import skyPro.telegramBotAnimal.repository.PetRepository;
import skyPro.telegramBotAnimal.service.PetService;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;


@Component
public class TelegramBotUpdatesListener extends TelegramLongPollingBot {
    @Autowired
    private PetService petService;
    private static final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

//    private final Pet pet;

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
            if (update.hasMessage()) {
                var message = update.getMessage();
                var text = message.getText();
                var chatId = message.getChatId().toString();

                if (text != null) {
                    SendMessage sendMessage = new SendMessage(chatId, "");
//
//            var message = update.getMessage();
//            if (message != null) {
//                var text = update.getMessage().getText();
//                var chatId = update.getMessage().getChatId().toString();
//
//                if (text != null) {
//                    SendMessage sendMessage = null;
                    switch (text) {
                        case "/start" ->
                            // Используем execute из TelegramLongPollingBot
                                execute(new SendMessage(chatId, "Привет! \uD83D\uDC4B Я бот, который поможет вам взаимодействовать с приютом," +
                                        "где бездомные животные находят заботу, уход, безопасность и надежду на новый дом. \n" +
                                        "Я могу рассказать вам о приюте, о его питомцах, как помочь питомцу найти свой дом, какие документы для этого необходимы и многое другое. \n" +
                                        "Жми скорее /menu"));
                        case "/menu" -> {
                            sendMessage = new SendMessage(chatId, "выберите услугу");
                            // Добавляем ReplyMarkup к SendMessage
                            sendMessage.setReplyMarkup(menuBot.sendMainMenu());
                            // Используем execute из TelegramLongPollingBot
                            execute(sendMessage);
                        }
                        case "Информация о приюте" -> {
                            // "Кнопка 1"
                            // Создаем новый объект SendMessage здесь:
                            sendMessage = new SendMessage(chatId, "Завести питомца — это очень серьезный шаг и здесь необходимо всё обдумать наперед!\n" +
                                    "Мы приют животных из Астаны, и в данном разделе меню, ты можешь найти необходимую информацию о нас.");
                            sendMessage.setReplyMarkup(menuBot.sendSubmenu1());
                            execute(sendMessage);
                        }
                        case "Расписание и адрес приюта" -> {
                            sendMessage = new SendMessage(chatId, "Наш приют расположен по адресу: г. Красноярск, Советский проспет, д.16.\n" +
                                    "Расписание работы приюта:\n" +
                                    " - [Понедельник - Пятница: 9:00 - 18:00].\n" +
                                    " - [Суббота - Воскресенье: 10:00 - 17:00].\n" +
                                    "Чтобы попасть на территорию приюта, необходимо получить пропуск у охраны по предварительной записи.\n" +
                                    "Контактные данные охраны: +7-921-911-19-19.");
                            sendMessage.setReplyMarkup(menuBot.sendSubmenu1());
                            execute(sendMessage);
                        }
                        case "Оформление пропуска и схема проезда" -> {
                            sendMessage = new SendMessage(chatId, "Для оформления пропуска необходимо при себе иметь паспорт.\n" +
                                    "После оформления пропуска Вам необходимо пройти в здание 16Д: Схема проезда указана на фото");
                            sendPhoto(chatId, "asd", "C:/Users/Анна/IdeaProjects/telegramBotAnimal/target/classes/static/123.jpg");
                            //sendPhoto(chatId, "asd", "static/123.jpg");
                            sendMessage.setReplyMarkup(menuBot.sendSubmenu1());
                            execute(sendMessage);
                        }
//telegramBotAnimal/src/main/resources/static/123.jpg
                        case "Как взять животное из приюта" -> {
                            //  "Кнопка 2"
                            sendMessage = new SendMessage(chatId, "Как взять животное из приюта");
                            sendMessage.setReplyMarkup(menuBot.sendSubmenu2());
                            execute(sendMessage);
                        }
                        case "Прислать отчет о питомце" ->
                            // "Кнопка 3"
                                execute(new SendMessage(chatId, "Питомец чувствует себя хорошо"));
                        case "Позвать волонтера" -> {
                            // "Кнопка 4"
                            sendMessage = new SendMessage(chatId, "Запрос отправлен волонтеру.");
                            execute(sendMessage);
                            sendToVolunteer(chatId, text);
                        }
                    }
                }
            }
        } catch (TelegramApiException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPhoto(String chatId, String imageCaption, String imagePath) throws FileNotFoundException, TelegramApiException {
        File imageFile = new File("C:/Users/Анна/IdeaProjects/telegramBotAnimal/target/classes/static/123.jpg");
        InputFile photo = new InputFile(imageFile);
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(photo);
        execute(sendPhoto);

    }
//    public void sendPhoto(String chatId, String imageCaption, String imagePath) throws FileNotFoundException, TelegramApiException {
//        File image = ResourceUtils.getFile("classpath:" + imagePath);
//        //File image = new File("C:/Users/Анна/IdeaProjects/telegramBotAnimal/target/classes/static/123.jpg");
//        SendPhoto sendPhoto = new SendPhoto();
//        System.out.println(image.getAbsolutePath());
//        sendPhoto.setChatId(chatId);
//        // Устанавливаем путь к файлу с изображением
//        sendPhoto.setCaption(imageCaption);
//        execute(sendPhoto);
//    }


    private void sendToVolunteer(String chatId, String text) throws TelegramApiException {
        final String ADMIN_ID = String.valueOf(934262991);
        execute(new SendMessage(ADMIN_ID, "Новое обращение от @" + chatId + ": " + text));
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


