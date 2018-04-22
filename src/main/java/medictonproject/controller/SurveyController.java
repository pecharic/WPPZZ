package medictonproject.controller;

import medictonproject.buisness.SurveyManager;
import medictonproject.model.Paginator;
import medictonproject.model.SurveyAnswerEntity;
import medictonproject.model.SurveyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping( "/survey" )
public class SurveyController {


    /*
     * instance of SurveyManager object
     */
    private SurveyManager surveyManager;


    /*
     * Constructor for SurveyController
     */

    @Autowired
    public SurveyController( SurveyManager surveyManager ){
        this.surveyManager = surveyManager;
    }

    /**
     * Controller that creates survey
     *
     * @param survey SurveyEntity object to be created
     * @param options List of strings as options to survey
     *
     * @return boolean false if error occurs , true otherwise
     */

    @RequestMapping(value = "/admin/add" , method = RequestMethod.POST ,  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public boolean createSurvey(SurveyEntity survey , @RequestParam("option") List<String> options ){
        return surveyManager.add( survey , options );
    }
    /**
     * Controller that updates survey
     *
     * @param survey SurveyEntity object to be updated
     * @param options List of strings as options to survey
     * @param survId  Integer id of survey to be updated
     * @return boolean false if error occurs , true otherwise
     */
    @RequestMapping(value = "/admin/update" , method = RequestMethod.POST ,  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public boolean createSurvey(SurveyEntity survey , @RequestParam("option") List<String> options, @RequestParam("survId") int survId ){
        return surveyManager.update( survey , options , survId );
    }

    /**
     * Controller for answering the survey.
     *
     * @param sas SurveyEntity object that answer belongs to
     * @param req HttpServletRequest
     * @param optionId id of option
     * @param surveyId id of survey
     * @return boolean false if error occurs , true otherwise
     */
    @RequestMapping(value = "/answer" , method = RequestMethod.POST ,  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public boolean answerSurvey(HttpServletRequest req, @RequestParam("optionId") Integer optionId,
                                 @RequestParam("surveyId") Integer surveyId){
        //System.out.println("gg" + (Integer) req.getAttribute("userId"));
        return surveyManager.answer((Integer) req.getAttribute("userId"), optionId, surveyId);
    }


    @RequestMapping(value="/admin/getSurveys/{page}", method = RequestMethod.GET)
    public Paginator getSurveys( @PathVariable("page") Integer page ) {
        return surveyManager.getSurveys( page );
    }

    /**
     * Controller that returns Survey to view by id.
     *
     * @param surveyId Integer ID of survey to return
     *
     * @return Survey object found by its ID.
     */
    @RequestMapping(value="/admin/getSurveyById/{id}", method = RequestMethod.GET)
    public SurveyEntity getSurveyById( @PathVariable("id") Integer surveyId ) {
        return surveyManager.getSurvey( surveyId );
    }

    /**
     * Controller that returns prepared Survey to view.
     *
     * @return List of prepared surveys
     */
    @RequestMapping( value = "/prepared")
    public List<SurveyEntity> getPrepared(){
        return surveyManager.getPrepared();
    }

    /**
     * Controller of deleting survey from database.
     *
     * @param survey Survey to be deleted
     */
    @RequestMapping( value = "/admin/delete", method = RequestMethod.POST ,  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void delete( SurveyEntity survey ) {
        surveyManager.delete( survey );
    }

    /**
     * Controller that returns recent Survey to view .
     *
     * @return Recent (published) survey
     */
    @RequestMapping( value = "/recentSurvey")
    public SurveyEntity getRecentSurvey(HttpServletRequest req ){
        return surveyManager.getRecentSurvey( (Integer)req.getAttribute("userId"));
    }
}
