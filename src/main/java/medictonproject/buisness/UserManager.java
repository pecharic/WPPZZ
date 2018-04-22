package medictonproject.buisness;

import medictonproject.integration.DoctorSpecializationDAO;
import medictonproject.integration.UserDAO;
import medictonproject.model.Constants;
import medictonproject.model.DoctorspecializationEntity;
import medictonproject.model.Paginator;
import medictonproject.model.UserEntity;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserManager
{

  /*
   * instance of UserDAO object with which we get data from database
   */
  private UserDAO userDAO;

  /*
   * instance of DoctorSpecializationDAO object with which we get data from database
   */

  private DoctorSpecializationDAO specDao;

  /*
   * instance of TokenManager object which creates a token
   */
  private TokenManager jwt;

  /*
   * Constructor of UserManager class
   */

  private EmailSenderManager emailSender;

  @Autowired
  UserManager( UserDAO userDAO, DoctorSpecializationDAO specDAO, TokenManager jwt, EmailSenderManager emailSender ) {
    this.userDAO = userDAO;
    this.specDao = specDAO;
    this.jwt = jwt;
    this.emailSender = emailSender;
  }

  /**
   * Method returns all the messages for a conversation specified with conversation id.
   *
   * @param  id ID of a conversation
   *
   */

  public void deleteUser(Integer id) {
    UserEntity user = (UserEntity)userDAO.getUserById(id);
    if( user.getIsAdmin() == -1 )
      userDAO.delete(user);
  }

  /**
   * Method creates an account for user with given informations and specifications.
   *
   * @param u object of type UserEntity which holds data for user
   * @param specs List of String objects which contains all user specializations
   *
   */
  
  public boolean register(UserEntity u, List<String> specs) {
    List<DoctorspecializationEntity> specializations = new ArrayList<>();
    u.setIsAdmin( -1 );

    for (int i = 0; i < specs.size(); i++) {
      DoctorspecializationEntity  tmp = (DoctorspecializationEntity) specDao.getSpecialization(specs.get(i));
      if( tmp == null )
        return false;
      specializations.add(tmp);
    }

    
    if( (UserEntity) userDAO.getUser(u) != null ) {
      //System.out.println("User with the same email is already registered.");
      return false;
    }
    
    //System.out.printf("Insert new user into database.");
    u.setPassword(BCrypt.hashpw(u.getPassword(), BCrypt.gensalt(16)));
    u.setSpecializations(specializations);
    if( userDAO.add(u) == false ) {
      //System.out.println( "Trying to insert null parameter");
      return false;
    }

    //System.out.println("AAAAAAAAAAAAAAAAAAAAAA " + u.getEmail());
    emailSender.sendSimpleMessage(u.getEmail(), "Schválenie účtu", "Dobrý deň, Váš účet budete môcť používať hneď, ako bude schválený administrátorom." +
                                                                                "O schválení účtu Vás budeme informovať emailom.");
    return true;
  }

  /**
   * Method is for user to login and also verify email and password.
   *
   * @param u object of type UserEntity which holds data for user
   * @param res object of type HttpServletResponse
   *
   */

  public int login( UserEntity u, HttpServletResponse res ) {
    Integer userId;

    UserEntity userExist = (UserEntity) userDAO.getUser(u);
    if( userExist == null || userExist.getIsAdmin() == -1 ) {
      //System.out.printf("The user was not a user found.");
      return 0;
    }
    
    if (BCrypt.checkpw(u.getPassword(), userExist.getPassword())) {
      userId = userExist.getUserId();
      jwt.addAuthentication(res, userId);
      //0 - not logged, 1 - not admin,
      if( userExist.getIsAdmin() == 1 )
          return 2;
      else
          return 1;
    }
    
    return 0;
  }


  /**
   * Method which returns all users.
   *
   * @return List of users which are UserEntity objects
   *
   */
  public List<UserEntity> getAllUsers() {
    return userDAO.getAllUsers();
  }

  /**
   * Method which returns all unapproved users.
   *
   * @param page type of Integer
   * @return Paginator object
   */

  public Paginator<UserEntity> getUnapprovedUsers( Integer page ) {
    List<UserEntity> userList = userDAO.getUnapprovedUsers();
    for( UserEntity u : userList )
      u.setPassword( null );
  
    Paginator<UserEntity> userPaginator = new Paginator<>( userList, page, Constants.PAGE_SIZE_USERS );
    return userPaginator;
  }
  
  
  /**
   * Method updates user's profile.
   *
   * @param userId Integer ID of user
   * @param u User to be updated
   *
   * @return true for successful update, false otherwise
   */
  public boolean updateUser( int userId , UserEntity u){
    UserEntity user = userDAO.getUserById( userId );

    u.setUserId( userId );
    u.setPassword( user.getPassword());
    if( userDAO.update( u ) == true )
      return true;
    
    return false;
  }

  /**
   * Updates user
   * @param u UserEntity User to be updated
   * @return boolean false if error occurs, true otherwise
   */
  public boolean updateUser(UserEntity u) {
    UserEntity user = userDAO.getUserById(u.getUserId());
    u.setPassword(user.getPassword());
    
    if( userDAO.update(u) == true )
      return true;
    
    return false;
  }

  /**
   * Approves user
   * @param userId Integer Id of user to be approved
   */
  public void approveUser( int userId ) {
    UserEntity user = userDAO.getUserById( userId );
    user.setIsAdmin( 0 );
    userDAO.update( user );
    emailSender.sendSimpleMessage(user.getEmail(), "Vas ucet bol schvaleny", "Dobry den " + user.getFirstName() + " " + user.getLastName() + " Vas medicton ucet bol schvaleny.");
  }

  /**
   *  Returns user with specific id
   * @param id Integer ID of user to be returned
   * @return user with userId id
   */
  public UserEntity getUser( int id ){
    return userDAO.getUserById( id );
  }
  public Paginator<UserEntity> getApprovedUsers( Integer page ) {
    List<UserEntity> approvedUsers = userDAO.getApprovedUsers();
    Paginator<UserEntity> approvedUsersPaginator = new Paginator<>( approvedUsers, page, Constants.PAGE_SIZE_USERS );
    return approvedUsersPaginator;
  }

  /**
   * Returns User with specific ID with reseted password.
   * @param userId Integer id of user to be returned.
   * @return user with userId id
   */
  public UserEntity getUserData(Integer userId) {
    UserEntity user = userDAO.getUserById(userId);
    user.setPassword("");
    return user;
  }
}
