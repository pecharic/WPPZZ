package medictonproject.buisness;

import medictonproject.integration.FaqDAO;
import medictonproject.model.FaqEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaqManager {
    private FaqDAO faqDao;

    @Autowired
    public FaqManager( FaqDAO faqDAO ){
        this.faqDao = faqDAO;
    }
    
    /**
     * Returns all the FAQ objects stored in database.
     *
     * @return List of FAQ objects
     */
    public List getFaq( int is_global ) {
        return faqDao.getAll( is_global );
    }
    
    /**
     * Permanently stores FAQ object in database.
     *
     * @param faq FAQ object to be added.
     */
    public void addFaq( FaqEntity faq, int is_global ) {
        faq.setIsGlobal( is_global );
        faqDao.add( faq );
    }
    
    /**
     * Edits information about FAQ object and updates it in database.
     *
     * @param faq FaQEntity object to be editet
     * @param is_global 0 for local FAQ, 1 for global FAQ
     */
    public void edit( FaqEntity faq, int is_global ) {
        faq.setIsGlobal( is_global );
        faqDao.update( faq );
    }
    
    /**
     * Deletes an FAQ object from database
     *
     * @param faqId id of FAQ object to be deleted
     */
    public void delete( int faqId ) {
        faqDao.delete( faqId );
    }
}
