package medictonproject.buisness;

import medictonproject.integration.AnnouncementDAO;
import medictonproject.integration.DoctorSpecializationDAO;
import medictonproject.integration.UserDAO;
import medictonproject.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardManager
{
  private UserDAO userDAO;
  private AnnouncementDAO announcementDAO;
  private DoctorSpecializationDAO doctorSpecializationDAO;
  
  @Autowired
  public DashboardManager( UserDAO userDAO,
                           AnnouncementDAO announcementDAO,
                           DoctorSpecializationDAO doctorSpecializationDAO ) {
    this.userDAO = userDAO;
    this.announcementDAO = announcementDAO;
    this.doctorSpecializationDAO = doctorSpecializationDAO;
  }
  
  /**
   * Method returns page of announcements - takes announcements from all the announcements.
   *
   * @param page - Page to be returned
   *
   * @return Page of announcements for main page.
   */
  public Paginator<AnnouncementEntity> getAnnouncements( Integer page,
                                                         Integer userId,
                                                         Integer itemsPerPage ) {
    UserEntity u = userDAO.getUserById( userId );
    List<AnnouncementEntity> announcements = announcementDAO.getAnnouncementsForUser( u );
  
    Paginator<AnnouncementEntity> annPaginator = new Paginator<>( announcements, page, itemsPerPage );
  
    return annPaginator;
  }

  public AnnouncementEntity getAnnouncementById( int userId ) {
    return announcementDAO.getAnnoucmentById( userId );
  }
  
  /**
   * Method stores a new announcement in database.
   *
   * @param ann - Announcement to be stored
   * @param specsInt - List of specializations for announcement
   *
   * @return boolean value depending on success
   */
  // adding local announcement
  public boolean addAnnouncement( AnnouncementEntity ann,
                                  List<String> specsInt ) {
    List<DoctorspecializationEntity> specsList = new ArrayList<>();
    for( String i : specsInt ) {
      //System.out.println(i);
      DoctorspecializationEntity spec = (DoctorspecializationEntity)doctorSpecializationDAO.getSpecialization( i );
      specsList.add( spec );
    }
    ann.setSpecializations( specsList );
    announcementDAO.add( ann );

   // System.out.println("Oznamenie pridane.");
    return true;
  }
  
  // adding global announcement
  public boolean addAnnouncement( AnnouncementEntity ann ) {
    announcementDAO.add( ann );
   // System.out.println("Oznamenie pridane.");
    return true;
  }
  
  /**
   * Method edits an announcement and updates it in database.
   *
   * @param ann - Announcement to be updated
   * @param specsInt - New set of specializations for announcement.
   *
   * @return true or false depending on success
   */
  public boolean editAnnouncement( AnnouncementEntity ann,
                                   List<String> specsInt ) {
    List<DoctorspecializationEntity> specsList = new ArrayList<>();
    for( String i : specsInt ) {
      DoctorspecializationEntity spec = (DoctorspecializationEntity)doctorSpecializationDAO.getSpecialization( i );
      specsList.add( spec );
    }
    ann.setSpecializations( specsList );
  
    announcementDAO.update( ann );
   // System.out.println("Oznamenie upravene.");
    return true;
  }

  /**
   * Method edits an announcement and updates it in database.
   *
   * @param ann - Announcement to be updated
   *
   * @return true or false depending on success
   */
  public boolean editAnnouncement( AnnouncementEntity ann ) {
    announcementDAO.update( ann );
   // System.out.println("Oznamenie upravene.");
    return true;
  }

  /**
   * Method returns page of announcements of specific specializations.
   *
   * @param page - Page to be returned
   *
   * @return Page of announcements of specific specializations
   */
  public Paginator<AnnouncementEntity> getLocalAnnouncements( Integer page ) {
    List<AnnouncementEntity> announcements = announcementDAO.getLocalAnnouncements();
    Paginator<AnnouncementEntity> annPaginator = new Paginator<>( announcements,
                                                                  page,
                                                                  Constants.PAGE_SIZE_ANNOUNCEMENTS_ADMIN );
    return annPaginator;
  }
  
  /**
   * Method returns page of announcements for main page.
   *
   * @param page - Page to be returned
   *
   * @return Page of announcements for main page.
   */
  public Paginator<AnnouncementEntity> getMainPageAnnouncements( Integer page ) {
    List<AnnouncementEntity> announcements = announcementDAO.getMainPageAnnouncements();
    Paginator<AnnouncementEntity> annPaginator = new Paginator<>( announcements,
      page,
      Constants.PAGE_SIZE_ANNOUNCEMENTS_ADMIN );
    return annPaginator;
  }
  
  /**
   * Method deletes an announcement with specific ID.
   *
   * @param id - ID of announcement
   */
  public void deleteAnnouncement(Integer id) {
    AnnouncementEntity a = announcementDAO.getAnnouncementById(id);
    announcementDAO.delete(a);
  }

}
