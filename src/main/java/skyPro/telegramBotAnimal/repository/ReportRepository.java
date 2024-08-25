package skyPro.telegramBotAnimal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import skyPro.telegramBotAnimal.model.PetReport;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<PetReport, Long> {

    @Query(value = "select * from pet_report where data >= DATE('now', '-2 days')", nativeQuery = true)
    List<PetReport> getOwnersAfterTwoDaysReport();

}
