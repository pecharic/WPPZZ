package medictonproject.controller;

import medictonproject.buisness.ConversationManager;
import medictonproject.model.ConversationEntity;
import medictonproject.model.MessageEntity;
import medictonproject.model.Paginator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/conversation")
public class ConversationController {

  /*
   * instance of ConversationManager object for calling business logic for conversations
   */

  private ConversationManager conversationManager;

  /*
   * constructor of ConversationController class
   */

  @Autowired
  ConversationController(ConversationManager conversationManager) {
    this.conversationManager = conversationManager;
  }

  /**
   * Controller for creating a conversation with it's first message.
   *
   * @param conversation ConversationEntity object which holds data about conversation
   * @param message object which holds first message text which will be inserted to conversation
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @return boolean value
   */

  @RequestMapping(value = "/insertConversation", method = RequestMethod.POST)
  public boolean insertConversation(ConversationEntity conversation, MessageEntity message, HttpServletRequest request,HttpServletResponse response)  {
    return conversationManager.insertConversation(conversation, message, (Integer)request.getAttribute("userId"));
  }

  /**
   * Controller creates a conversation with it's first message by admin.
   *
   * @param conversation ConversationEntity object which holds data about conversation
   * @param message object which holds first message text which will be inserted to conversation
   * @param request HttpServletRequest object
   * @param userId id of user who owns this conversation
   * @return boolean value
   */

  @RequestMapping(value = "/admin/insertConversation", method = RequestMethod.POST)
  public boolean insertConversation(ConversationEntity conversation,
                                    MessageEntity message,
                                    Integer userId,
                                    HttpServletRequest request)  {
    return conversationManager.insertConversation(conversation, message, userId, (Integer)request.getAttribute("userId"));
  }

  /**
   * Controller for inserting messages to a given conversation.
   *
   * @param  conversationId id of conversation where will the message be inserted
   * @param  message object which holds first message text which will be inserted to conversation
   * @param  request object of type HttpServletRequest
   * @param  response object of type HttpServletResponse
   * @return boolean value
   */

  @RequestMapping(value = "/insertMessage", method = RequestMethod.POST)
  public boolean insertMessage(@RequestParam("id") Integer conversationId, MessageEntity message, HttpServletRequest request,HttpServletResponse response)  {
    return conversationManager.insertMessage(conversationId, message, (Integer)request.getAttribute("userId"));
  }


  /**
   * Controller for getting conversations by page for user.
   * @param  page number of page to be returned
   * @param  request object of type HttpServletRequest
   * @return Page of conversations
   */

  @RequestMapping(value = "/getConversations/{page}", method = RequestMethod.GET)
  public Paginator<ConversationEntity> getConversations( HttpServletRequest request,
                                                         @PathVariable("page") Integer page )  {
    return conversationManager.getUserConversations((Integer)request.getAttribute("userId"), page);
  }

  /**
   * Controller for getting conversation messages for user.
   * @param  conversationId id of conversation which will be returned
   * @param  req object of type HttpServletRequest
   * @param  response object of type HttpServletResponse
   * @return Messages list of conversation
   */

  @RequestMapping(value = "/getConversationMessages", method = RequestMethod.GET)
  public List<MessageEntity> getConversationMessages(@RequestParam("id") Integer conversationId,
                                                     HttpServletRequest req,HttpServletResponse response)  {
    //System.out.println(" = " + conversationId + " = ");
    return conversationManager.getConversationMessages( conversationId,
                                                        (Integer)req.getAttribute( "userId" ) );
  }

  /**
   * Controller for admin to get all conversations.
   * @param  page number of page to be returned
   * @param  response object of type HttpServletResponse
   * @return Page of conversations
   */

  @RequestMapping(value = "/admin/getConversations/{page}", method = RequestMethod.GET)
  public Paginator<ConversationEntity> getConversationsForAdmin(@PathVariable("page") Integer page,HttpServletResponse response, HttpServletRequest req) {
    return conversationManager.getAllConversations( page ,  (Integer)req.getAttribute( "userId" ));
  }

  /**
   * Controller for getting conversation messages for admin.
   * @param  conversationId id of conversation
   * @param  response object of type HttpServletResponse
   * @return List of messages of object type MessageEntity
   */

  @RequestMapping(value = "/admin/getConversationMessages", method = RequestMethod.GET)
  public List<MessageEntity> getConversationMessages(@RequestParam("id") Integer conversationId,HttpServletResponse response) {
    return conversationManager.getConversationMessages(conversationId);
  }

  @RequestMapping(value = "/unreadConversationsCount")
  public int getUnreadMessages(HttpServletRequest req ) {
    return conversationManager.getUnreadConversationsCount((Integer) req.getAttribute("userId"));
  }
  
  @RequestMapping(value = "/admin/unreadConversationsCount")
  public int getUnreadMessages() {
    return conversationManager.getUnreadConversationsCount();
  }

  @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
  public ResponseEntity handle() {
    return new ResponseEntity(HttpStatus.OK);
  }

  @RequestMapping(value = "/**/**", method = RequestMethod.OPTIONS)
  public ResponseEntity handle123() {
    return new ResponseEntity(HttpStatus.OK);
  }
}
