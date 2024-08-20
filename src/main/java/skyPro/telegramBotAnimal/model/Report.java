package skyPro.telegramBotAnimal.model;

import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "report")
public class Report {
@Id
    @Column(name = "chat_Id")
    private Long chatId;

    @Column(name = "text")
    private String text;

    @Lob
    @Column(name = "photo")
    private byte[] photo;



    public Report() {
    }

    public Report(Long chatId, String text, byte[] photo) {
        this.chatId = chatId;
        this.text = text;
        this.photo = photo;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getWords() {
        return text;
    }

    public void setWords(String text) {
        this.text = text;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(chatId, report.chatId) && Objects.equals(text, report.text) && Arrays.equals(photo, report.photo);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(chatId, text);
        result = 31 * result + Arrays.hashCode(photo);
        return result;
    }

    @Override
    public String toString() {
        return "Report{" +
                "chatId=" + chatId +
                ", words='" + text + '\'' +
                ", photo=" + Arrays.toString(photo) +
                '}';
    }
}
