package skyPro.telegramBotAnimal.check;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import skyPro.telegramBotAnimal.model.Photo;
import skyPro.telegramBotAnimal.repository.PhotoRepository;
import skyPro.telegramBotAnimal.service.TelegramBotService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class CheckReport {
    private static final Logger log = LoggerFactory.getLogger(CheckReport.class);
    private final TelegramBotService telegramBotService;
    private final PhotoRepository photoRepository;

    public CheckReport(TelegramBotService telegramBotService, PhotoRepository photoRepository) {
        this.telegramBotService = telegramBotService;

        this.photoRepository = photoRepository;
    }

    //@Scheduled(cron = "0 0 21   ")
    @Scheduled(cron = "0 0/1 * * * *")
    public void checkReport() {
        try {
            log.info("Проверка отчетов началась.");
            LocalDateTime today = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
            List<Photo> photos = photoRepository.findPhotoByDate(today);
            log.info("Найдено {} фотографий.", ((List<?>) photos).size());

            for (Photo photo : photos) {
                if (photo.getText() == null || photo.getFileId() == null) {
                    log.info("Отчет за {} отсутствует, отправляем сообщение.", today);
                    telegramBotService.execute(new SendMessage(String.valueOf(photo.getChatId()), "В настоящий момент отчет за сегодняшний день не получен"));
                } else {
                    log.info("Отчет за {} найден.", today);
                }
            }
        } catch (Exception e) {
            log.error("Произошла ошибка при проверке отчетов.", e);
        }
    }
}
