package skyPro.telegramBotAnimal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import skyPro.telegramBotAnimal.model.Pet;
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

}
