package skyPro.telegramBotAnimal.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import skyPro.telegramBotAnimal.repository.PetRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Service
public class NotificationSheduled {

    //private static final Logger log = LoggerFactory.getLogger(NotificationScheduler.class);
    private final TelegramBot telegramBot;
    private final PetRepository repository;

    public NotificationSheduled(TelegramBot telegramBot, PetRepository repository) {
        this.telegramBot = telegramBot;
        this.repository = repository;
    }
}


    /*
    @Scheduled(cron = "0 0 21 * * *")
    public void volunteerRun() {
        List<Volunteer> volunteers = volunteerService.getAll();
        volunteers.forEach( e ->
                telegramBot.execute(new SendMessage(e.getChatId() , "Время смотреть отчеты"))
        );

    }

     */

