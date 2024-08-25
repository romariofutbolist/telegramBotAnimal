package skyPro.telegramBotAnimal.configuration;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
//import org.telegram.telegrambots.meta.api.methods.commands.DeleteMyCommands;
//import org.telegram.telegrambots.meta.generics.TelegramBot;

@Configuration
@Data
@PropertySource("application.properties")
public class ConfigurationAnimal {

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.name}")
    private String name;

    @Value("{telegram.bot.volunteer}")
    private String volunteer;

    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        return bot;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }
}
