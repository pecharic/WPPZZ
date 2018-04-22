package medictonproject.integration;

import medictonproject.model.FaqEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Service
public class FaqDAO implements medictonproject.interfaces.FaqInterface {
  @PersistenceContext
  private EntityManager em;

  /**
   * get all FAQ
   *
   * @param is_global boolean indicating which FAQ to return , global(1) or not global(0)
   *
   * @return List of all FAQ objects from database with is+global = 1
   */
  @Override
  public List getAll(int is_global) {
    Query q = em.createNativeQuery( "SELECT * FROM faq WHERE is_global=?1", FaqEntity.class );
    q.setParameter( 1, is_global );
    return q.getResultList();
  }

  /**
   * Add new faq to the database
   *
   * @param f FaqEntity object to add
   */
  @Override
  @Transactional
  public void add(FaqEntity f) {
    em.persist( f );
  }

  /**
   * Delete faq from the database
   *
   * @param faqId Integer id of faq to delete
   */
  @Override
  @Transactional
  public void delete(int faqId) {
    FaqEntity faq = find( faqId );
    em.remove( faq );
  }

  /**
   * Find faq in the database
   *
   * @param faqId Integer id of faq to find
   * @return FaqEntity object.
   */
  @Override
  @Transactional
  public FaqEntity find(int faqId) {
    return em.find( FaqEntity.class, faqId );
  }

  /**
   * Update faq in the database
   *
   * @param f FaqEntity object to update
   */
  @Override
  @Transactional
  public void update(FaqEntity f) {
    em.merge( f );
  }
}
