package medictonproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conversation", schema = "swprojekt")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ConversationEntity {
    private int conversationId;
    private String title;
    private Timestamp beginningDate;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UserEntity conversationOwner;
    @JsonIgnore
    private List<MessageEntity> messages = new ArrayList<>();
    private MessageEntity firstMessage;
    private boolean seen;

    @Transient
    public MessageEntity getFirstMessage() {
        return firstMessage;
    }

    public void setFirstMessage(MessageEntity firstMessage) {
        this.firstMessage = firstMessage;
    }
    
    @Transient
    public boolean isSeen() {
        return seen;
    }
    
    public void setSeen( boolean seen ) {
        this.seen = seen;
    }
    
    @OneToMany(mappedBy="conversation", cascade = javax.persistence.CascadeType.ALL)
    public List<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageEntity> messages) {
        this.messages = messages;
    }

    @ManyToOne(fetch=FetchType.LAZY, cascade = javax.persistence.CascadeType.ALL)
    @JoinColumn(name="user_id")
    public UserEntity getConversationOwner() {
        return conversationOwner;
    }

    public void setConversationOwner(UserEntity conversationOwner) {
        this.conversationOwner = conversationOwner;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversation_id", nullable = false)
    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    @Basic
    @Column(name = "title", nullable = false, length = 200)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "beginning_date", nullable = false)
    public Timestamp getBeginningDate() {
        return beginningDate;
    }

    public void setBeginningDate(Timestamp beginningDate) {
        this.beginningDate = beginningDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConversationEntity that = (ConversationEntity) o;

        if (conversationId != that.conversationId) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (beginningDate != null ? !beginningDate.equals(that.beginningDate) : that.beginningDate != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = conversationId;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (beginningDate != null ? beginningDate.hashCode() : 0);
        return result;
    }
}
