package medictonproject.controller;

import medictonproject.buisness.HomeManager;
import medictonproject.buisness.IntuoManager;
import medictonproject.model.DoctorspecializationEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/home")
public class HomeController {

  /**
   * instance of HomeManager object
   */
  private HomeManager homeManager;
  private IntuoManager intuoManager;
  private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

  /**
   * HomeManager constructor
   */

  @Autowired
  HomeController(HomeManager homeManager , IntuoManager intuoManager) {
    this.homeManager = homeManager;
    this.intuoManager = intuoManager;
  }

  /**
   * Controller for getting announcements
   *
   * @return List of all the announcements for main page
   */

  @RequestMapping( value ="/getAnnouncements" , method = RequestMethod.GET)
  public List getAnnouncements() {
    return homeManager.getAnnouncements();
  }

  /**
   * Controller for specializations.
   *
   * @return List of all the specializations in database
   */

  @RequestMapping(value ="/getSpecializations")
  public List<DoctorspecializationEntity> getSpecializations(){
    return homeManager.getSpecializations();
  }

}
