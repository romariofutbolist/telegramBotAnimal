package skyPro.telegramBotAnimal.model;

import org.hibernate.annotations.Entity;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "petReport")
public class PetReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String textOfReport;
    private LocalDateTime data;


    private long userId;

    //    @Lob
    //@Column(columnDefinition = "MEDIUMBLOB")
    //private byte[] data;


    public PetReport(long id, String textOfReport, LocalDateTime data, long userId) {
        this.id = id;
        this.textOfReport = textOfReport;
        this.data = data;
        this.userId = userId;
    }

    public PetReport() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTextOfReport() {
        return textOfReport;
    }

    public void setTextOfReport(String textOfReport) {
        this.textOfReport = textOfReport;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetReport petReport = (PetReport) o;
        return id == petReport.id && userId == petReport.userId && Objects.equals(textOfReport, petReport.textOfReport) && Objects.equals(data, petReport.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, textOfReport, data, userId);
    }

    @Override
    public String toString() {
        return "PetReport{" +
                "id=" + id +
                ", textOfReport='" + textOfReport + '\'' +
                ", data=" + data +
                ", userId=" + userId +
                '}';
    }
}
