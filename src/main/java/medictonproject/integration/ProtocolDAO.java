package medictonproject.integration;

import medictonproject.model.EquipmentEntity;
import medictonproject.model.ProtocolEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class ProtocolDAO {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public boolean add(ProtocolEntity p){
        try{
            em.persist(p);
            return true;
        }catch( Exception e ){
            System.out.println(e);
            return false;
        }
    }

    public boolean delete(ProtocolEntity p){
        try{
            em.remove(p);
            return true;
        }catch( Exception e ){
            return false;
        }
    }
    public ProtocolEntity find(Integer eqId) {
        return em.find( ProtocolEntity.class, eqId );
    }
}
