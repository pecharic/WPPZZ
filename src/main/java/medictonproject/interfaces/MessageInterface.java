package medictonproject.interfaces;

import medictonproject.model.ConversationEntity;
import medictonproject.model.MessageEntity;
import medictonproject.model.UserEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface MessageInterface {

    @Transactional
    void add(MessageEntity m);

    @Transactional
    List getMessages(UserEntity u, ConversationEntity c);
}
