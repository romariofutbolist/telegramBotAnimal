package skyPro.telegramBotAnimal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import skyPro.telegramBotAnimal.model.Avatar;
import skyPro.telegramBotAnimal.model.PetReport;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    //void deleteAvatar(Long petReportId);
    //Optional<Avatar> findAll(Long petReportId);
}
