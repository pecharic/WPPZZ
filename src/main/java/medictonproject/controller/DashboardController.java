package medictonproject.controller;

import medictonproject.buisness.DashboardManager;
import medictonproject.model.AnnouncementEntity;
import medictonproject.model.Constants;
import medictonproject.model.Paginator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping( "/dashboard" )
public class DashboardController {

  /*
   * instance of DashboardManager object for calling bussiness logic
   */
  private DashboardManager dashboardManager;

  /**
   * DashBoardController constructor
   */

  @Autowired
  public DashboardController(DashboardManager dashboardManager) {
    this.dashboardManager = dashboardManager;
  }

  /**
   * Controller for getting certain ammount of announcements.
   *
   * @param page - number of page to be returned
   * @param request - object of HttpServletRequest
   *
   * @return Page of announcements
   */

  @CrossOrigin(origins = "*")
  @RequestMapping(value = "/getAnnouncements/{page}", method = RequestMethod.GET)
  public Paginator<AnnouncementEntity> getAnnouncements( @PathVariable("page") Integer page,
                                                         HttpServletRequest request ) {
    return dashboardManager.getAnnouncements( page,
                                              (Integer)request.getAttribute( "userId" ),
                                              Constants.PAGE_SIZE_ANNOUNCEMENTS_USER );
  }

  /**
   * Controller which allows admin to add announcement.
   * @param ann - object which holds data about announcement to be inserted to database
   * @param specs - specification tags under which the announcement will be shown
   * @return boolean value
   */

  @RequestMapping(value = "/admin/addAnnouncement", method = RequestMethod.POST)
  public boolean addAnnouncement(AnnouncementEntity ann,
                                 @RequestParam("specs") List<String> specs) {
    return dashboardManager.addAnnouncement(ann, specs);
  }

  /**
   * Controller which allows admin to add global announcement.
   * @param ann - object which holds data about global announcement to be inserted to database
   * @return boolean value
   */
  
  @RequestMapping(value = "/admin/addAnnouncementGlobal", method = RequestMethod.POST)
  public boolean addAnnouncement( AnnouncementEntity ann ) {
    return dashboardManager.addAnnouncement(ann);
  }

  /**
   * Controller which allows admin to edit announcement.
   * @param ann - holds data which will replace the old data
   * @param specs - list of doctor specializations
   * @return boolean value
   */

  @RequestMapping(value = "/admin/editAnnouncement", method = RequestMethod.POST)
  public boolean editAnnouncement(AnnouncementEntity ann,
                                  @RequestParam("specs") List<String> specs) {
    return dashboardManager.editAnnouncement(ann, specs);
  }

    /**
     * Controller which allows admin to edit announcement.
     * @param ann - holds data which will replace the old data
     * @return boolean value
     */

    @RequestMapping(value = "/admin/editAnnouncementGlobal", method = RequestMethod.POST)
    public boolean editAnnouncement(AnnouncementEntity ann) {
        return dashboardManager.editAnnouncement(ann);
    }

  /**
   * Controller for admin to get certain ammount of announcements.
   * @param page number of page to be returned
   * @return Page of conversations
   */

  @RequestMapping(value = "/admin/getAnnouncements/{page}", method = RequestMethod.GET)
  public Paginator<AnnouncementEntity> getAllAnnouncements( @PathVariable("page") Integer page ) {
    return dashboardManager.getLocalAnnouncements( page );
  }

  /**
   * Controller for admin to get main page announcements
   * @param page number of page to be returned
   * @return boolean value
   */
  
  @RequestMapping(value = "/admin/getMainPageAnnouncements/{page}", method = RequestMethod.GET)
  public Paginator<AnnouncementEntity> getMainPageAnnouncements( @PathVariable("page") Integer page ) {
    return dashboardManager.getMainPageAnnouncements( page );
  }

  /**
   * Controller which allows admin to get announcement of given id.
   * @param id Id of given announcement which will be returned
   * @return Announcement object found by ID
   */

  @RequestMapping(value = "/admin/getAnnouncementById/{id}", method = RequestMethod.GET)
  public AnnouncementEntity getAllAnnouncementById(@PathVariable("id") int id) {
   // System.out.println(id);
    return dashboardManager.getAnnouncementById(id);
  }

  /**
   * Method which allows admin to delete announcement.
   * @param id - Id of announcement which will be deleted
   */

  @RequestMapping(value = "/admin/deleteAnnouncement", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public void deleteAnnouncement(@RequestParam("id") Integer id) {
    dashboardManager.deleteAnnouncement(id);
  }

  @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
  public ResponseEntity handle() {
    return new ResponseEntity(HttpStatus.OK);
  }

}

