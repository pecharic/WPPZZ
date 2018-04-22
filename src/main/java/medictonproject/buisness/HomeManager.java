package medictonproject.buisness;

import medictonproject.integration.AnnouncementDAO;
import medictonproject.integration.DoctorSpecializationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeManager
{
  private AnnouncementDAO announcementDAO;
  private DoctorSpecializationDAO docDao;

  @Autowired
  HomeManager(AnnouncementDAO announcementDAO , DoctorSpecializationDAO docDao ) {
    this.announcementDAO = announcementDAO;
    this.docDao = docDao;
  }
  /**
   * Returns list of annoucements to controller
   *
   * @return List of AnnoucementEntity
   */
  public List getAnnouncements() {
    return announcementDAO.getMainPageAnnouncements();
  }

  /**
   * Returns list of specializations to controller
   *
   * @return List of SpecializationEntity
   */
  public List getSpecializations(){
    return docDao.getSpecializations();
  }
}
