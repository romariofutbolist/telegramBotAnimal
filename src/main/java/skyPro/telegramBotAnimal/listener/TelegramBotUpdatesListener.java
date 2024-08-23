package skyPro.telegramBotAnimal.listener;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import skyPro.telegramBotAnimal.configuration.ConfigurationAnimal;
import skyPro.telegramBotAnimal.model.MenuBot;
import skyPro.telegramBotAnimal.model.Photo;
import skyPro.telegramBotAnimal.model.User;
import skyPro.telegramBotAnimal.model.Pet;
import skyPro.telegramBotAnimal.repository.PetRepository;
import skyPro.telegramBotAnimal.repository.PhotoRepository;
//import skyPro.telegramBotAnimal.repository.ReportRepository;
import skyPro.telegramBotAnimal.repository.UserRepository;
import skyPro.telegramBotAnimal.service.PetService;
import skyPro.telegramBotAnimal.service.UserService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TelegramBotUpdatesListener extends TelegramLongPollingBot {
    private Map<Long, String> userStates = new HashMap<>(); //
    private Map<Long, Integer> incorrectCounts = new HashMap<>(); //
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\+7-9\\d{2}-\\d{3}-\\d{2}-\\d{2}");
    @Autowired
    private PetService petService;
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final ConfigurationAnimal animal;
//    private final Pet pet;

    private final UserRepository repository;
    private final PetRepository petRepository;
   // private final ReportRepository reportRepository;
    private final PhotoRepository photoRepository;
    private final MenuBot menuBot;


    public TelegramBotUpdatesListener(ConfigurationAnimal animal, UserRepository repository, MenuBot menuBot, UserService userService, PetRepository petRepository, PhotoRepository photoRepository) {
        this.animal = animal;
        this.repository = repository;
        this.menuBot = menuBot;
        this.userService = userService;
        this.petRepository = petRepository;
    //    this.reportRepository = reportRepository;
        this.photoRepository = photoRepository;
    }

    @PostConstruct
    public void init() throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        String text = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        String login = update.getMessage().getFrom().getUserName();
        //List<PhotoSize> photo = update.getMessage().getPhoto();
        var state = userStates.get(chatId);
        var user = userService.findByUser(chatId);

        if (update.hasMessage() && update.getMessage().hasPhoto()) {
            Message message = update.getMessage();
            PhotoSize[] photos = update.getMessage().getPhoto().toArray(new PhotoSize[0]);
            PhotoSize lastPhoto = photos[photos.length - 1]; // Получаем фото
            String fileId = lastPhoto.getFileId(); // Получаем ID фото
            String caption = message.getCaption();
//            String caption = message.setCaption() ? message.getCaption() : "";
//            String text1 = message.getText();

            // Загружаем файл фото с сервера Telegram
            GetFile getFile = new GetFile();
            getFile.setFileId(fileId);

            // тут что-то с подписью


            // Сохраняем фото в базу данных

            Photo photoEntity = new Photo(); // Созд. объект
            // Заполнение поля объекта
            photoEntity.setFileId(fileId);
            photoEntity.setText(caption);  // Сохр. текст
            photoEntity.setChatId(chatId); // Сохр. чат ID пользователя
            photoEntity.setLogin(login); // Сохр. логин пользователя

            // Сохранение фото в базе данных

            photoRepository.save(photoEntity);
            sendMessage(chatId, "Отчет сохранен.");






//            try (FileOutputStream outputStream = new FileOutputStream(filePathPet)) {
//             //   outputStream.write(fileContent);
//                outputStream.write(getFile.getFileContent());
//            }  catch (IOException e) {
//                throw new RuntimeException(e);
//            }


        }else if (update.hasMessage() && update.getMessage().hasText()) {
//            String text = update.getMessage().getText();
//            long chatId = update.getMessage().getChatId();
//            String login = update.getMessage().getFrom().getUserName();
//            var state = userStates.get(chatId);
//            var user = userService.findByUser(chatId);
            if ("PhoneListener".equals(state)) {
                handleContactInput(chatId, text);
                userStates.remove(chatId);
            } else {
                switch (text) {
                    case "/start":
                        if (user == null) {
                            startCommandReceived(chatId, update.getMessage().getChat().getFirstName(), login);
                            break;
                        }
                    case "/menu":
                        menu(chatId);
                        break;
                    case "Информация о приюте":
                        getInformationAboutShelter(chatId);
                        break;
                    case "Расписание и адрес приюта":
                        getAdressOfShelter(chatId);
                        break;
                    case "Оформление пропуска и схема проезда":
                        IssuePassAndGetDrivingDirections(chatId);
                        break;
                    case "Техника безопасности":
                        getSafetyEquipment(chatId);
                        break;
                    case "Запросить связь":
                        writeDownContactPhoneNumber(chatId);
                        break;
                    case "Назад":
                        goBack(chatId);
                        break;
                    case "Как взять животное из приюта":
                        takeAnimalFromShelter(chatId);
                        break;
                    case "Список животных":
                        getShowPets(chatId);
                        break;
                    case "Правила знакомства и усыновления":
                        getRulesOfBehaviorAtShelter(chatId);
                        break;
                    case "Список необходимых документов":
                        provideListOfDocuments(chatId);
                        break;
                    case "Рекомендации":
                        getRecommendations(chatId);
                        break;
                    case "Транспортировка животного":
                        getRecommendationsAnimalTransportation(chatId);
                        break;
                    case "Обустройство дома":
                        getRecommendationsHomeImprovement(chatId);
                        break;
                    case "Обустройство дома для взрослого питомца":
                        getRecommendationsHomeImprovementForAdult(chatId);
                        break;
                    case "Обустройство дома для питомца с ограниченными возможностями":
                        getRecommendationsHomeImprovementForDisabledPet(chatId);
                        break;
                    case "Вернуться":
                        toReturn(chatId);
                        break;
                    case "Советы кинолога":
                        getAdviceFromDogHandler(chatId);
                        break;
                    case "Проверенные кинологи":
                        getDogHandlerContacts(chatId);
                        break;
                    case "Причины отказа":
                        getReasonsForRefusal(chatId);
                        break;
                    case "Позвать волонтера":
                        callToVolunteer(chatId);
                        break;
                    case "Прислать отчет о питомце":
                        sendPetReport(chatId);
                        break;
                    case "Форма ежедневного отчета":
                        sendDailyReportForm(chatId);
                        break;
                    default:
                        var count = incorrectCounts.getOrDefault(chatId, 0);
                        if (count < 2) {
                            incorrectCounts.put(chatId, count + 1);
                            writeIncorrectText(chatId);
                        } else {
                            writeIncorrectText2(chatId);
                        }
                }
            }
        }
    }

    /*
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
     */
    //Основная команда - /start
    private void startCommandReceived(long chatId, String name, String login) {
        var user = new User();
        user.setChatId(chatId);
        user.setName(name);
        user.setId(chatId);
        user.setId(user.getId());
        user.setLogin(login);
        repository.save(user);
        sendMessage(chatId, "Привет, " + name + ". Я бот, который поможет вам взаимодействовать с приютом,где бездомные животные находят заботу, уход, безопасность и надежду на новый дом." +
                "\n" + "Я могу рассказать вам о приюте, о его питомцах, как помочь питомцу найти свой дом, какие документы для этого необходимы и многое другое." +
                "\n" + "Жми скорее /menu");
    }

    //Метод, помогающий вывести сообщение пользователю
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

    //Метод, помогающий вывести сообщение пользователю, а также кнопки меню
    private void sendMessage2(long chatId, String text, ReplyKeyboard replyMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setReplyMarkup(replyMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException();
        }
    }

    //Кнопка 1: меню
    private void menu(long chatId) {
        String text = "выберите услугу";
        sendMessage2(chatId, text, menuBot.sendMainMenu());
    }

    //Кнопка 1.1: Информация о приюте
    private void getInformationAboutShelter(long chatId) {
        String text = "Завести питомца — это очень серьезный шаг и здесь необходимо всё обдумать наперед!\n" +
                "Мы приют животных из Астаны, и в данном разделе меню, ты можешь найти необходимую информацию о нас.";
        sendMessage2(chatId, text, menuBot.sendSubmenu1());
    }

    //Кнопка 1.1.1: Расписание и адрес приюта
    private void getAdressOfShelter(long chatId) {
        String text = "Наш приют расположен по адресу: г. Красноярск, Советский проспет, д.16.\n" +
                "Расписание работы приюта:\n" +
                " - [Понедельник - Пятница: 9:00 - 18:00].\n" +
                " - [Суббота - Воскресенье: 10:00 - 17:00].\n" +
                "Чтобы попасть на территорию приюта, необходимо получить пропуск у охраны по предварительной записи.\n" +
                "Контактные данные охраны: +7-921-911-19-19.";
        sendMessage2(chatId, text, menuBot.sendSubmenu1());
    }

    //Кнопка 1.1.2: Оформление пропуска и схема проезда
    private void IssuePassAndGetDrivingDirections(long chatId) {
        String text = "Для оформления пропуска необходимо при себе иметь паспорт.\n" +
                "После оформления пропуска Вам необходимо пройти в здание 16Д: Схема проезда указана на фото";
        sendMessage2(chatId, text, menuBot.sendSubmenu1());
        sendPhoto(chatId);
    }

    //Метод, позволяющий отправить пользователю картинку - схема проезда
    //    C:/Users/Анна/IdeaProjects/telegramBotAnimal/target/classes/static/123.jpg
    //    /home/roma/telegramBotAnimal/target/classes/static/123.jpg
    //    /Users/denis/IdeaProjects/telegramBotAnimal/src/main/resources/123.jpg
    public void sendPhoto(long chatId) {
        String imagePath = "C:/Users/Анна/IdeaProjects/telegramBotAnimal/target/classes/static/123.jpg";
        File imageFile = new File(imagePath);
        InputFile photo = new InputFile(imageFile);
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(photo);
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
    }

    //Кнопка 1.1.3: Техника безопасности
    private void getSafetyEquipment(long chatId) {
        String text = "Вот некоторые правила техники безопасности в приюте для животных:\n" +
                "1. Проявляйте терпение и уважение к сотрудникам, волонтерам и другим посетителям.\n" +
                "2. Любые действия в приюте совершаются с разрешения работников или руководства.\n" +
                "3. На территории приюта не кричите, не размахивайте руками, не бегайте между будками или вольерами, не пугайте и не дразните животных.\n" +
                "4. Запрещается посещение приюта в состоянии алкогольного, наркотического опьянения.\n" +
                "5. Запрещается самостоятельно открывать вольеры и выводить животное без разрешения сотрудника приюта.\n" +
                "6. Запрещается подходить близко к вольерам и гладить собак через сетку на выгулах.\n" +
                "7. Запрещается допускать близкий контакт между собаками во время выгула во избежание драк.\n" +
                "8. Запрещается отпускать животных с поводка.\n" +
                "9. Разрешается гулять только на отведенной территории, о которой сообщит работник приюта.\n" +
                "При несоблюдении правил сотрудники приюта оставляют за собой право отказать посетителю в посещении приюта.";
        sendMessage2(chatId, text, menuBot.sendSubmenu1());
    }

    //Кнопка 1.1.4: Запросить связь
    //Кнопка 2.2.8: Запросить связь
    private void writeDownContactPhoneNumber(long chatId) {
        String text = "Я могу записать Ваши контактные данные и в ближайшее время с Вами свяжется наш волонтер и проконсультируют Вас. " +
                "Введите номер телефона";
        sendMessage(chatId, text);
        userStates.put(chatId, "PhoneListener");
    }

    //Метод, определяющий правильность номера телефона и позволяющий записать контактные данные в БД при корректном их написании
    private void handleContactInput(Long chatId, String text) {
        Matcher matcher = PHONE_PATTERN.matcher(text);
        if (matcher.matches()) {
            var task = repository.findByChatId(chatId);
            task.setPhone(text);
            repository.save(task);
            sendMessage(chatId, "Номер телефона успешно сохранен! Нажмите кнопку /menu");
        } else {
            sendMessage(chatId, "Неверный формат номера телефона. Пожалуйста, введите номер в формате:" + "+7-9**-**-**");
        }
    }

    //Кнопка 1.2: Как взять животное из приюта
    private void takeAnimalFromShelter(long chatId) {
        String text = "В данном разделе я помогу тебе с выбором твоего будущего друга, " +
                "дам список необходимых документов, чтобы забрать питомца из приюта, " +
                "дам список рекомендаций по транспортиовке и обустройству дома для питомца " +
                "и предоставлю контактные данные кинологов для получения советов по общению с питомцем";
        sendMessage2(chatId, text, menuBot.sendSubmenu2());
    }

    //Кнопка 1.2.1: Список животных
    public void getShowPets(long chatId) {
        List<Pet> pets = petService.getAll();
        StringBuilder petsInfo = new StringBuilder("Наши питомцы:\n");

        // Получите список доступных животных
        Collection<Pet> availablePets = petRepository.getAvailableAnimals();

        // Создайте сообщение о доступных животных
        for (Pet pet : availablePets) {
            petsInfo.append(pet.getName()).append("\n");
        }

        // Отправьте сообщение в Telegram
        sendMessage(chatId, petsInfo.toString());
    }


//    public void getShowPets(long chatId) {
//        List<Pet> pets = petService.getAll();
//        StringBuilder petsInfo = new StringBuilder("Наши питомцы:\n");
//        return petRepository.getAvailableAnimals();
//
//        // Проход по каждому питомцу и добавление информации о нем в строку сообщения
//
//
//        for (Pet pet : pets) {
//            // Проверяем, есть ли значение в chatId
//            if (pet.getUserId() == null) {
//                petsInfo.append("Имя: ").append(pet.getName()).append("\n")
//                        .append("Порода: ").append(pet.getBreed()).append("\n")
//                        .append("Возраст: ").append(pet.getAge()).append("\n\n");
//
//        }
//
//
//        // Отправка сообщения в Telegram
//        sendMessage(chatId, petsInfo.toString());
//    }

    //Кнопка 1.2.2: Правила знакомства и усыновления
    private void getRulesOfBehaviorAtShelter(long chatId) {
        String text = "Вот Вам несколько ссылок для ознакомления. Здесь вы сможете найти необходимую для вас информацию:\n"
                + "https://adme.media/articles/10-sovetov-kotorye-pomogut-podruzhitsya-s-neznakomoj-sobakoj-2509006/:\n" +
                "https://www.mk.ru/social/2020/08/15/kak-vesti-sebya-s-zhivotnymi-iz-priyuta-pyat-osnovnykh-pravil.html";
        sendMessage2(chatId, text, menuBot.sendSubmenu2());
    }

    //Кнопка 1.2.3: Список необходимых документов
    private void provideListOfDocuments(long chatId) {
        String text = "Если вы решились обзавестись новым членом семьи, ниже представлен список основных шагов и документов на собаку или кошку из приюта, которые у вас могут запросить: \n"
                + "1. Заявление на усыновление: Шаблон заявления приведен по ссылке: https://v-dobrie-ruki.ru/informacionnyj-razdel/zoopravo/shablon-dogovora-o-peredache-zhivotnyh-1-21 \n" +
                "2. Документы, удостоверяющие личность: Паспорт гражданина Казахстана или иной документ, удостоверяющий личность.\n" +
                "3. Документы о месте жительства: Справка о регистрации по месту жительства. Возможно, приют захочет удостовериться, что у вас достаточно места для содержания животного.\n" +
                "4. Справка о доходах (Это может быть необходимо для обеспечения достойного ухода за животным). \n" +
                "5. Согласие членов семьи (Если у вас есть члены семьи, они также должны дать согласие на усыновление).\n" +
                "6. Фотографии места проживания (Это делается для того, чтобы удостовериться, что условия будут комфортными для животного).\n" +
                "7. Справка от ветеринара (Если у вас уже есть другие домашние животные, приют может запросить справку от ветеринара об их здоровье и прививках). \n" +
                "8. После предоставления всех документов (п.1-7) необходимо будет подписать Обязательство об уходе за животным (Обязательство о том, что вы обеспечите должный уход за питомцем).";
        sendMessage2(chatId, text, menuBot.sendSubmenu2());
    }

    //Кнока 1.2.4: Рекомендации
    private void getRecommendations(long chatId) {
        String text = "Здесь вы получите рекомендации по интересующим вас темам в списке меню";
        sendMessage2(chatId, text, menuBot.sendSubmenu3());
    }

    //Кнока 1.2.4.1: Транспортировка животного
    private void getRecommendationsAnimalTransportation(long chatId) {
        String text = "Рекомендации по транспортировке питомца Вы получите по этой ссылке: \n" +
                "https://vk.com/wall-53030854_73978";
        sendMessage2(chatId, text, menuBot.sendSubmenu3());
    }

    //Кнока 1.2.4.2: Обустройство дома
    private void getRecommendationsHomeImprovement(long chatId) {
        String text = "Рекомендации по обустройству дома Вы получите по этой ссылке: \n" +
                "https://greenvector.media/materials/kak-podgotovit-dom-k-jivotnomu";
        sendMessage2(chatId, text, menuBot.sendSubmenu3());
    }

    //Кнока 1.2.4.3: Обустройство дома для взрослого питомца
    private void getRecommendationsHomeImprovementForAdult(long chatId) {
        String text = "Рекомендации по обустройству дома для взрослого питомца Вы получите дополнительно по этой ссылке: \n" +
                "https://journal.tinkoff.ru/list/pet-interior/";
        sendMessage2(chatId, text, menuBot.sendSubmenu3());
    }

    //Кнока 1.2.4.4: Обустройство дома для питомца с ограниченными возможностями
    private void getRecommendationsHomeImprovementForDisabledPet(long chatId) {
        String text = "Рекомендации по обустройству дома для взрослого питомца Вы получите дополнительно по этой ссылке: \n" +
                "https://translated.turbopages.org/lifestyle/four-ways-home-comfortable-pet-140838680.html";
        sendMessage2(chatId, text, menuBot.sendSubmenu3());
    }

    //Кнока 1.2.5: Советы кинолога
    private void getAdviceFromDogHandler(long chatId) {
        String text = "Cоветы кинолога по первичному общению с собакой можно получить по этой ссылке: \n" +
                "https://www.dogfriend.org/nk-lexikon/new-cynology/communication/make-a-bond/";
        sendMessage2(chatId, text, menuBot.sendSubmenu2());
    }

    //Кнока 1.2.6: Проверенные кинологи
    private void getDogHandlerContacts(long chatId) {
        String text = "Мной дан список проверенных кинологов для общения с ними:\n" +
                "1. Алексей, 43 года. Стаж: 20 лет. Контактные данные:+7-923-232-34-54. \n" +
                "2. Георгий, 30 лет. Стаж: 7 лет. Контактные данные:+7-923-555-30-90. \n" +
                "3. Юлия, 26 лет. Стаж: 3 года. Контактные данные:+7-923-987-78-79. \n";
        sendMessage(chatId, text);
        //sendToDogHandler(String.valueOf(chatId), text);
    }

    /*
    private void sendToDogHandler(String chatId, String text) {
        final String ADMIN_ID = String.valueOf(934262991);
        try {
            execute(new SendMessage(ADMIN_ID, "Новое обращение от @" + chatId + ": " + text));
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
    }
     */
    //Кнока 1.2.7: Причины отказа
    private void getReasonsForRefusal(long chatId) {
        String text = "Список причин, почему могут отказать и не дать забрать собаку из приюта: \n" +
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
                "Простой пример: к будущим хозяевам черных кошек, особенно перед Хеллоуином, присматриваются особенно пристально.";
        sendMessage2(chatId, text, menuBot.sendSubmenu2());
    }

    //Кнопка 1.3: Прислать отчет о питомце
    private void sendPetReport(long chatId) {
        String text = "Мы очень рады, что у нашего питомца появился новый друг! Но тем не менее мы переживаем за наших питомцев." +
                "Мы хотели бы чтобы ты оповещал нас о состоянии питомца на протяжении 30 дней." +
                "В данном разделе ты можешь получить образец формы ежедневного отчета, на основании, которого ты можешь нас оповещать о состоянии питомца";
        sendMessage2(chatId, text, menuBot.sendSubmenu4());
    }

    //Кнопка 1.3.1: Форма ежедневного отчета
    private void sendDailyReportForm(long chatId) {
        String text = "В ежедневный отчет входит следующая информация: \n" +
                "\n" +
                "- *Фото животного.*\n" +
                "(Фотография и текст должны быть одним сообщением)" +
                "- *Рацион животного.*\n" +
                "- *Общее самочувствие и привыкание к новому месту.*\n" +
                "- *Изменения в поведении: отказ от старых привычек, приобретение новых.*\n" +
                "\n" +
                "Отчет нужно присылать каждый день, ограничений в сутках по времени сдачи отчета нет. Каждый день после 21:00 волонтеры отсматривают все присланные отчеты," +
                " и в случае некорректного заполнения тебе в телеграмм поступит напоминание от бота о правильности заполнения отчета.\n" +
                "\n" +
                "Если ты не будешь присылать ежедневный отчет, то по истечении 2 дней волонтеры будут обязаны самолично проверять условия содержания животного. \n" +
                "Как только период в 30 дней заканчивается, волонтеры принимают решение о том, остается животное у хозяина или нет. Испытательный срок может быть пройден, может быть продлен на срок еще 14 или 30 дней, а может быть не пройден.";
        sendMessage2(chatId, text, menuBot.sendSubmenu4());
    }

    //Кнока 1.4: Позвать волонтера
    private void callToVolunteer(long chatId) {
        String text = "Запрос отправлен волонтеру.";
        sendMessage(chatId, text);
        sendToVolunteer(String.valueOf(chatId), text);
    }

    //Метод, помогающий передать необходимому волонтеру новое обращение
    private void sendToVolunteer(String chatId, String text) {
        final String ADMIN_ID = String.valueOf(934262991);
        try {
            execute(new SendMessage(ADMIN_ID, "Новое обращение от @" + chatId + ": " + text));
        } catch (TelegramApiException e) {
            throw new RuntimeException("ошибка");
        }
    }

    //Метод, определяющий правильность набора команды. Если команда неверная, то бот просит нажать на кнопку меню
    private void writeIncorrectText(long chatId) {
        String text = "Не понял. Давайте попробуем снова. \" +\n" +
                "Что бы вы хотели сделать? Выберете пункт из /menu";
        sendMessage(chatId, text);
    }

    //Метод, определяющий правильность набора команды. Если команда снова неверная, то бот спрашивает о возможном вызове волонтера и вызывает его
    private void writeIncorrectText2(long chatId) {
        String text = "Может тогда вызвать волонтера?";
        sendMessage(chatId, text);
        callToVolunteer(chatId);
    }

    //Кнопка НАЗАД - вернуться в меню из подпункта
    public void goBack(long chatId) {
        String text = "выберите услугу";
        sendMessage2(chatId, text, menuBot.sendMainMenu());
    }

    //Кнопка Вернуться в подпункт меню
    public void toReturn(long chatId) {
        String text = "В данном разделе я помогу тебе с выбором твоего будущего друга, " +
                "дам список необходимых документов, чтобы забрать питомца из приюта, " +
                "дам список рекомендаций по транспортиовке и обустройству дома для питомца " +
                "и предоставлю контактные данные кинологов для получения советов по общению с питомцем";
        sendMessage2(chatId, text, menuBot.sendSubmenu2());
    }


    @Override
    public String getBotToken() {
        return animal.getToken();
    }

    @Override
    public String getBotUsername() {
        return animal.getName();
    }
}