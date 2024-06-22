package skyPro.telegramBotAnimal.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigaurationAnimal {





    @Value("${telegram.bot.token}")
    private String token;


}
