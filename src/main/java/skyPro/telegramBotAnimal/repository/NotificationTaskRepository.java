package skyPro.telegramBotAnimal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import skyPro.telegramBotAnimal.model.NotificationTask;

public interface NotificationTaskRepository extends JpaRepository <NotificationTask, Long> {
}
