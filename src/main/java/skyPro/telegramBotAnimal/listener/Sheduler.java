package skyPro.telegramBotAnimal.listener;

import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import skyPro.telegramBotAnimal.model.PetReport;
import skyPro.telegramBotAnimal.model.User;
import skyPro.telegramBotAnimal.repository.ReportRepository;
import com.pengrad.telegrambot.TelegramBot;
import skyPro.telegramBotAnimal.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;

@Service
public class Sheduler {

    private static final Logger log = LoggerFactory.getLogger(Sheduler.class);
    private final TelegramBot telegramBot;

    public Sheduler(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }


    @Scheduled(cron = "0 0 21 * * *")
    public void volunteerRun() {
        final String ADMIN_ID = String.valueOf(1063364663);
        try {
            telegramBot.execute(new SendMessage(ADMIN_ID, "Время смотреть отчеты"));
        } catch (RuntimeException e) {
            log.error("ADMIN_ID does`t used");
        }
    }



}



