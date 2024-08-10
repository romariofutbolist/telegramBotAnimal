package skyPro.telegramBotAnimal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "pets")

public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    //@Column(name = "id")
    private long id;
    //@Column(nullable = false)
    //@Column(name = "name")
    private String name;
    //@Column(nullable = false)
    //@Column(name = "breed")
    private String breed;
    //@Column(nullable = false)
    //@Column(name = "age")
     private int age;
    //@Column(nullable = false)
    //@Column(name = "food")
    private String food;
    //@Column
    //@Column(name = "shelter")
    private String shelter;

//    @JsonIgnore

    public Pet(long id, String name, String breed, int age, String food, String shelter) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.food = food;
        this.shelter = shelter;
    }
    public Pet(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getShelter() {
        return shelter;
    }

    public void setShelter(String shelter) {
        this.shelter = shelter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return id == pet.id && age == pet.age && Objects.equals(name, pet.name) && Objects.equals(breed, pet.breed) && Objects.equals(food, pet.food) && Objects.equals(shelter, pet.shelter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, breed, age, food, shelter);
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                ", age=" + age +
                ", food='" + food + '\'' +
                ", shelter='" + shelter + '\'' +
                '}';
    }
}
