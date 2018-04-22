package medictonproject.buisness;

import medictonproject.integration.ConversationDAO;
import medictonproject.integration.MessageDAO;
import medictonproject.integration.UserDAO;
import medictonproject.model.ConversationEntity;
import medictonproject.model.MessageEntity;
import medictonproject.model.Paginator;
import medictonproject.model.UserEntity;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

class ConversationManagerTest {
    private UserDAO userDAO;
    private UserEntity userEntity;
    private UserEntity admin;
    private MessageDAO messageDAO;
    private MessageEntity messageEntity;
    private ConversationDAO conversationDAO;
    private ConversationEntity conversationEntity;
    private ConversationManager conversationManager;

    private List<MessageEntity> messageEntityList;
    private List<MessageEntity> messageEntityList1;
    private List<ConversationEntity> conversationEntityList;

    int userId = 0;
    int adminId = 1;
    int conversationId = 2;
    int pageNumber = 0;

    private void initializeFields(){
        userDAO = mock(UserDAO.class);
        userEntity = mock(UserEntity.class);
        admin = mock(UserEntity.class);
        messageDAO = mock(MessageDAO.class);
        messageEntity = mock(MessageEntity.class);
        conversationDAO = mock(ConversationDAO.class);
        conversationEntity = mock(ConversationEntity.class);
        conversationManager = new ConversationManager(conversationDAO, userDAO, messageDAO);

        messageEntityList = mock(ArrayList.class);
        messageEntityList1 = mock(ArrayList.class);
        conversationEntityList = mock(ArrayList.class);
    }

    @Test
    public void getUserConversations_FindsUser(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );

        conversationManager.getUserConversations(userId, pageNumber);

        verify(userDAO).getUserById(userId);
    }

    @Test
    public void getUserConversations_FindsUserConversations(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );

        conversationManager.getUserConversations(userId, pageNumber);

        verify(userEntity).getConversations();
    }

    @Test
    public void getUserConversationsWithEmptyConversationList_doesNotSetFirstMessage(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        when( userEntity.getConversations() ).thenReturn( new ArrayList<>() );

        conversationManager.getUserConversations(userId, pageNumber);

        verify(conversationDAO, times(0)).setFirstMessage(anyObject());
    }

    @Test
    public void insertConversation_findsUser(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );

        conversationManager.insertConversation(conversationEntity, messageEntity, userId);

        verify(userDAO).getUserById(userId);
    }

    @Test
    public void insertConversation_getsUserMessages(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );

        conversationManager.insertConversation(conversationEntity, messageEntity, userId);

        verify(userEntity).getMessages();
    }

    @Test
    public void insertConversation_addsMessageToUser(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        when( userEntity.getMessages() ).thenReturn( messageEntityList );

        conversationManager.insertConversation(conversationEntity, messageEntity, userId);

        verify(messageEntityList).add(messageEntity);
    }

    @Test
    public void insertConversation_getsUserConversations(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        when( userEntity.getConversations() ).thenReturn( conversationEntityList );

        conversationManager.insertConversation(conversationEntity, messageEntity, userId);

        verify(userEntity).getConversations();
    }

    @Test
    public void insertConversation_addsConversationToUser(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        when( userEntity.getConversations() ).thenReturn( conversationEntityList );

        conversationManager.insertConversation(conversationEntity, messageEntity, userId);

        verify(conversationEntityList).add(conversationEntity);
    }

    @Test
    public void insertConversation_setsConversationOwner(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );

        conversationManager.insertConversation(conversationEntity, messageEntity, userId);

        verify(conversationEntity).setConversationOwner(userEntity);
    }

    @Test
    public void insertConversation_getsConversationMessages(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );

        conversationManager.insertConversation(conversationEntity, messageEntity, userId);

        verify(conversationEntity).getMessages();
    }

    @Test
    public void insertConversation_addsMessageToConversation(){
        initializeFields();

        when( conversationEntity.getMessages() ).thenReturn( messageEntityList );
        when( userDAO.getUserById(userId) ).thenReturn( userEntity );

        conversationManager.insertConversation(conversationEntity, messageEntity, userId);

        verify(messageEntityList).add(messageEntity);
    }

    @Test
    public void insertConversation_setsConversation(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );

        conversationManager.insertConversation(conversationEntity, messageEntity, userId);

        verify(messageEntity).setConversation(conversationEntity);
    }

    @Test
    public void insertConversation_setsMessageOwner(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );

        conversationManager.insertConversation(conversationEntity, messageEntity, userId);

        verify(messageEntity).setMessageOwner(userEntity);
    }

    @Test
    public void insertConversation_addsConversationToDao(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );

        conversationManager.insertConversation(conversationEntity, messageEntity, userId);

        verify(conversationDAO).add(conversationEntity);
    }

    @Test
    public void insertConversationAdmin_findsAdmin(){
        initializeFields();

        when( userDAO.getUserById(adminId) ).thenReturn( admin );
        when( userDAO.getUserById(userId) ).thenReturn( userEntity );

        conversationManager.insertConversation(conversationEntity, messageEntity, userId, adminId);

        verify(userDAO, times(1)).getUserById(adminId);
    }

    @Test
    public void insertConversationAdmin_findsUser(){
        initializeFields();

        when( userDAO.getUserById(adminId) ).thenReturn( admin );
        when( userDAO.getUserById(userId) ).thenReturn( userEntity );

        conversationManager.insertConversation(conversationEntity, messageEntity, userId, adminId);

        verify(userDAO).getUserById(userId);
    }

    @Test
    public void insertConversationAdmin_getsAdminMessages(){
        initializeFields();

        when( userDAO.getUserById(adminId) ).thenReturn( admin );
        when( userDAO.getUserById(userId) ).thenReturn( userEntity );

        conversationManager.insertConversation(conversationEntity, messageEntity, userId, adminId);

        verify(admin).getMessages();
    }

    @Test
    public void insertConversationAdmin_getsUserConversations(){
        initializeFields();

        when( userDAO.getUserById(adminId) ).thenReturn( admin );
        when( userDAO.getUserById(userId) ).thenReturn( userEntity );

        conversationManager.insertConversation(conversationEntity, messageEntity, userId, adminId);

        verify(userEntity).getConversations();
    }

    @Test
    public void insertConversationAdmin_addsMessageToAdmin(){
        initializeFields();

        when( userDAO.getUserById(adminId) ).thenReturn( admin );
        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        when( admin.getMessages() ).thenReturn( messageEntityList );

        conversationManager.insertConversation(conversationEntity, messageEntity, userId, adminId);

        verify(messageEntityList).add(messageEntity);
    }

    @Test
    public void insertConversationAdmin_addConversationToUser(){
        initializeFields();

        when( userDAO.getUserById(adminId) ).thenReturn( admin );
        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        when( userEntity.getConversations() ).thenReturn( conversationEntityList );

        conversationManager.insertConversation(conversationEntity, messageEntity, userId, adminId);

        verify(conversationEntityList).add(conversationEntity);
    }

    @Test
    public void insertConversationAdmin_setsConversationOwner() {
        initializeFields();

        when( userDAO.getUserById(adminId) ).thenReturn( admin );
        when( userDAO.getUserById(userId) ).thenReturn( userEntity );

        conversationManager.insertConversation(conversationEntity, messageEntity, userId, adminId);

        verify(conversationEntity).setConversationOwner(userEntity);
    }

    @Test
    public void insertConversationAdmin_getsConversationMessages() {
        initializeFields();

        when( userDAO.getUserById(adminId) ).thenReturn( admin );
        when( admin.getMessages() ).thenReturn( messageEntityList );
        when( userDAO.getUserById(userId) ).thenReturn( userEntity );

        conversationManager.insertConversation(conversationEntity, messageEntity, userId, adminId);

        verify(conversationEntity).getMessages();
    }

    @Test
    public void insertConversationAdmin_addsMessageToUserConversation() {
        initializeFields();

        when( userDAO.getUserById(adminId) ).thenReturn( admin );
        when( admin.getMessages() ).thenReturn( messageEntityList );
        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        when( conversationEntity.getMessages() ).thenReturn( messageEntityList1 );

        conversationManager.insertConversation(conversationEntity, messageEntity, userId, adminId);

        verify(messageEntityList1).add(messageEntity);
    }

    @Test
    public void insertConversationAdmin_setsConversationToMessage() {
        initializeFields();

        when( userDAO.getUserById(adminId) ).thenReturn( admin );
        when( userDAO.getUserById(userId) ).thenReturn( userEntity );

        conversationManager.insertConversation(conversationEntity, messageEntity, userId, adminId);

        verify(messageEntity).setConversation(conversationEntity);
    }

    @Test
    public void insertConversationAdmin_setsMessageOwner() {
        initializeFields();

        when( userDAO.getUserById(adminId) ).thenReturn( admin );
        when( userDAO.getUserById(userId) ).thenReturn( userEntity );

        conversationManager.insertConversation(conversationEntity, messageEntity, userId, adminId);

        verify(messageEntity).setMessageOwner(admin);
    }

    @Test
    public void insertConversationAdmin_addsConversationToDao() {
        initializeFields();

        when( userDAO.getUserById(adminId) ).thenReturn( admin );
        when( userDAO.getUserById(userId) ).thenReturn( userEntity );

        conversationManager.insertConversation(conversationEntity, messageEntity, userId, adminId);

        verify(conversationDAO).add(conversationEntity);
    }

    @Test
    public void getConversationMessagesOneArg_findsConversation(){
        initializeFields();

        when( conversationDAO.getConversationById(conversationId) ).thenReturn( conversationEntity );
        when( conversationEntity.getConversationOwner() ).thenReturn( userEntity );

        conversationManager.getConversationMessages(conversationId);

        verify(conversationDAO).getConversationById(conversationId);
    }

    @Test
    public void getConversationMessagesOneArg_findsConversationOwner(){
        initializeFields();

        when( conversationDAO.getConversationById(conversationId) ).thenReturn( conversationEntity );
        when( conversationEntity.getConversationOwner() ).thenReturn( userEntity );

        conversationManager.getConversationMessages(conversationId);

        verify(conversationEntity).getConversationOwner();
    }

    @Test
    public void getConversationMessagesOneArg_getsOwnerId(){
        initializeFields();

        when( conversationDAO.getConversationById(conversationId) ).thenReturn( conversationEntity );
        when( conversationEntity.getConversationOwner() ).thenReturn( userEntity );

        conversationManager.getConversationMessages(conversationId);

        verify(userEntity).getUserId();
    }

    @Test
    public void getConversationMessagesOneArg_updatesConversation(){
        initializeFields();

        when( conversationDAO.getConversationById(conversationId) ).thenReturn( conversationEntity );
        when( conversationEntity.getConversationOwner() ).thenReturn( userEntity );
        when( userEntity.getUserId() ).thenReturn( userId );

        conversationManager.getConversationMessages(conversationId);

        verify(conversationDAO).update(conversationEntity);
    }

    @Test
    public void getConversationMessagesOneArgWithoutMessages_returnsEmptyList(){
        initializeFields();

        when( conversationDAO.getConversationById(conversationId) ).thenReturn( conversationEntity );
        when( conversationEntity.getConversationOwner() ).thenReturn( userEntity );
        when( userEntity.getUserId() ).thenReturn( userId );
        when( conversationEntity.getMessages() ).thenReturn( new ArrayList<>() );

        List<MessageEntity> result = conversationManager.getConversationMessages(conversationId);
        int expected = 0;
        int actual = result.size();

        assertEquals(expected, actual);
    }

    @Test
    public void getConversationMessagesTwoArgs_findsConversation(){
        initializeFields();

        when( conversationDAO.getConversationById(conversationId) ).thenReturn( conversationEntity );
        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        when( userEntity.getIsAdmin() ).thenReturn( 0 );
        when( conversationEntity.getConversationOwner() ).thenReturn( userEntity );
        when( userEntity.getUserId() ).thenReturn( userId );
        when( conversationEntity.getMessages() ).thenReturn( new ArrayList<>() );

        conversationManager.getConversationMessages(conversationId, userId);

        verify(conversationDAO).getConversationById(conversationId);
    }

    @Test
    public void getConversationMessagesTwoArgs_findsConversationOwner(){
        initializeFields();

        when( conversationDAO.getConversationById(conversationId) ).thenReturn( conversationEntity );
        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        when( userEntity.getIsAdmin() ).thenReturn( 0 );
        when( conversationEntity.getConversationOwner() ).thenReturn( userEntity );
        when( userEntity.getUserId() ).thenReturn( userId );
        when( conversationEntity.getMessages() ).thenReturn( new ArrayList<>() );

        conversationManager.getConversationMessages(conversationId, userId);

        verify(conversationEntity).getConversationOwner();
    }

    @Test
    public void getConversationMessagesTwoArgs_getsOwnerId(){
        initializeFields();

        when( conversationDAO.getConversationById(conversationId) ).thenReturn( conversationEntity );
        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        when( userEntity.getIsAdmin() ).thenReturn( 0 );
        when( conversationEntity.getConversationOwner() ).thenReturn( userEntity );
        when( userEntity.getUserId() ).thenReturn( userId );
        when( conversationEntity.getMessages() ).thenReturn( new ArrayList<>() );

        conversationManager.getConversationMessages(conversationId, userId);

        verify(userEntity).getUserId();
    }

    @Test
    public void getConversationMessagesTwoArgsPassedUserIsNotOwner_returnsNull(){
        initializeFields();

        when( conversationDAO.getConversationById(conversationId) ).thenReturn( conversationEntity );
        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        when( userEntity.getIsAdmin() ).thenReturn( 0 );
        when( conversationEntity.getConversationOwner() ).thenReturn( userEntity );
        when( userEntity.getUserId() ).thenReturn( userId + 1 );
        when( conversationEntity.getMessages() ).thenReturn( new ArrayList<>() );

        List<MessageEntity> result = conversationManager.getConversationMessages(conversationId, userId);

        assertNull(result);
    }

    @Test
    public void getConversationMessagesTwoArgs_updatesConversation(){
        initializeFields();

        when( conversationDAO.getConversationById(conversationId) ).thenReturn( conversationEntity );
        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        when( userEntity.getIsAdmin() ).thenReturn( 1 );
        when( conversationEntity.getConversationOwner() ).thenReturn( userEntity );
        when( userEntity.getUserId() ).thenReturn( userId );
        when( conversationEntity.getMessages() ).thenReturn( new ArrayList<>() );

        conversationManager.getConversationMessages(conversationId, userId);

        verify(conversationDAO).update(conversationEntity);
    }

    @Test
    public void insertMessage_findsUser(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        when( conversationDAO.getConversationById(conversationId) ).thenReturn( conversationEntity );
        when( conversationEntity.getConversationOwner() ).thenReturn( userEntity );
        when( userEntity.getUserId() ).thenReturn( userId );
        when( userEntity.getIsAdmin() ).thenReturn( 1 );

        conversationManager.insertMessage(conversationId, messageEntity, userId);

        verify(userDAO).getUserById(userId);
    }

    @Test
    public void insertMessage_findsConversation(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        when( conversationDAO.getConversationById(conversationId) ).thenReturn( conversationEntity );
        when( conversationEntity.getConversationOwner() ).thenReturn( userEntity );
        when( userEntity.getUserId() ).thenReturn( userId );
        when( userEntity.getIsAdmin() ).thenReturn( 1 );

        conversationManager.insertMessage(conversationId, messageEntity, userId);

        verify(conversationDAO).getConversationById(conversationId);
    }

    @Test
    public void insertMessage_setsMessageOwner(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        when( conversationDAO.getConversationById(conversationId) ).thenReturn( conversationEntity );
        when( conversationEntity.getConversationOwner() ).thenReturn( userEntity );
        when( userEntity.getUserId() ).thenReturn( userId );
        when( userEntity.getIsAdmin() ).thenReturn( 1 );

        conversationManager.insertMessage(conversationId, messageEntity, userId);

        verify(messageEntity).setMessageOwner(userEntity);
    }

    @Test
    public void insertMessage_setsMessageConversation(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        when( conversationDAO.getConversationById(conversationId) ).thenReturn( conversationEntity );
        when( conversationEntity.getConversationOwner() ).thenReturn( userEntity );
        when( userEntity.getUserId() ).thenReturn( userId );
        when( userEntity.getIsAdmin() ).thenReturn( 1 );

        conversationManager.insertMessage(conversationId, messageEntity, userId);

        verify(messageEntity).setConversation(conversationEntity);
    }

    @Test
    public void insertMessage_addsMessageToDao(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        when( conversationDAO.getConversationById(conversationId) ).thenReturn( conversationEntity );
        when( conversationEntity.getConversationOwner() ).thenReturn( userEntity );
        when( userEntity.getUserId() ).thenReturn( userId );
        when( userEntity.getIsAdmin() ).thenReturn( 1 );

        conversationManager.insertMessage(conversationId, messageEntity, userId);

        verify(messageDAO).add(messageEntity);
    }

    @Test
    public void insertMessageConversationDoesNotBelongToUserAndUserIsNotAdmin_returnsFalse(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        when( conversationDAO.getConversationById(conversationId) ).thenReturn( conversationEntity );
        when( conversationEntity.getConversationOwner() ).thenReturn( userEntity );
        when( userEntity.getUserId() ).thenReturn( userId + 10 );
        when( userEntity.getIsAdmin() ).thenReturn( 0 );

        boolean result = conversationManager.insertMessage(conversationId, messageEntity, userId);

        assertFalse(result);
    }

   /* @Test
    public void getUnreadConversationsCountOneArg_getsUnreadMessagesOfUser(){
        initializeFields();

        conversationManager.getUnreadConversationsCount(userId);

        verify(conversationDAO).getUnreadMessages(userId);
    }*/

    /*@Test
    public void getUnreadConversationsCountOneArg_getsConversationId(){
        initializeFields();

        List<MessageEntity> unreadMessages = new ArrayList<>();
        unreadMessages.add(messageEntity);

        when( conversationDAO.getUnreadMessages(userId) ).thenReturn( unreadMessages );
        when( messageEntity.getConversation() ).thenReturn( conversationEntity );

        conversationManager.getUnreadConversationsCount(userId);

        verify(conversationEntity).getConversationId();
    }*/

    @Test
    public void getUnreadConversationsCountNoArg_getsUnreadMessagesOfUser(){
        initializeFields();

        conversationManager.getUnreadConversationsCount();

        verify(conversationDAO).getUnreadMessages();
    }

    @Test
    public void getUnreadConversationsCountNoArg_getsConversationId(){
        initializeFields();

        List<MessageEntity> unreadMessages = new ArrayList<>();
        unreadMessages.add(messageEntity);

        when( conversationDAO.getUnreadMessages() ).thenReturn( unreadMessages );
        when( messageEntity.getConversation() ).thenReturn( conversationEntity );

        conversationManager.getUnreadConversationsCount();

        verify(conversationEntity).getConversationId();
    }

}