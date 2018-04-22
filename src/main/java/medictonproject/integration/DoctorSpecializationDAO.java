package medictonproject.integration;

import medictonproject.model.DoctorspecializationEntity;
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
public class DoctorSpecializationDAO implements medictonproject.interfaces.DoctorSpecializationInterface
{
    @PersistenceContext
    private EntityManager em;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DoctorSpecializationDAO.class);
    
    /**
     * Returns specialization by its ID.
     *
     * @param id Integer ID of specialization
     * @return Specialization object
     */
    @Override
    @Transactional
    public DoctorspecializationEntity getSpecialization( Integer id ) {
        return em.find( DoctorspecializationEntity.class, id );
    }
    
    /**
     * Returns all the specializations in database
     *
     * @return List of all the specializations in database
     */
    @Override
    @Transactional
    public List<DoctorspecializationEntity> getSpecializations(){
        return em.createNativeQuery("SELECT * from doctorspecialization ", DoctorspecializationEntity.class).getResultList();
    }
    
    /**
     * Returns specialization by its name.
     *
     * @param spec Name of specialization
     * @return Specialization object with all the attributes
     */
    @Override
    @Transactional
    public DoctorspecializationEntity getSpecialization( String spec ){
        Query q = em.createNativeQuery("Select * from doctorspecialization where name = ?1 LIMIT 1", DoctorspecializationEntity.class).setParameter(1,spec);
        
        try {
            DoctorspecializationEntity docSpec = (DoctorspecializationEntity) q.getSingleResult();
            return docSpec;
        } catch ( NoResultException e ) {
            logger.info("getSpecialization noResultException", e);
            return null;
        }
    }
    
    /**
     * Updates concrete specialization in database.
     *
     * @param doc New specialization object that is going to update the old one
     */
    @Override
    @Transactional
    public void update( DoctorspecializationEntity doc ){
        em.merge( doc );
    }
}
