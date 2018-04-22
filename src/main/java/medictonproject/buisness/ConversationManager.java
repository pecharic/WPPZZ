package medictonproject.buisness;

import medictonproject.integration.ConversationDAO;
import medictonproject.integration.MessageDAO;
import medictonproject.integration.UserDAO;
import medictonproject.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class ConversationManager {
  private ConversationDAO conversationDAO;
  private UserDAO userDAO;
  private MessageDAO messageDAO;

  @Autowired
  public ConversationManager(ConversationDAO conversationDAO, UserDAO userDAO, MessageDAO messageDAO) {
    this.conversationDAO = conversationDAO;
    this.userDAO = userDAO;
    this.messageDAO = messageDAO;
  }
  
  /**
   * Returns a page of conversations for a user.
   *
   * @param  userId ID of user
   *
   * @param  page Number of page to be returned
   *
   * @return Page of conversations
   */
  public Paginator<ConversationEntity> getUserConversations(Integer userId, Integer page) {
    UserEntity user = userDAO.getUserById(userId);
    List<ConversationEntity> conversations = user.getConversations();
    for (ConversationEntity c : conversations) {
      conversationDAO.setFirstMessage(c);
      setIsSeenConversation( c , userId);
    }
  
    Paginator<ConversationEntity> conPaginator = new Paginator<>( conversations,
                                                                  page,
                                                                  Constants.PAGE_SIZE_CONVERSATIONS );
    return conPaginator;
  }
  
  /**
   * Returns a page of conversations for admin - conversations from all users.
   *
   * @param  page Number of page to be returned
   *
   * @return Page of conversations
   */
  public Paginator<ConversationEntity> getAllConversations( Integer page , Integer userId) {
    List<ConversationEntity> conversations = conversationDAO.getAllConversations();
    for (ConversationEntity c : conversations) {
      conversationDAO.setFirstMessage(c);
      setIsSeenConversation( c , userId );
    }
    
    Paginator<ConversationEntity> conPaginator = new Paginator<>( conversations,
                                                                  page,
                                                                  Constants.PAGE_SIZE_CONVERSATIONS );
    return conPaginator;
  }
  
  /**
   * Method persists a new conversation.
   *
   * @param conversation ConversationEntity object that is about to be added
   * @param message Beginning message in the conversation
   *
   * @return boolean value depending on the success of persist
   */
  public boolean insertConversation(ConversationEntity conversation, MessageEntity message, Integer userId) {
    UserEntity user = userDAO.getUserById(userId);

    user.getMessages().add(message);
    user.getConversations().add(conversation);

    conversation.setConversationOwner(user);
    conversation.getMessages().add(message);

    message.setConversation(conversation);
    message.setMessageOwner(user);

    conversationDAO.add(conversation);
    return true;
  }
  
  /**
   * Method persists a new conversation.
   *
   * @param conversation ConversationEntity object that is about to be added
   * @param message Beginning message in the conversation
   *
   * @return boolean value depending on the success of persist
   */
  // inserting conversations for admin side
  public boolean insertConversation(ConversationEntity conversation,
                                    MessageEntity message,
                                    Integer userId,
                                    Integer adminId) {
    UserEntity admin = userDAO.getUserById( adminId );
    UserEntity user = userDAO.getUserById(userId);
    
    admin.getMessages().add(message);
    user.getConversations().add(conversation);
    
    conversation.setConversationOwner(user);
    conversation.getMessages().add(message);
    
    message.setConversation(conversation);
    message.setMessageOwner(admin);
    
    conversationDAO.add(conversation);
    return true;
  }
  
  /**
   * Method returns all the messages for a conversation specified with conversation id.
   *
   * @param conversationId ID of a conversation
   * @param userId ID of a user
   *
   * @return List of messages belonging to a specific conversation
   */
  // messages for coversation for user side
  public List<MessageEntity> getConversationMessages( Integer conversationId, Integer userId ) {
    ConversationEntity conversation = conversationDAO.getConversationById(conversationId);
    UserEntity u = userDAO.getUserById(userId);

    if ( u.getIsAdmin()!= 1 &&  conversation.getConversationOwner().getUserId() != userId )
      return null;

    List<MessageEntity> messages = conversation.getMessages();
    for (MessageEntity message : messages) {
      message.setOwnerName();
      if( message.getMessageOwner().getUserId() != userId )
        message.setIsSeen(1);
    }
  
    conversationDAO.update(conversation);
    return conversation.getMessages();
  }
  
  /**
   * Method returns all the messages for a conversation specified with conversation id.
   *
   * @param  conversationId ID of a conversation
   *
   * @return List of messages of object type MessageEntity
   */
  // messages for conversation for admin side
  public List<MessageEntity> getConversationMessages(Integer conversationId) {
    ConversationEntity conversation = conversationDAO.getConversationById(conversationId);
    Integer conversationOwnerId = conversation.getConversationOwner().getUserId();
    
    List<MessageEntity> messages = conversation.getMessages();
    for (MessageEntity message : messages) {
        message.setOwnerName();
        if( message.getMessageOwner().getUserId() == conversationOwnerId )
            message.setIsSeen(1);
    }
  
    conversationDAO.update(conversation);
    return conversation.getMessages();
  }
  
  /**
   * Method inserts a new message to a concrete conversation
   *
   * @param  id Integer ID of parent conversation
   * @param  message Message that is about to be persisted
   *
   * @return true or false - depending of success of persisting
   */
  public boolean insertMessage(Integer id, MessageEntity message, Integer userId) {
    UserEntity user = userDAO.getUserById(userId);
    ConversationEntity conversation = conversationDAO.getConversationById(id);
    if (conversation.getConversationOwner().getUserId() != userId && user.getIsAdmin() != 1) {
      return false;
    }
    message.setMessageOwner(user);
    message.setConversation(conversation);
    messageDAO.add(message);
    return true;
  }
  
  /**
   * Method sets all the messages of a conversation as seen.
   *
   * @param  c Conversation object
   */
  private void setIsSeenConversation(ConversationEntity c, Integer userId ) {
    for(MessageEntity m : c.getMessages()) {

      if( m.getMessageOwner().getUserId()!= userId && m.getIsSeen() == 0) {
        c.setSeen(false);
        return;
      }
    }
    c.setSeen( true );
  }
  
  /**
   * Method counts all the unread conversations for a specific user.
   *
   * @param userId ID of a user
   *
   * @return Amount of all unread conversations.
   */
  // for user side
  public int getUnreadConversationsCount( Integer userId ){
    UserEntity user = userDAO.getUserById(userId);
    List<ConversationEntity> conversations;
    if( user.getIsAdmin() == 1 ){
      conversations = conversationDAO.getAllConversations();
    }else{
      conversations =user.getConversations();
    }



    int unreadCount = 0;

    for(ConversationEntity c : conversations){
       if( conversationDAO.getUnreadMessages( userId , c.getConversationId()) > 0 )
         unreadCount++;
    }
    return unreadCount;
  }
  
  /**
   * Method counts all the unread conversations for admin.
   *
   * @return Amount of all unread conversations.
   */
  // for admin side
  public int getUnreadConversationsCount(){
    List<MessageEntity> unreadMessages = conversationDAO.getUnreadMessages();
    Set<Integer> unreadConversations = new TreeSet<>();
    
    for( MessageEntity m : unreadMessages ) {
      Integer conversationId = m.getConversation().getConversationId();
      if( m.getIsSeen() == 0 )
        unreadConversations.add( conversationId );
    }
    
    return unreadConversations.size();
  }
}
