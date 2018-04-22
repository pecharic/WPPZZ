package medictonproject.interfaces;

import medictonproject.model.ConversationEntity;
import medictonproject.model.MessageEntity;
import medictonproject.model.UserEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface ConversationInterface {
    @Transactional
    List getConversations(UserEntity u );

    @Transactional
    List getAllConversations();

    @Transactional
    void setFirstMessage(ConversationEntity c);

    @Transactional
    void add(ConversationEntity c);

    @Transactional
    void delete(ConversationEntity c);

    @Transactional
    void update(ConversationEntity c);

    @Transactional
    ConversationEntity getConversationById(Integer conversationId);

    @Transactional
    int getUnreadMessages(Integer userId, Integer conversationId);

    @Transactional
    List<MessageEntity> getUnreadMessages();
}
