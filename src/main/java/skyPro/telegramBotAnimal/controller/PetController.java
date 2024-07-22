package skyPro.telegramBotAnimal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import skyPro.telegramBotAnimal.model.Pet;
import skyPro.telegramBotAnimal.service.PetService;

@RestController
@RequestMapping(path = "/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public Pet addPet(@RequestBody Pet pet) {
        return petService.addPet(pet);
    }

    @GetMapping
    public Pet getPetById(@RequestParam long id) {
        return petService.getPetById(id);
    }

    @PutMapping
    public Pet updatePet(@RequestBody Pet pet) {
        return petService.updatePet(pet);
    }

    @DeleteMapping
    public boolean deletePet(@RequestParam long id) {
        return petService.deletePet(id);
    }
}
