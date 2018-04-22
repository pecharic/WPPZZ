package medictonproject.integration;

import medictonproject.model.EquipmentEntity;
import medictonproject.model.UserEntity;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Service
public class EquipmentDAO implements medictonproject.interfaces.EquipmentInterface {

  private static final org.slf4j.Logger logger = LoggerFactory.getLogger(EquipmentDAO.class);

  @PersistenceContext
  private EntityManager em;
  
  // get Equipment by User ID
  /**
   * Returns all the equipment by user's Integer ID.
   *
   * @param userId Integer ID of user
   * @return List of equipment belonging to user with ID userId
   */
  @Override
  @Transactional
  public List<EquipmentEntity> getAll(Integer userId)
  {
    Query q  = em.createNativeQuery( "SELECT * FROM equipment WHERE user_id=?1", EquipmentEntity.class );
    q.setParameter( 1, userId );
    return q.getResultList();
  }
  
  /**
   * Returns equipment with its concrete id.
   *
   * @param eqId Integer ID of equipment
   * @return Equipment object with ID eqId
   */
  @Override
  public EquipmentEntity getEq(Integer eqId)
  {
    Query q  = em.createNativeQuery( "SELECT * FROM equipment WHERE equipment_id=?1", EquipmentEntity.class );
    q.setParameter( 1, eqId );
    return (EquipmentEntity) q.getSingleResult();
  }
  
  /**
   * Adds new equipment into database.
   *
   * @param e Equipment object that is about to be added
   */
  @Override
  @Transactional
  public boolean add(EquipmentEntity e) {
    try {
      em.persist(e);
    } catch ( Exception ex ) {
      logger.info("equipmentDAO add exception", ex);
      return false;
    }
    return true;
  }
  
  /**
   * Deletes equipment from database.
   *
   * @param e Equipment object that is about to be deleted
   */
  @Override
  @Transactional
  public void delete(EquipmentEntity e) {
    em.remove( e );
  }
  
  /**
   * Updates equipment in database.
   *
   * @param e Equipment object that is about to be updated
   */
  @Override
  @Transactional
  public boolean update(EquipmentEntity e) {
    try {
      em.merge(e);
    } catch ( Exception ex ) {
      logger.info("EquipmentDAO exception update", ex);
      return false;
    }
    return true;
  }
  
  /**
   * Returns one concrete equipment by its Integer ID
   *
   * @param eqId Integer ID of equipment
   */
  @Override
  @Transactional
  public EquipmentEntity find(Integer eqId) {
    return em.find( EquipmentEntity.class, eqId );
  }
}
