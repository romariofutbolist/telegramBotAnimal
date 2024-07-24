package skyPro.telegramBotAnimal.listener;


import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import skyPro.telegramBotAnimal.configuration.ConfigurationAnimal;
import skyPro.telegramBotAnimal.model.MenuBot;
import skyPro.telegramBotAnimal.model.NotificationTask;
import skyPro.telegramBotAnimal.repository.NotificationTaskRepository;
import skyPro.telegramBotAnimal.service.PetService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class TelegramBotUpdatesListener extends TelegramLongPollingBot {
    private Map<Long, String> userStates = new HashMap<>(); //
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\+7-9\\d{2}-\\d{3}-\\d{2}-\\d{2}");

    @Autowired
    private PetService petService;
    private static final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final ConfigurationAnimal animal;
//    private final Pet pet;

    private final NotificationTaskRepository repository;
    private final MenuBot menuBot;


    public TelegramBotUpdatesListener(ConfigurationAnimal animal, NotificationTaskRepository repository, MenuBot menuBot) {
        this.animal = animal;
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
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            var state = userStates.get(chatId);

            if ("PhoneListener".equals(state)) {
                handleContactInput(chatId, text);
                userStates.remove(chatId);
            } else {
                switch (text) {
                    case "/start":
                        startCommandReceived(chatId, text);
                        break;

                    case "/menu":
                        menu(chatId, text);
                        break;

                    case "Информация о приюте":
                        informationAboutShelter(chatId, text);
                        break;

                    case "Расписание и адрес приюта":
                        adressOfShelter(chatId, text);
                        break;

                    case "Оформление пропуска и схема проезда":
                        registrationOfPass(chatId, text);
                        break;

                    case "Техника безопасности":
                        safetyEquipment(chatId, text);
                        break;

                    case "Запросить связь":
                        contactPhoneNumber(chatId, text);
                        break;

                    case "Как взять животное из приюта":
                        takeAnimalFromShelter(chatId, text);
                        break;

                    case "Список животных":
                        updateFile("Список животных", "Кот барсик 4 месяца, 3 кг, цвет рыжий\n" +
                                "Кот васька 3 месяца, 2.4 кг, цвет белый");
                        try {
                            sendDocument(310232057L, new File("Список животных"));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;

                    case "Правила знакомства и усыновления":
                        getRulesOfBehaviorAtShelter(chatId, text);
                        break;

                    case "Список необходимых документов":
                        provideListOfDocuments(chatId, text);
                        break;

                    case "Рекомендации":
                        getRecommendations(chatId, text);
                        break;

                    case "Транспортировка животного":
                        getRecommendationsAnimalTransportation(chatId, text);
                        break;

                    case "Обустройство дома":
                        getRecommendationsHomeImprovement(chatId,text);
                        break;

                    case "Обустройство дома для взрослого питомца":
                        getRecommendationsHomeImprovementForAdult(chatId,text);
                        break;

                    case "Обустройство дома для питомца с ограниченными возможностями":
                        getRecommendationsHomeImprovementForDisabledPet(chatId,text);
                        break;

                    case "Советы кинолога":
                        getAdviceFromDogHandler(chatId, text);
                        break;

                    case "Проверенные кинологи":
                        getDogHandlerContacts(chatId, text);
                        break;

                    case "Причины отказа":
                        getReasonsForRefusal(chatId, text);
                        break;

                    case "Позвать волонтера":
                        callToVolunteer(chatId, text);
                        break;

                    // default:
                    //    writeIncorrectText(chatId, text);
                    //    break;

                }
            }
/*
            if (!userStates.containsKey(chatId)) {
                // Новый пользователь
                handleNewRequest(chatId, text);

 */
           /* } else {
                // Уже был контакт с ботом
                handleExistingRequest(chatId, update.getMessage().getText());

            */
        }


    }




    public void sendDocument(long chatId, File file) throws TelegramApiException {
        SendDocument request = new SendDocument();
        request.setChatId(chatId);
        request.setDocument(new InputFile(file));
        execute(request);


    }

    public void updateFile(String file, String content) {
        try (var out = new BufferedWriter(new FileWriter(file))) {
            out.write(content);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void startCommandReceived(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Привет! Я бот, который поможет вам взаимодействовать с приютом,где бездомные животные находят заботу, уход, безопасность и надежду на новый дом." +
                "\n" + "Я могу рассказать вам о приюте, о его питомцах, как помочь питомцу найти свой дом, какие документы для этого необходимы и многое другое." +
                "\n" + "Жми скорее /menu");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException();
        }
    }


    private void menu(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("выберите услугу");
        message.setReplyMarkup(menuBot.sendMainMenu());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
    }

    private void informationAboutShelter(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Завести питомца — это очень серьезный шаг и здесь необходимо всё обдумать наперед!\n" +
                "Мы приют животных из Астаны, и в данном разделе меню, ты можешь найти необходимую информацию о нас.");
        message.setReplyMarkup(menuBot.sendSubmenu1());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
    }

    private void adressOfShelter(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Наш приют расположен по адресу: г. Красноярск, Советский проспет, д.16.\n" +
                "Расписание работы приюта:\n" +
                " - [Понедельник - Пятница: 9:00 - 18:00].\n" +
                " - [Суббота - Воскресенье: 10:00 - 17:00].\n" +
                "Чтобы попасть на территорию приюта, необходимо получить пропуск у охраны по предварительной записи.\n" +
                "Контактные данные охраны: +7-921-911-19-19.");
        message.setReplyMarkup(menuBot.sendSubmenu1());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
    }

    private void registrationOfPass(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Для оформления пропуска необходимо при себе иметь паспорт.\n" +
                "После оформления пропуска Вам необходимо пройти в здание 16Д: Схема проезда указана на фото");
        message.setReplyMarkup(menuBot.sendSubmenu1());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
        sendPhoto(chatId, "asd", "/home/roma/telegramBotAnimal/target/classes/static/123.jpg");
    }

    public void sendPhoto(long chatId, String imageCaption, String imagePath) {
        File imageFile = new File("/home/roma/telegramBotAnimal/target/classes/static/123.jpg");
        InputFile photo = new InputFile(imageFile);
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(photo);
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
    }    private void getRecommendationsHomeImprovementForAdultPet(long chatId, String text) {
    }

    private void safetyEquipment(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Вот некоторые правила техники безопасности в приюте для животных:\n" +
                "1. Проявляйте терпение и уважение к сотрудникам, волонтерам и другим посетителям.\n" +
                "2. Любые действия в приюте совершаются с разрешения работников или руководства.\n" +
                "3. На территории приюта не кричите, не размахивайте руками, не бегайте между будками или вольерами, не пугайте и не дразните животных.\n" +
                "4. Запрещается посещение приюта в состоянии алкогольного, наркотического опьянения.\n" +
                "5. Запрещается самостоятельно открывать вольеры и выводить животное без разрешения сотрудника приюта.\n" +
                "6. Запрещается подходить близко к вольерам и гладить собак через сетку на выгулах.\n" +
                "7. Запрещается допускать близкий контакт между собаками во время выгула во избежание драк.\n" +
                "8. Запрещается отпускать животных с поводка.\n" +
                "9. Разрешается гулять только на отведенной территории, о которой сообщит работник приюта.\n" +
                "При несоблюдении правил сотрудники приюта оставляют за собой право отказать посетителю в посещении приюта.");
        message.setReplyMarkup(menuBot.sendSubmenu1());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
    }

    private void contactPhoneNumber(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Я могу записать Ваши контактные данные и в ближайшее время с Вами свяжется наш волонтер и проконсультируют Вас. " +
                "Введите - Number phone: и далее номер телефона");
        try {
            execute(message);
            userStates.put(chatId, "PhoneListener");
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
    }

    private void handleContactInput(Long chatId, String text) {
        Matcher matcher = PHONE_PATTERN.matcher(text);
        if (matcher.matches()) {
            var task = new NotificationTask();
            task.setPhone(text);
            task.setChat_id(chatId);
            task.setText_msg("Здесь должно быть имя");
            task.setId(chatId);
            repository.save(task);
            sendMessage(chatId, "Номер телефона успешно сохранен! Нажмите кнопку /menu");
        } else {
            sendMessage(chatId, "Неверный формат номера телефона. Пожалуйста, введите номер в формате:" + "Number phone:+7-9**-**-**");
        }
    }


//    if (matcher.matches()) {
//        var date = parseDate(matcher.group(1));
//        if (date == null) {
//            telegramBot.execute(new SendMessage(chatId, "Неправильный формат даты"));
//            return;
//        }
//        var task = new NotificationTask();
//        task.setText_msg(matcher.group(3));
//        task.setChat_id(chatId);
//        task.setDate(date);
//        repository.save(task);
//        logger.info("Task has been saved: {}", task);
//    }

    private void takeAnimalFromShelter(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("В данном разделе я помогу тебе с выбором твоего будущего друга, " +
                "дам список необходимых документов, чтобы забрать питомца из приюта, " +
                "дам список рекомендаций по транспортиовке и обустройству дома для питомца " +
                "и предоставлю контактные данные кинологов для получения советов по общению с питомцем");
        message.setReplyMarkup(menuBot.sendSubmenu2());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
    }

    private void getRulesOfBehaviorAtShelter(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Вот Вам несколько ссылок для ознакомления. Здесь вы сможете найти необходимую для вас информацию:\n"
                + "https://adme.media/articles/10-sovetov-kotorye-pomogut-podruzhitsya-s-neznakomoj-sobakoj-2509006/:\n" +
                "https://www.mk.ru/social/2020/08/15/kak-vesti-sebya-s-zhivotnymi-iz-priyuta-pyat-osnovnykh-pravil.html");
        message.setReplyMarkup(menuBot.sendSubmenu2());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
    }

    private void provideListOfDocuments(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Если вы решились обзавестись новым членом семьи, ниже представлен список основных шагов и документов на собаку или кошку из приюта, которые у вас могут запросить: \n"
                + "1. Заявление на усыновление: Шаблон заявления приведен по ссылке: https://v-dobrie-ruki.ru/informacionnyj-razdel/zoopravo/shablon-dogovora-o-peredache-zhivotnyh-1-21 \n" +
                "2. Документы, удостоверяющие личность: Паспорт гражданина Казахстана или иной документ, удостоверяющий личность.\n" +
                "3. Документы о месте жительства: Справка о регистрации по месту жительства. Возможно, приют захочет удостовериться, что у вас достаточно места для содержания животного.\n" +
                "4. Справка о доходах (Это может быть необходимо для обеспечения достойного ухода за животным). \n" +
                "5. Согласие членов семьи (Если у вас есть члены семьи, они также должны дать согласие на усыновление).\n" +
                "6. Фотографии места проживания (Это делается для того, чтобы удостовериться, что условия будут комфортными для животного).\n" +
                "7. Справка от ветеринара (Если у вас уже есть другие домашние животные, приют может запросить справку от ветеринара об их здоровье и прививках). \n" +
                "8. После предоставления всех документов (п.1-7) необходимо будет подписать Обязательство об уходе за животным (Обязательство о том, что вы обеспечите должный уход за питомцем).");
        message.setReplyMarkup(menuBot.sendSubmenu2());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
    }

    private void getRecommendations(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Здесь вы получите рекомендации по интересующим вас темам в списке меню");
        message.setReplyMarkup(menuBot.sendSubmenu3());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
    }

    private void getRecommendationsAnimalTransportation(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Рекомендации по транспортировке питомца Вы получите по этой ссылке: \n" +
                "https://vk.com/wall-53030854_73978");
        message.setReplyMarkup(menuBot.sendSubmenu3());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
    }

    private void getRecommendationsHomeImprovement(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Рекомендации по обустройству дома Вы получите по этой ссылке: \n" +
                "https://greenvector.media/materials/kak-podgotovit-dom-k-jivotnomu");
        message.setReplyMarkup(menuBot.sendSubmenu3());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
    }

    private void getRecommendationsHomeImprovementForAdult(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Рекомендации по обустройству дома для взрослого питомца Вы получите дополнительно по этой ссылке: \n" +
                "https://journal.tinkoff.ru/list/pet-interior/");
        message.setReplyMarkup(menuBot.sendSubmenu3());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
    }

    private void getRecommendationsHomeImprovementForDisabledPet(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Рекомендации по обустройству дома для взрослого питомца Вы получите дополнительно по этой ссылке: \n" +
                "https://translated.turbopages.org/lifestyle/four-ways-home-comfortable-pet-140838680.html");
        message.setReplyMarkup(menuBot.sendSubmenu3());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
    }

    private void getAdviceFromDogHandler(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Cоветы кинолога по первичному общению с собакой можно получить по этой ссылке: \n" +
                "https://www.dogfriend.org/nk-lexikon/new-cynology/communication/make-a-bond/");
        message.setReplyMarkup(menuBot.sendSubmenu2());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
    }

    private void getDogHandlerContacts(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Мной дан список проверенных кинологов для общения с ними:\n" +
                "1. Алексей, 43 года. Стаж: 20 лет. Контактные данные:\n" +
                "");
        sendToDogHandler(String.valueOf(chatId), text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
    }

    private void sendToDogHandler(String chatId, String text) {
        final String ADMIN_ID = String.valueOf(934262991);
        try {
            execute(new SendMessage(ADMIN_ID, "Новое обращение от @" + chatId + ": " + text));
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
    }

    private void getReasonsForRefusal(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Список причин, почему могут отказать и не дать забрать собаку из приюта: \n" +
                "1. Отказ обеспечить безопасность питомца на новом месте. \n" +
                "2. Нестабильные отношения в семье. \n" +
                "3. Антинаучное мышление. \n" +
                "4. Наличие дома большого количества животных.\n" +
                "5. Маленькие дети в семье.\n" +
                "6. Аллергия.\n" +
                "7. Животное забирают в подарок кому-то. \n" +
                "8. Животное забирают в целях использования его рабочих качеств.\n" +
                "9. Отказ приехать познакомиться с животным.\n" +
                "10. Претендент — пожилой человек, проживающий один.\n" +
                "11. Отсутствие регистрации и собственного жилья или его несоответствие нормам приюта.\n" +
                "12. Без объяснения причин.\n" +
                "Такое тоже бывает, потому что не всегда удобно сказать человеку о своих подозрениях и сомнениях. \n" +
                "Простой пример: к будущим хозяевам черных кошек, особенно перед Хеллоуином, присматриваются особенно пристально.");
        message.setReplyMarkup(menuBot.sendSubmenu2());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
    }

    private void callToVolunteer(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Запрос отправлен волонтеру.");
        sendToVolunteer(String.valueOf(chatId), text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
    }

    private void sendToVolunteer(String chatId, String text) {
        final String ADMIN_ID = String.valueOf(934262991);
        try {
            execute(new SendMessage(ADMIN_ID, "Новое обращение от @" + chatId + ": " + text));
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
    }


    private void writeIncorrectText(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Не понял. Давайте попробуем снова. \" +\n" +
                "Что бы вы хотели сделать? Выберете пункт из /menu");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
    }

/*
    private void handleNewRequest(long chatId, String text) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);



        if (text.equalsIgnoreCase("/start")) {
            // Первое приветствие
            execute(new SendMessage(chatId, "Привет! \uD83D\uDC4B Я бот, который поможет вам взаимодействовать с приютом," +
                    "где бездомные животные находят заботу, уход, безопасность и надежду на новый дом. \n" +
                    "Я могу рассказать вам о приюте, о его питомцах, как помочь питомцу найти свой дом, какие документы для этого необходимы и многое другое. \n" +
                    "Жми скорее /menu"));
            message.setReplyMarkup(menuBot.sendMainMenu());
        } else {
            message.setText("Не понял. Давайте попробуем снова. " +
                    "Что бы вы хотели сделать?");
            message.setReplyMarkup(menuBot.sendMainMenu());
        }
    }


 */
/*
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

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
                        execute(new SendMessage(chatId, "Привет! \uD83D\uDC4B Я бот, который поможет вам взаимодействовать с приютом," +
                                "где бездомные животные находят заботу, уход, безопасность и надежду на новый дом. \n" +
                                "Я могу рассказать вам о приюте, о его питомцах, как помочь питомцу найти свой дом, какие документы для этого необходимы и многое другое. \n" +
                                "Жми скорее /menu"));

                    } else if ("/menu".equals(text)) {
                        sendMessage = new SendMessage(chatId, "выберите услугу");
                        // Добавляем ReplyMarkup к SendMessage
                        sendMessage.setReplyMarkup(menuBot.sendMainMenu());
                        // Используем execute из TelegramLongPollingBot
                        execute(sendMessage);
                    } else if ("Информация о приюте".equals(text)) {
                        // "Кнопка 1"
                        // Создаем новый объект SendMessage здесь:
                        sendMessage = new SendMessage(chatId, "Завести питомца — это очень серьезный шаг и здесь необходимо всё обдумать наперед!\n" +
                                "Мы приют животных из Астаны, и в данном разделе меню, ты можешь найти необходимую информацию о нас.");
                        sendMessage.setReplyMarkup(menuBot.sendSubmenu1());
                        execute(sendMessage);
                    } else if ("Расписание и адрес приюта".equals(text)) {
                        sendMessage = new SendMessage(chatId, "Наш приют расположен по адресу: г. Красноярск, Советский проспет, д.16.\n" +
                                "Расписание работы приюта:\n" +
                                " - [Понедельник - Пятница: 9:00 - 18:00].\n" +
                                " - [Суббота - Воскресенье: 10:00 - 17:00].\n" +
                                "Чтобы попасть на территорию приюта, необходимо получить пропуск у охраны по предварительной записи.\n" +
                                "Контактные данные охраны: +7-921-911-19-19.");
                        sendMessage.setReplyMarkup(menuBot.sendSubmenu1());
                        execute(sendMessage);
                    } else if ("Оформление пропуска и схема проезда".equals(text)) {

                        sendMessage = new SendMessage(chatId, "Для оформления пропуска необходимо при себе иметь паспорт.\n" +
                                "После оформления пропуска Вам необходимо пройти в здание 16Д: Схема проезда указана на фото");
sendPhoto(chatId, "asd", "C:/Users/Анна/IdeaProjects/telegramBotAnimal/target/classes/static/123.jpg");
                        //sendPhoto(chatId, "asd", "static/123.jpg");
                        sendMessage.setReplyMarkup(menuBot.sendSubmenu1());
                        execute(sendMessage);
//telegramBotAnimal/src/main/resources/static/123.jpg
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
                        sendMessage = new SendMessage(chatId, "Запрос отправлен волонтеру.");
                        execute(sendMessage);
                        sendToVolunteer(chatId, text);                    }
                }
            }
        } catch (TelegramApiException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleNewRequest(long chatId, String text) {
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

 */

    @Override
    public String getBotToken() {
        return animal.getToken();
    }

    @Override
    public String getBotUsername() {
        return animal.getName();
    }
}


