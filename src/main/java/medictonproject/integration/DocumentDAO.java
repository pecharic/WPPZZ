package medictonproject.integration;


import medictonproject.model.DocumentEntity;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class DocumentDAO implements medictonproject.interfaces.DocumentInterface {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DocumentDAO.class);

    @PersistenceContext
    EntityManager em;
    
    /**
     * Adds information about document stored on server into database.
     *
     * @param d Document object
     */
    @Override
    @Transactional
    public boolean add(DocumentEntity d){
        try {
            em.persist(d);
        } catch ( Exception ex ) {
            logger.info("Document add exception", ex);
            return false;
        }
        return true;
    }
}
