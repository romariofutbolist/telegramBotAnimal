package skyPro.telegramBotAnimal.model;


import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filePath;
    private long fileSize;
    private String mediaType;
    private byte[] data;

    @OneToOne
    private PetReport petReport;


    public Avatar() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public PetReport getPetReport() {
        return petReport;
    }

    public void setPetReport(PetReport petReport) {
        this.petReport = petReport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Avatar avatarPet = (Avatar) o;
        return fileSize == avatarPet.fileSize && Objects.equals(id, avatarPet.id) && Objects.equals(filePath, avatarPet.filePath) && Objects.equals(mediaType, avatarPet.mediaType) && Objects.deepEquals(data, avatarPet.data) && Objects.equals(petReport, avatarPet.petReport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, filePath, fileSize, mediaType, Arrays.hashCode(data), petReport);
    }

    @Override
    public String toString() {
        return "AvatarPet{" +
                "id=" + id +
                ", filePath='" + filePath + '\'' +
                ", fileSize=" + fileSize +
                ", mediaType='" + mediaType + '\'' +
                ", data=" + Arrays.toString(data) +
                ", petReport=" + petReport +
                '}';
    }
}
