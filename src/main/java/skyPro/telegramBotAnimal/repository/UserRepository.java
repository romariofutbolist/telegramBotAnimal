package skyPro.telegramBotAnimal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import skyPro.telegramBotAnimal.model.Pet;
import skyPro.telegramBotAnimal.model.User;

import java.util.Collection;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {
    User findByChatId(long chatId);
    //Collection<User> findAllByPetId();
}
