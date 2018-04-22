package medictonproject.integration;

import medictonproject.model.OptionEntity;
import medictonproject.model.SurveyEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Service
public class OptionDAO implements medictonproject.interfaces.OptionInterface {
  @PersistenceContext
  private EntityManager em;

  /**
   * Returns all options to specific survey.
   *
   * @param s SurveyEntity object.
   * @return list of Options beloning to survey.
   */
  @Override
  public List getOptionsBySurvey(SurveyEntity s)
  {
    String query = "SELECT * FROM `option` WHERE survey_id=?1";
    Query q = em.createNativeQuery( query, OptionEntity.class );
    q.setParameter( 1, s.getSurveyId() );
    return q.getResultList();
  }

  /**
   * Returns  option by id.
   *
   * @param id  Integer  id of the option.
   * @return  Option with specific id
   */
  @Override
  public Object getOptionsById(int id){
    return em.createNativeQuery("SELECT * FROM `option` WHERE option_id = ?1", OptionEntity.class).setParameter(1, id ).getSingleResult();
  }


  /**
   * Add new  option in the the database
   *
   * @param o OptionEntity object
   */
  @Override
  @Transactional
  public void add(OptionEntity o)
  {
    em.persist( o );
  }


  /**
   * Delete option from the the database
   *
   * @param o OptionEntity object
   */
  @Override
  @Transactional
  public void delete(OptionEntity o)
  {
    em.remove( o );
  }


  /**
   * Update option in the the database
   *
   * @param o OptionEntity object
   */
  @Override
  @Transactional
  public void update(OptionEntity o)
  {
    em.merge( o );
  }
}
