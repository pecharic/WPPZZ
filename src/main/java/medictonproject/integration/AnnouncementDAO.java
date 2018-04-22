package medictonproject.integration;

import medictonproject.model.AnnouncementEntity;
import medictonproject.model.DoctorspecializationEntity;
import medictonproject.model.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnnouncementDAO implements medictonproject.interfaces.AnnouncementInterface {

  @PersistenceContext
  private EntityManager em;
  
  /**
   * Returns all the announcements that belong to main page.
   *
   * @return List of main page announcements
   */
  @Override
  public List getMainPageAnnouncements() {
    return em.createNativeQuery("SELECT * FROM announcement WHERE is_announcement = 0 ORDER BY date ASC" , AnnouncementEntity.class).getResultList();
  }
  
  /**
   * Returns all the local announcements (announcements for groups registered users).
   *
   * @return List of local announcements
   */
  @Override
  public List getLocalAnnouncements() {
    return em.createNativeQuery("SELECT * FROM announcement WHERE is_announcement = 1 ORDER BY date ASC" , AnnouncementEntity.class).getResultList();
  }
  
  /**
   * Returns announcement by id.
   *
   * @param id Integer ID of announcement
   * @return Announcement object
   */
  @Override
  public AnnouncementEntity getAnnoucmentById(int id) {
    return (AnnouncementEntity) em.createNativeQuery("SELECT * FROM announcement WHERE announcement_id = ?1" , AnnouncementEntity.class).setParameter(1,id).getSingleResult();
  }
  
  /**
   * Returns all the announcements that for the specific user (his specializations)
   *
   * @return List of local announcements
   */
  @Override
  public List<AnnouncementEntity> getAnnouncementsForUser(UserEntity u) {
    List<DoctorspecializationEntity> specs = u.getSpecializations();
    List<AnnouncementEntity> annoucementsList = new ArrayList<>();

    for( DoctorspecializationEntity spec : specs )
      annoucementsList.addAll( spec.getAnnoucements() );

    return annoucementsList;
  }
  
  /**
   * Returns announcement by its id.
   *
   * @param announcementId Integer ID of announcement
   * @return Announcement object
   */
  @Override
  @Transactional
  public AnnouncementEntity getAnnouncementById(int announcementId) {
    AnnouncementEntity a = em.find( AnnouncementEntity.class, announcementId );
    return a;
  }
  
  /**
   * Adds an announcement into database
   *
   * @param a Announcement object that is going to be added
   */
  @Override
  @Transactional
  public void add(AnnouncementEntity a)
  {
    em.persist( a );
  }
  
  /**
   * Deletes a specific announcement from database.
   *
   * @param a Announcement object that is going to be deleted
   */
  @Override
  @Transactional
  public void delete(AnnouncementEntity a) {
    em.remove( a );
  }
  
  /**
   * Updates attributes of announcement in database.
   *
   * @param a Updated announcement object
   */
  @Override
  @Transactional
  public void update(AnnouncementEntity a)
  {
    em.merge( a );
  }
}
