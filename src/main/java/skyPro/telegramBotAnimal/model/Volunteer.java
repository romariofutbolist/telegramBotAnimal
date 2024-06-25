package skyPro.telegramBotAnimal.model;

import java.util.Objects;

public class Volunteer {
    public String name;

    public Volunteer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Volunteer volunteer = (Volunteer) o;
        return Objects.equals(name, volunteer.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "Volunteer{" +
                "name='" + name + '\'' +
                '}';
    }
}
