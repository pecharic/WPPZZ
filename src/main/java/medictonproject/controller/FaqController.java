package medictonproject.controller;

import medictonproject.buisness.FaqManager;
import medictonproject.model.FaqEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/faq")
public class FaqController {

    /*
     * instance of FaqManager object
     */
    private FaqManager faqManager;

    /*
     * Contructor of FaqController class
     */

    @Autowired
    public FaqController( FaqManager faqManager ){
        this.faqManager = faqManager;
    }


    /**
     * Controller that returns list of global FAQs to the user view
     *
     * @return List of FaQEntity object
     */
    @RequestMapping(value = "/getGlobal", method = RequestMethod.GET)
    public List getGlobalFaq(){
        return faqManager.getFaq(1);
    }

    /**
     * Controller that adds new global faq to the database
     *
     * @param faq FaqEntity object that holds faq data
     */
    @RequestMapping(value = "/admin/addGlobal", method = RequestMethod.POST)
    public void addGlobalFaq( FaqEntity faq ) {
        faqManager.addFaq( faq, 1 );
    }

    /**
     * Controller that edits global faq in database
     *
     * @param faq FaQEntity object to be editet
     */
    @RequestMapping(value = "/admin/editGlobal", method = RequestMethod.PUT)
    public void editGlobalFaq( FaqEntity faq ) {
        faqManager.edit( faq, 1 );
    }


    /**
     * Controller that returns local faq
     *
     * @return List of all faqs.
     */
    @RequestMapping(value = "/getLocal", method = RequestMethod.GET)
    public List getLocalFaq(){
        return faqManager.getFaq( 0 );
    }
    /**
     * Controller that adds new local faq to the database
     *
     * @param faq FaqEntity object to add.
     */
    @RequestMapping(value = "/admin/addLocal", method = RequestMethod.POST)
    public void addLocalFaq( FaqEntity faq ) {
        faqManager.addFaq( faq, 0 );
    }
    
    // id must be sent in request!
    /**
     * Controller that edits local faq in database
     *
     * @param faq FaQEntity object to be editet
     */
    @RequestMapping(value = "/admin/editLocal", method = RequestMethod.PUT)
    public void editLocalFaq( FaqEntity faq ) {
        faqManager.edit( faq, 0 );
    }
    /**
     * Controller that deletes faq in database
     *
     * @param faqId id of faq to be deleted. FaqId has to be sent with request.
     */
    @RequestMapping(value = "/admin/delete", method = RequestMethod.GET)
    public void deleteFaq( @RequestParam("faqId") int faqId ) {
        faqManager.delete( faqId );
    }
}
