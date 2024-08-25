package skyPro.telegramBotAnimal.check;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import skyPro.telegramBotAnimal.model.Photo;
import skyPro.telegramBotAnimal.repository.PhotoRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class CheckReport {
    private static final Logger log = LoggerFactory.getLogger(CheckReport.class);
    private final TelegramBot telegramBot;
    private final PhotoRepository photoRepository;

    public CheckReport(TelegramBot telegramBot, PhotoRepository photoRepository) {
        this.telegramBot = telegramBot;
        this.photoRepository = photoRepository;
    }

    @Scheduled(cron = "0 0 21   ") // проверка в 9 вечера
    public void checkReport() {
        photoRepository.findPhotoByDate(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS))
                .forEach(photo -> {
                    telegramBot.execute(new SendMessage(photo.getChatId(), "Отчет не был получен"));
                    log.info("Message has been sent");
                });

    }
}


