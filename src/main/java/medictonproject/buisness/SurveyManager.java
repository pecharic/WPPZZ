package medictonproject.buisness;

import medictonproject.integration.OptionDAO;
import medictonproject.integration.SurveyAnswerDAO;
import medictonproject.integration.SurveyDAO;
import medictonproject.integration.UserDAO;
import medictonproject.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SurveyManager {

    private SurveyDAO surveyDAO;
    private UserDAO userDao;
    private OptionDAO optionDAO;
    private SurveyAnswerDAO survAnswer;
    @Autowired
    /**
     * SurveyManager constructor
     */
    public SurveyManager( SurveyDAO surveyDAO , UserDAO userDao, OptionDAO optionDAO, SurveyAnswerDAO survAnswer ){
        this.surveyDAO = surveyDAO;
        this.userDao = userDao;
        this.optionDAO = optionDAO;
        this.survAnswer = survAnswer;
    }

    /**
     * Updates option.
     * @param s SurveyEntity Survey to update
     * @param options List of strings list of options
     * @param survId Integer id of survey to update
     * @return boolean false if error occurs, true otherwise
     */
    public boolean update(SurveyEntity s, List<String> options , int survId ){
        SurveyEntity sur = surveyDAO.getSurvey( survId );
        s.setSurveyId( survId );
        
        // 2 published surveys can not exist in database at the same time
        if( s.getIsPublished() == 1 && surveyDAO.existsPublished() && sur.getIsPublished() == 0 ) {
            return false;
        }
        
        for( String opt : options ) {
            OptionEntity surContains = listContainsOption( sur.getOptions(), opt );
            OptionEntity sContains = listContainsOption( s.getOptions(), opt );
            if( surContains != null && sContains == null ) {
                s.getOptions().add( surContains );
                continue;
            }
            if( surContains == null && sContains == null ) {
                OptionEntity o = new OptionEntity();
                o.setText( opt );
                o.setSurvey( s );
                s.getOptions().add( o );
            }
        }
        surveyDAO.update(s);
        return true;
    }

    /**
     * Checks if option exist in list
     * @param options List of OptionEntity list of options
     * @param option String option to check.
     * @return OptionEntity
     */
    private OptionEntity listContainsOption( List<OptionEntity> options, String option ) {
        for( OptionEntity opt : options )
            if( opt.getText().equals( option ) )
                return opt;
        return null;
    }

    /**
     * Creates new survey
     * @param survey SurveyEntity survey to add
     * @param options List of strings options of added survey
     * @return boolean false if error occurs, true otherwise
     */
    public boolean add(SurveyEntity survey , List<String> options ){
           
       List<OptionEntity> ops = new ArrayList<>();
       for( String option : options ){
           OptionEntity tmp_option = new OptionEntity();
           tmp_option.setText( option );
           tmp_option.setSurvey(survey);
           ops.add(tmp_option);
       }
       survey.setOptions(ops);

       // 2 published surveys can not exist in database at the same time -
       // if this is about to be done, new survey's attribute isPublished is set to 0
       if( surveyDAO.existsPublished() )
           survey.setIsPublished( 0 );

       return surveyDAO.add(survey);
    }

    /**
     * Answers the survey
     * @param userId Integer ID of user that answers survey
     * @param optionId Integer ID of chosen option
     * @param surverId Integer ID of answered survey
     * @return boolean false if error occurs, true otherwise
     */
    public boolean answer( int userId , int optionId , int surverId ){
        UserEntity user;
        OptionEntity option;

        user = userDao.getUserById( userId );
        option  = (OptionEntity) optionDAO.getOptionsById( optionId );

        if( option.getSurvey().getSurveyId() != surverId )
            return false;

        SurveyAnswerEntity answer = new SurveyAnswerEntity();
        answer.setOption( option );
        answer.setUser( user );
        answer.setAnswerDate( new Timestamp( new Date().getTime()));

        survAnswer.add(answer);
        return true;
    }

    /**
     *  Returns all prepared surveys.
     * @return List of SurveyEntity objects.
     */
    public  List<SurveyEntity> getPrepared(){
        return  surveyDAO.getPreparedSurveys();
    }
    public SurveyEntity getRecentSurvey( int id ){
        UserEntity user = userDao.getUserById( id );
        SurveyEntity sur;
        
        sur = surveyDAO.getRecent();
        if( sur == null ) {
           // System.out.println("Ziadna survey nie je published.");
            return null;
        }
        
        for( SurveyAnswerEntity answer : user.getAnswers() ){
            if( answer.getOption().getSurvey().equals( sur )){
                return null;
            }
        }
        return sur;
    }
    
    public Paginator getSurveys( Integer page ) {
        List<SurveyEntity> surveyList = surveyDAO.getSurveys();
        Paginator<SurveyEntity> surveyPaginator = new Paginator<>( surveyList, page, Constants.PAGE_SIZE_SURVEYS );
        return surveyPaginator;
    }

    /**
     * Retrieve survey with speicifc id
     * @param surveyId Integer  ID of survey
     * @return SurveyEntity object
     */
    public SurveyEntity getSurvey( Integer surveyId ) {
        SurveyEntity survey = surveyDAO.getSurvey( surveyId );
        List<OptionEntity> options = survey.getOptions();
        int optionsNumber = options.size();
        int totalAnswers = 0;
        List<Double> answerStats = new ArrayList<>();

        for (int i = 0; i < optionsNumber; i++){
            int optionId = options.get(i).getOptionId();
            List<SurveyAnswerEntity> answers = survAnswer.getOptionAnswers( optionId );
            double answersNumber = answers.size();
            answerStats.add( answersNumber );
            totalAnswers += answersNumber;
        }

        if (totalAnswers > 0)
            for (int i = 0; i < answerStats.size(); i++)
                answerStats.set(i, answerStats.get(i) / totalAnswers);

        survey.setOptionsStat(answerStats);

        return survey;
    }

    /**
     *  Deletes survey
     * @param survey SurveyEnity survey to delete
     */
    public void delete( SurveyEntity survey ) {
        survey = surveyDAO.getSurvey( survey.getSurveyId() );
        surveyDAO.delete( survey );
    }
}
