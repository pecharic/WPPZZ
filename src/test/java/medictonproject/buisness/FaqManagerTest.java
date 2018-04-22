package medictonproject.buisness;

import medictonproject.integration.FaqDAO;
import medictonproject.model.FaqEntity;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class FaqManagerTest {

    @Test
    public void addFaq_insertsIntoDatabase(){
        FaqDAO faqDAO = mock(FaqDAO.class);
        FaqEntity faqEntity = mock(FaqEntity.class);
        FaqManager faqManager = new FaqManager(faqDAO);

        faqManager.addFaq(faqEntity, 1);

        verify(faqDAO).add(faqEntity);
    }

    @Test
    public void addFaq_entityIsSetToGlobal(){
        FaqDAO faqDAO = mock(FaqDAO.class);
        FaqEntity faqEntity = mock(FaqEntity.class);
        FaqManager faqManager = new FaqManager(faqDAO);

        faqManager.addFaq(faqEntity, 1);

        verify(faqEntity).setIsGlobal(1);
    }

    @Test
    public void edit_editsInDatabase(){
        FaqDAO faqDAO = mock(FaqDAO.class);
        FaqEntity faqEntity = mock(FaqEntity.class);
        FaqManager faqManager = new FaqManager(faqDAO);

        faqManager.edit(faqEntity, 1);

        verify(faqDAO).update(faqEntity);
    }

    @Test void edit_entityIsSetGlobal(){
        FaqDAO faqDAO = mock(FaqDAO.class);
        FaqEntity faqEntity = mock(FaqEntity.class);
        FaqManager faqManager = new FaqManager(faqDAO);

        faqManager.edit(faqEntity, 1);

        verify(faqEntity).setIsGlobal(1);
    }
    @Test
    public void delete_deletesFromDatabase(){
        FaqDAO faqDAO = mock(FaqDAO.class);
        FaqManager faqManager = new FaqManager(faqDAO);

        faqManager.delete(0);

        verify(faqDAO).delete(0);
    }

}