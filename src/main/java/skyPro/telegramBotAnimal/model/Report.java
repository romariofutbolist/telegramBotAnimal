package skyPro.telegramBotAnimal.model;

import org.telegram.telegrambots.meta.api.objects.InputFile;
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

    @Column(name = "photo")
    private InputFile photo;

    private String fileName;
    private String fileType;

    public Report() {}

    public Report(Long chatId, String text, InputFile photo, String fileName, String fileType) {
        this.chatId = chatId;
        this.text = text;
        this.photo = photo;
        this.fileName = fileName;
        this.fileType = fileType;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public InputFile getPhoto() {
        return photo;
    }

    public void setPhoto(InputFile photo) {
        this.photo = photo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(chatId, report.chatId) && Objects.equals(text, report.text) && Objects.equals(photo, report.photo) && Objects.equals(fileName, report.fileName) && Objects.equals(fileType, report.fileType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, text, photo, fileName, fileType);
    }

    @Override
    public String toString() {
        return "Report{" +
                "chatId=" + chatId +
                ", text='" + text + '\'' +
                ", photo=" + photo +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                '}';
    }
}
