package medictonproject.buisness;

import medictonproject.integration.DoctorSpecializationDAO;
import medictonproject.integration.UserDAO;
import medictonproject.model.DoctorspecializationEntity;
import medictonproject.model.Paginator;
import medictonproject.model.UserEntity;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class UserManagerTest {
    private UserDAO userDAO;
    private DoctorSpecializationDAO specDao;
    private DoctorspecializationEntity docEntity;
    private TokenManager jwt;
    private UserManager userManager;
    private UserEntity userEntity;
    private UserEntity userEntityDB;
    private HttpServletResponse httpServerResponse;
    private EmailSenderManager emailSenderManager;
    private int pageNumber = 1;
    private BCrypt bcCrypt;

    private void initializeFields(){
        userEntity = mock(UserEntity.class);
        userEntityDB = mock(UserEntity.class);
        userDAO = mock(UserDAO.class);
        specDao = mock(DoctorSpecializationDAO.class);
        jwt = mock(TokenManager.class);
        httpServerResponse = mock(HttpServletResponse.class);
        docEntity = mock(DoctorspecializationEntity.class);
        bcCrypt = mock(BCrypt.class);
        emailSenderManager = mock(EmailSenderManager.class);
        userManager = new UserManager(userDAO, specDao, jwt, emailSenderManager);
    }
    private UserEntity getUserEntityDummy() {
        UserEntity dummy = new UserEntity();
        dummy.setIsAdmin(0);
        dummy.setPassword("heslo");
        dummy.setUserId(69);
        dummy.setAddressCity("Prague");
        dummy.setAddressState("CZE");
        dummy.setAddressHouseNumber("69");
        dummy.setEmail("email@email.cz");
        dummy.setFirstName("Name");
        dummy.setLastName("Lastname");
        dummy.setPhoneNumber("123456789");
        dummy.setAddressStreet("Budějická");
        return dummy;

    }

    @Test
    public void delete_getsUserId(){
        initializeFields();

        when(userEntity.getIsAdmin()).thenReturn(-1);
        when(userEntity.getUserId()).thenReturn(1);
        when(userDAO.getUserById(1)).thenReturn(userEntity);

        userManager.deleteUser(1);

        verify(userDAO).getUserById(userEntity.getUserId());
    }

    @Test
    public void delete_notApprovedUserDeleted(){
        initializeFields();

        when(userEntity.getIsAdmin()).thenReturn(-1);
        when(userEntity.getUserId()).thenReturn(1);
        when(userDAO.getUserById(1)).thenReturn(userEntity);

        userManager.deleteUser(1);

        verify(userDAO).delete(userEntity);
    }

    @Test
    public void delete_adminUserNotDeleted(){
        initializeFields();

        when(userEntity.getIsAdmin()).thenReturn(1);
        when(userEntity.getUserId()).thenReturn(1);
        when(userDAO.getUserById(1)).thenReturn(userEntity);

        userManager.deleteUser(1);

        verify(userDAO,times(0)).delete(userEntity);
    }

    @Test
    public void delete_ApprovedUserNotDeleted(){
        initializeFields();

        when(userEntity.getIsAdmin()).thenReturn(0);
        when(userEntity.getUserId()).thenReturn(1);
        when(userDAO.getUserById(1)).thenReturn(userEntity);

        userManager.deleteUser(1);

        verify(userDAO,times(0)).delete(userEntity);
    }

    @Test
    public void register_getsUser() {
        initializeFields();

        when(userEntity.getUserId()).thenReturn(69);

        userManager.register(userEntity,new ArrayList<>());

        verify(userDAO).getUser(userEntity);
    }


    @Test
    public void register_passedUnexistingSpecialization() {
        initializeFields();

        when(userEntity.getUserId()).thenReturn(69);
        when(specDao.getSpecialization("Akrobat")).thenReturn(null);

        List<String> specializations = new ArrayList<>();
        specializations.add("Akrobat");
        boolean result = userManager.register(userEntity,specializations);

        assertFalse(result);
    }

    @Test
    public void register_passedExistingUser() {
        initializeFields();
        when(userDAO.getUser(userEntity)).thenReturn(userEntity);
        boolean result = userManager.register(userEntity,new ArrayList<>());

        assertFalse(result);
    }

    @Test
    public void getAllUsers_getUserList() {
        initializeFields();

        userManager.getAllUsers();

        verify(userDAO).getAllUsers();
    }

    @Test
    public void getApprovedUsers_getUserList() {
        initializeFields();

        userManager.getApprovedUsers(pageNumber);

        verify(userDAO).getApprovedUsers();
    }

    @Test
    public void getApprovedUsers_returnEmptyPaginator() {
        initializeFields();
        userManager.getApprovedUsers(pageNumber);
        verify(userDAO).getApprovedUsers();

        when(userDAO.getAllUsers()).thenReturn(new ArrayList<>());

        Paginator paginator = userManager.getApprovedUsers(pageNumber);
        int expected = 0;
        int actual = paginator.getTotalItems();

        assertEquals(expected, actual);

    }

    @Test
    public void updateUser_existingUser() {
        initializeFields();
        when(userEntity.getUserId()).thenReturn(69);

        UserEntity dummy = getUserEntityDummy();
        when(userDAO.getUser(userEntity)).thenReturn(null);
        when(userDAO.getUserById(69)).thenReturn(dummy);
        List<String> specializations = new ArrayList<>();
        specializations.add("Gynekolog");
        userManager.register(dummy,specializations);
        dummy.setEmail("email@notemail.com");
        userManager.updateUser(dummy);

        assertEquals (dummy.getEmail(),userDAO.getUserById(69).getEmail());
    }

    @Test
    public void login_getsUser() {
        initializeFields();
        when(userEntity.getUserId()).thenReturn(69);
        userManager.login(userEntity,httpServerResponse);

        verify(userDAO).getUser(userEntity);
    }

    @Test
    public void login_unexistinUser() {
        initializeFields();
        when(userDAO.getUser(userEntity)).thenReturn(null);
        int response = userManager.login(userEntity,httpServerResponse);

        assertEquals(0,response);
    }

    @Test
    public void login_UserLoged() {
        initializeFields();
        String password = BCrypt.hashpw("password", BCrypt.gensalt(16));

        when(userDAO.getUser(userEntity)).thenReturn(userEntityDB);
        when(userEntityDB.getIsAdmin()).thenReturn(1);
        when(userEntity.getPassword()).thenReturn("password");
        when(userEntityDB.getPassword()).thenReturn(password);

        int actual = userManager.login(userEntity,httpServerResponse);
        int expected = 2;

        assertEquals(expected, actual);
    }

    @Test
    public void login_UserNotLoged() {
        initializeFields();
        when(userDAO.getUser(userEntity)).thenReturn(userEntityDB);
        when(userEntityDB.getIsAdmin()).thenReturn(0);
        when(userEntity.getPassword()).thenReturn("password");
        String password = BCrypt.hashpw("password", BCrypt.gensalt(16));
        when(userEntityDB.getPassword()).thenReturn(password);
        int response = userManager.login(userEntity,httpServerResponse);
        assertEquals(1,response);
    }

    @Test
    public void login_PasswordMismatch() {
        initializeFields();
        when(userDAO.getUser(userEntity)).thenReturn(userEntityDB);
        when(userEntityDB.getIsAdmin()).thenReturn(0);
        when(userEntity.getPassword()).thenReturn("paassword");
        String password = BCrypt.hashpw("password", BCrypt.gensalt(16));
        when(userEntityDB.getPassword()).thenReturn(password);
        int response = userManager.login(userEntity,httpServerResponse);
        assertEquals(0,response);
    }

    @Test
    public void approveUser_PasswordMismatch() {
        initializeFields();
        when(userDAO.getUserById(69)).thenReturn(userEntity);

        userManager.approveUser(69);

        verify(userDAO).update(userEntity);

    }

    @Test
    public void getUserData_PasswordMismatch() {
        initializeFields();
        when(userDAO.getUserById(69)).thenReturn(userEntity);

        userManager.getUserData(69);

        verify(userDAO).getUserById(69);

    }
}
