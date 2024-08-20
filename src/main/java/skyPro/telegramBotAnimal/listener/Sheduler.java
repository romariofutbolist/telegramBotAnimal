package skyPro.telegramBotAnimal.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import skyPro.telegramBotAnimal.repository.ReportRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class Sheduler {

    //private static final Logger log = LoggerFactory.getLogger(NotificationScheduler.class);
    private final TelegramBot telegramBot;
    private final ReportRepository reportRepository;

    public Sheduler(TelegramBot telegramBot, ReportRepository reportRepository) {
        this.telegramBot = telegramBot;
        this.reportRepository = reportRepository;
    }

}


