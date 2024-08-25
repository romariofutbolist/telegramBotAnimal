package skyPro.telegramBotAnimal.listener;

import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import skyPro.telegramBotAnimal.model.PetReport;
import skyPro.telegramBotAnimal.repository.ReportRepository;
import com.pengrad.telegrambot.TelegramBot;
import java.util.List;

@Service
public class Sheduler {

    private static final Logger log = LoggerFactory.getLogger(Sheduler.class);
    private final TelegramBot telegramBot;
    private final ReportRepository reportRepository;

    public Sheduler(TelegramBot telegramBot, ReportRepository reportRepository) {
        this.telegramBot = telegramBot;
        this.reportRepository = reportRepository;
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

    @Scheduled(cron = "0 0/59 * * * *")
    public void petReportRun() {
        List<PetReport> petReports = reportRepository.getOwnersAfterTwoDaysReport();
        if(petReports==null) {
            petReports.forEach(e ->
                    telegramBot.execute(new SendMessage(e.getUser().getChatId(), "Нужно прислать отчет")));
        }
    }

}



