package medictonproject.model;

import com.fasterxml.jackson.annotation.*;

import javax.jws.soap.SOAPBinding;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "message", schema = "swprojekt")
public class MessageEntity {
    private int messageId;
    private String text;
    private Timestamp addDate;
    private int isSeen;
    @JsonIgnore
    private ConversationEntity conversation;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UserEntity messageOwner;
    private String ownerName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    public UserEntity getMessageOwner() {
        return messageOwner;
    }

    public void setMessageOwner(UserEntity messageOwner) {
        this.messageOwner = messageOwner;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="conversation_id")
    public ConversationEntity getConversation() {
        return conversation;
    }

    public void setConversation(ConversationEntity conversation) {
        this.conversation = conversation;
    }

    @Id
    @Column(name = "message_id", nullable = false)

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    @Basic
    @Column(name = "text", nullable = false, length = 5000)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Basic
    @Column(name = "add_date", nullable = false)
    public Timestamp getAddDate() {
        return addDate;
    }

    public void setAddDate(Timestamp addDate) {
        this.addDate = addDate;
    }

    @Basic
    @Column(name = "is_seen", nullable = false)
    public int getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(int isSeen) {
        this.isSeen = isSeen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageEntity that = (MessageEntity) o;

        if (messageId != that.messageId) return false;
        if (isSeen != that.isSeen) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        if (addDate != null ? !addDate.equals(that.addDate) : that.addDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = messageId;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (addDate != null ? addDate.hashCode() : 0);
        result = 31 * result + isSeen;
        return result;
    }

    @Transient
    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName() {
        this.ownerName = messageOwner.getFirstName();
    }
}
