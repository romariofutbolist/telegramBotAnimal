package skyPro.telegramBotAnimal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import skyPro.telegramBotAnimal.model.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
