package medictonproject.integration;

import medictonproject.model.SurveyEntity;
import medictonproject.model.UserEntity;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class SurveyDAO implements medictonproject.interfaces.SurveyInterface {

  private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SurveyDAO.class);

  @PersistenceContext
  private EntityManager em;


  /**
   * Returns one exact Survey - for overviewing/editing
   *
   * @param survey_id Integer ID of the survey
   * @return SurveyEntity object
   */
  @Override
  @Transactional
  public SurveyEntity getSurvey(Integer survey_id)
  {
    String query = "SELECT * FROM survey WHERE survey_id=?1";
    Query q = em.createNativeQuery( query, SurveyEntity.class );
    q.setParameter( 1, survey_id );
    return (SurveyEntity)q.getSingleResult();
  }
  
  //
  /**
   * Returns ID and a question of Surveys - for brief printing on page
   *
   * @return list of unprepared SurveyEntity objects
   */
  @Override
  @Transactional
  public List getBriefSurveys()
  {
    String query = "SELECT survey_id,question FROM survey WHERE is_prepared=0";
    Query q = em.createNativeQuery( query, SurveyEntity.class );
    return q.getResultList();
  }

  /**
   * Returns all surveys
   *
   * @return list of SurveyEntity objects
   */
  @Override
  public List<SurveyEntity> getSurveys() {
    Query q = em.createNativeQuery( "SELECT * FROM survey", SurveyEntity.class );
    return q.getResultList();
  }

  /**
   * Returns recent survey.
   *
   * @return recent Survey
   */
  @Override
  public SurveyEntity getRecent(){
    Query q = em.createNativeQuery("Select * from survey WHERE is_published = 1 LIMIT 1", SurveyEntity.class);
    
    try {
      SurveyEntity sur = (SurveyEntity) q.getSingleResult();
      return sur;
    } catch ( NoResultException e ) {
      logger.info("getRecent", e);
      return null;
    }
  }
  
  /**
   * Returns whether a published survey exists in database or not.
   *
   * @return 0 for not existing object, 1 for existing
   */
  @Override
  public boolean existsPublished() {
    Query q = em.createNativeQuery("Select * from survey WHERE is_published = 1 LIMIT 1", SurveyEntity.class);
    try {
      q.getSingleResult();
    } catch( NoResultException e ) {
      logger.info("doesNotExistPublished", e);
      return false;
    }
    return true;
  }
  
  // returns all the prepared Surveys - for printing prepared surveys while creating
  // a new Survey
  /**
   * Returns all prepared surveys.
   *
   * @return lsit of prepared Surveys
   */
  @Override
  @Transactional
  public List<SurveyEntity> getPreparedSurveys()
  {
    String query = "SELECT * FROM survey WHERE is_prepared=1";
    Query q = em.createNativeQuery( query, SurveyEntity.class );
    return q.getResultList();
  }
  /**
   * Adds new survey to the database
   *
   *  @param s SurveyEntity object
   */
  @Override
  @Transactional
  public boolean add(SurveyEntity s)
  {
    try {
      em.persist( s );
    } catch( Exception e ) {
      logger.info("add", e);
      return false;
    }
    return true;
  }

  /**
   * Delete survey from the database
   *
   * @param s SurveyEntity object
   */
  @Override
  @Transactional
  public void delete(SurveyEntity s)
  {
    em.remove( s );
  }
  /**
   * Update survey in the the database
   *
   * @param s SurveyEntity object
   */
  @Override
  @Transactional
  public void update(SurveyEntity s)
  {
    em.merge( s );
  }
}
