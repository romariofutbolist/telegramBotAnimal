package skyPro.telegramBotAnimal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import skyPro.telegramBotAnimal.model.Report;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    //Optional<Report> findByChatId(Long chatId);
}
