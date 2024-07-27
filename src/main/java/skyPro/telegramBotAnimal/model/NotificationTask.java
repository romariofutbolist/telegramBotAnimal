package skyPro.telegramBotAnimal.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "notification_task")
public class NotificationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long chat_id;
    private String userName;
    @Column(name = "firstName", nullable = false)
    private String firstName;
    private String lastName;
    private long userID;
    private String text_msg;
    @Column(name = "phone")
    private String phone;

    public NotificationTask(Long id, long chat_id, String userName, String firstName, String lastName, long userID, String text_msg, String phone) {
        this.id = id;
        this.chat_id = chat_id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userID = userID;
        this.text_msg = text_msg;
        this.phone = phone;
    }


    public NotificationTask() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getChat_id() {
        return chat_id;
    }

    public void setChat_id(long chat_id) {
        this.chat_id = chat_id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getText_msg() {
        return text_msg;
    }

    public void setText_msg(String text_msg) {
        this.text_msg = text_msg;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return chat_id == that.chat_id && userID == that.userID && Objects.equals(id, that.id) && Objects.equals(userName, that.userName) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(text_msg, that.text_msg) && Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chat_id, userName, firstName, lastName, userID, text_msg, phone);
    }
}
