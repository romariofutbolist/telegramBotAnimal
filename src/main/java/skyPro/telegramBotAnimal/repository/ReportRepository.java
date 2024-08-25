package skyPro.telegramBotAnimal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import skyPro.telegramBotAnimal.model.PetReport;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<PetReport, Long> {
    //void findNotificationTasksByTaskDate(LocalDateTime localDateTime);
    //List<PetReport> findNotificationTasks(LocalDateTime time);

    //List<PetReport> get(long id);
    //List<PetReport> getAll();
}
