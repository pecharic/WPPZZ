package medictonproject.integration;

import medictonproject.model.ConversationEntity;
import medictonproject.model.MessageEntity;
import medictonproject.model.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Service
public class MessageDAO implements medictonproject.interfaces.MessageInterface {
  @PersistenceContext
  private EntityManager em;

  /**
   * Add new Message in the database
   *
   * @param m MessageEntity object
   */
  
  @Override
  @Transactional
  public void add(MessageEntity m)
  {
    em.persist( m );
  }

  /**
   * Get all messages from conversation.
   *
   * @param u UserEntity object owner of conversation
   * @param c ConversationEntity object converstaion to retrieve messages from
   * @return list of messages belonging to user u
   */
  @Override
  @Transactional
  public List getMessages(UserEntity u, ConversationEntity c)
  {
    Query q = em.createNativeQuery( "SELECT * FROM message " +
                                       "WHERE user_id=?1 AND conversation_id=?2",
                                       MessageEntity.class );
    q.setParameter( 1, u.getUserId() );
    q.setParameter( 1, c.getConversationId() );
    return q.getResultList();
  }
}
