package skyPro.telegramBotAnimal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import skyPro.telegramBotAnimal.model.User;

public interface UserRepository extends JpaRepository <User, Long> {
    User findByChatId(long chatId);
}
