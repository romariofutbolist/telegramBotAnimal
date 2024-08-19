package skyPro.telegramBotAnimal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import skyPro.telegramBotAnimal.model.Pet;
import skyPro.telegramBotAnimal.model.PetReport;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    //List<PetReport>findNotificationTasksByTaskDate(LocalDateTime time);

}
