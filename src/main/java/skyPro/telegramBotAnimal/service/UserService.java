package skyPro.telegramBotAnimal.service;

import org.springframework.stereotype.Service;
import skyPro.telegramBotAnimal.model.User;
import skyPro.telegramBotAnimal.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUser(long chatId) {
        return userRepository.findByChatId(chatId);
    }

}
