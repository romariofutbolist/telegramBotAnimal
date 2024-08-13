package skyPro.telegramBotAnimal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import skyPro.telegramBotAnimal.model.PetReport;

@Repository
public interface ReportRepository extends JpaRepository<PetReport, Long> {
}
