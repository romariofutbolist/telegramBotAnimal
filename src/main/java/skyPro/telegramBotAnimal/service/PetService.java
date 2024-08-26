package skyPro.telegramBotAnimal.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import skyPro.telegramBotAnimal.model.Pet;
import skyPro.telegramBotAnimal.exceptions.RecordNotFoundException;
import skyPro.telegramBotAnimal.repository.PetRepository;

import java.util.List;


@Service
public class PetService {

    private final Logger logger = LoggerFactory.getLogger(PetService.class);
    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Pet addPet(Pet pet) {
        logger.info("Питомец добавлен");
        return petRepository.save(pet);
    }

    public Pet getPetById(long id) {
        try {
            logger.info("Питомец был найден, id =" + id);
            return petRepository.findById(id).orElseThrow(RecordNotFoundException::new);
        } catch (RecordNotFoundException e) {
            logger.error("Питомец с id =" + id + " не найден");
            throw e;
        }
    }

    public Pet updatePet(Pet pet) {
        logger.info("Данные о питомце обновлены");
        return petRepository.findById(pet.getId())
                .map(entity -> petRepository.save(pet))
                .orElse(null);
    }

    public boolean deletePet(long id) {
        logger.info("Питомец с id = {} был удален" + id);
        return petRepository.findById(id)
                .map(entity -> {
                    petRepository.delete(entity);
                    return true;
                }).orElse(false);
    }

    public List<Pet> getAll() {
        return petRepository.findAll();
    }



}


