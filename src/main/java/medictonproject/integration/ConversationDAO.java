package medictonproject.integration;

import medictonproject.buisness.IntuoManager;
import medictonproject.model.ConversationEntity;
import medictonproject.model.MessageEntity;
import medictonproject.model.UserEntity;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Service
public class ConversationDAO implements medictonproject.interfaces.ConversationInterface {

  private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ConversationDAO.class);

  @PersistenceContext
  private EntityManager em;
  
  /**
   * Returns all the conversations that belong to specific user.
   *
   * @param u Owner of the conversations
   * @return List of conversations of specific user
   */
  @Override
  @Transactional
  public List getConversations(UserEntity u)
  {
    Query q  = em.createNativeQuery( "SELECT * FROM conversation WHERE user_id=?1", ConversationEntity.class );
    q.setParameter( 1, u.getUserId() );
    return q.getResultList();
  }
  
  /**
   * Returns all the conversations in database.
   *
   * @return List of all the conversations in database
   */
  @Override
  @Transactional
  public List getAllConversations() {
    try{
      Query q  = em.createNativeQuery( "SELECT * FROM conversation ORDER BY beginning_date DESC", ConversationEntity.class );
      return q.getResultList();
    } catch( NoResultException e ) {
      logger.info("getAllConversations noResultException", e);
      return null;
    }
  }
  
  /**
   * Sets beginning message for the conversation.
   *
   * @param c Conversation for which is going to be beginning message set
   */
  @Override
  @Transactional
  public void setFirstMessage(ConversationEntity c) {
    try {
      Query q = em.createNativeQuery("SELECT * FROM message WHERE conversation_id = ?1 ORDER BY add_date ASC LIMIT 1", MessageEntity.class);
      q.setParameter(1, c.getConversationId());
      c.setFirstMessage((MessageEntity) q.getSingleResult());
    } catch( NoResultException e ) {
      //System.out.println( "Set first message no result");
      logger.info("setFirstMessage noResultException", e);
      c.setFirstMessage(null);
    }
  }
  
  /**
   * Adds a new conversation into database.
   *
   * @param c Conversation that is about to be added
   */
  @Override
  @Transactional
  public void add(ConversationEntity c) {
    em.persist(c);
  }
  
  /**
   * Deletes a converation from database.
   *
   * @param c Conversation that is about to be deleted
   */
  @Override
  @Transactional
  public void delete(ConversationEntity c) {
    em.remove( c );
  }
  
  /**
   * Updates conversation in database
   *
   * @param c Conversation that is about to be updated
   */
  @Override
  @Transactional
  public void update(ConversationEntity c) {
    em.merge( c );
  }
  
  /**
   * Returns conversation object found by its ID.
   *
   * @param conversationId Integer ID of conversation
   * @return Conversation object
   */
  @Override
  @Transactional
  public ConversationEntity getConversationById(Integer conversationId) {
    return em.find(ConversationEntity.class, conversationId);
  }
  
  /**
   * Returns all the messages of specific user.
   *
   * @param userId Integer ID of user
   * @return List of messages of specific user
   */
  @Override
  @Transactional
  public int  getUnreadMessages(Integer userId , Integer conversationId) {
    Query q = em.createNativeQuery( "SELECT * FROM message WHERE user_id!=?1 AND conversation_id=?2 AND is_seen = 0", MessageEntity.class );
    q.setParameter( 1, userId );
    q.setParameter(2,conversationId);

    return q.getResultList().size();
  }
  
  /**
   * Returns all the messages in database
   *
   * @return List of all the messages in database
   */
  @Override
  @Transactional
  public List<MessageEntity> getUnreadMessages() {
    Query q = em.createNativeQuery( "SELECT * FROM message", MessageEntity.class );
    return q.getResultList();
  }
}
