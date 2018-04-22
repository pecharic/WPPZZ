package medictonproject.controller;

import medictonproject.buisness.UserManager;
import medictonproject.model.Paginator;
import medictonproject.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * instance of UserManager object
     */

    private UserManager userManager;

    /**
     * UserManager constructor
     */

    @Autowired
    public UserController(UserManager userManager) {
        this.userManager = userManager;
    }

    /**
     * Controller for login. This controller calls userManager.login method
     *
     * @param u User to be authorized
     * @param res object of type HttpServetResponse
     *
     * @return 0 for not authorized user, 1 for authorized user but not admin,
     *         2 for authorized admin
     */

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Integer authorization(UserEntity u, HttpServletResponse res) {
        return userManager.login(u, res);
    }

    /**
     * Controller for register. This controller calls userManager.register method
     *
     * @param u User to be registered
     * @param specs Specializations of a user (doctor)
     *
     * @return true for successful registration, 0 for failed registration
     */

    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE )
    public boolean registration(UserEntity u , @RequestParam("specs") List<String> specs ) {
        return userManager.register(u , specs);
    }

    /**
     * Controller for updating user profile. This controller calls userManager.updateUser method
     *
     * @param u User profile to be updated
     * @param req object of type HttpServletRequest
     *
     * @return true for successful update of user profile, false for failure
     */

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public boolean updateUser(UserEntity u , HttpServletRequest req) {
        return userManager.updateUser((Integer) req.getAttribute("userId"), u );
    }

    /**
     * Controller for getting user.
     *
     * @param req object of type HttpServletRequest
     *
     * @return Requested user from database
     */

    @RequestMapping(value = "/getUser")
    public UserEntity getUser( HttpServletRequest req ){
       // System.out.println("It is " + (Integer) req.getAttribute("userId"));
        return userManager.getUser( (Integer) req.getAttribute("userId"));
    }

    /**
     * Controller for admin to get all users. This controller calls userManager.getAllUsers method
     *
     * @return List of all users in database
     */
    @RequestMapping(value = "/admin/getAllUsers", method = RequestMethod.GET)
    public List<UserEntity> getAllUsers() {
        return userManager.getAllUsers();
    }

    /**
     * Controller for admin to delete user account. This controller calls userManager.deleteUser method
     *
     * @param id ID of user to be deleted
     */
    @RequestMapping(value = "/admin/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void deleteUser(@RequestParam("id") Integer id) {
        userManager.deleteUser(id);
    }

    /**
     * Controller for admin to get all unapproved users.
     *
     * @param page Number of page to be returned
     *
     * @return Single page of users
     */

    @RequestMapping(value = "/admin/getUnapprovedUsers/{page}", method = RequestMethod.GET )
    public Paginator<UserEntity> getUnapprovedUsers( @PathVariable("page") Integer page ){
        return userManager.getUnapprovedUsers( page );
    }

    /**
     * Controller for admin to approve user.
     *
     * @param userId ID of user to be approved
     */
    
    @RequestMapping(value = "/admin/approveUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void approveUser( int userId ) {
        userManager.approveUser( userId );
    }

    /**
     * Controller for admin to get certain ammount of approved users.
     *
     * @param page Number of page to be returned
     *
     * @return Single page of approved users
     */

    @RequestMapping(value = "/admin/getApprovedUsers/{page}", method = RequestMethod.GET)
    public Paginator<UserEntity> getApprovedUsers( @PathVariable("page") Integer page ) {
        return userManager.getApprovedUsers( page );
    }

    /**
     * Controller for admin to get user data.
     *
     * @param userId ID of user to be returned
     *
     * @return User object with information about user
     */

    @RequestMapping(value = "/admin/getUserData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public UserEntity getUserData(@RequestParam("id") Integer userId) {
        return userManager.getUserData(userId);
    }


    /**
     * Controller for admin to update user profile.
     *
     * @param u Updated user object
     *
     * @return true for successful update of user, false for failure
     */

    @RequestMapping(value = "/admin/updateUser", method = RequestMethod.PUT)
    public boolean updateUser(UserEntity u) {
        return userManager.updateUser(u);
    }

    @RequestMapping(value = "/**",method = RequestMethod.OPTIONS)
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
