package skyPro.telegramBotAnimal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TelegramBotAnimalApplication {

	public static void main(String[] args)	 {
		SpringApplication.run(TelegramBotAnimalApplication.class, args);
	}

}
