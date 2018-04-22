package medictonproject.buisness;

import medictonproject.integration.OptionDAO;
import medictonproject.integration.SurveyAnswerDAO;
import medictonproject.integration.SurveyDAO;
import medictonproject.integration.UserDAO;
import medictonproject.model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SurveyManagerTest {
    private SurveyDAO surveyDAO;
    private SurveyEntity surveyEntity;
    private SurveyEntity surveyEntityOld;
    private SurveyEntity surveyEntityNew;
    private UserDAO userDAO;
    private UserEntity userEntity;
    private OptionDAO optionDAO;
    private OptionEntity optionEntity;
    private SurveyAnswerDAO surveyAnswerDAO;
    private SurveyAnswerEntity surveyAnswerEntity;
    private SurveyManager surveyManager;

    int surveyId = 0;
    int pageNumber = 1;
    int userId = 2;
    int optionId = 3;

    private void initializeFields(){
        surveyDAO = mock(SurveyDAO.class);
        surveyEntity = mock(SurveyEntity.class);
        surveyEntityOld = mock(SurveyEntity.class);
        surveyEntityNew = mock(SurveyEntity.class);
        userDAO = mock(UserDAO.class);
        userEntity = mock(UserEntity.class);
        optionDAO = mock(OptionDAO.class);
        optionEntity = mock(OptionEntity.class);
        surveyAnswerDAO = mock(SurveyAnswerDAO.class);
        surveyAnswerEntity = mock(SurveyAnswerEntity.class);
        surveyManager = new SurveyManager(surveyDAO, userDAO, optionDAO, surveyAnswerDAO);
    }

    @Test
    public void delete_getsSurveyId(){
        initializeFields();

        surveyManager.delete(surveyEntity);

        verify(surveyEntity).getSurveyId();
    }

    @Test
    public void delete_getsSurveyToDelete(){
        initializeFields();

        when( surveyEntity.getSurveyId() ).thenReturn( surveyId );

        surveyManager.delete(surveyEntity);

        verify(surveyDAO).getSurvey(surveyId);
    }

    @Test
    public void delete_deletesSurvey(){
        initializeFields();

        when( surveyEntity.getSurveyId() ).thenReturn( surveyId );
        when( surveyDAO.getSurvey(surveyId) ).thenReturn( surveyEntity );

        surveyManager.delete(surveyEntity);

        verify(surveyDAO).delete(surveyEntity);
    }

    @Test
    public void getSurveys_getsSurveyList(){
        initializeFields();

        surveyManager.getSurveys(pageNumber);

        verify(surveyDAO).getSurveys();
    }

    @Test
    public void getSurveysWithEmptyList_returnsEmptyPaginator() {
        initializeFields();

        when(surveyDAO.getSurveys()).thenReturn(new ArrayList<>());

        Paginator paginator = surveyManager.getSurveys(pageNumber);
        int expected = 0;
        int actual = paginator.getTotalItems();

        assertEquals(expected, actual);
    }

    @Test
    public void getRecentSurvey_findsUser(){
        initializeFields();

        surveyManager.getRecentSurvey(userId);

        verify(userDAO).getUserById(userId);
    }

    @Test
    public void getRecentSurvey_findsRecentSurvey(){
        initializeFields();

        surveyManager.getRecentSurvey(userId);

        verify(surveyDAO).getRecent();
    }

    @Test
    public void getRecentSurveyNoResentSurvey_returnsNull(){
        initializeFields();

        when( surveyDAO.getRecent() ).thenReturn( null );

        SurveyEntity result = surveyManager.getRecentSurvey(userId);

        assertNull(result);
    }

    @Test
    public void getRecentSurvey_getsUserAnswers(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        when( surveyDAO.getRecent() ).thenReturn( surveyEntity );

        surveyManager.getRecentSurvey(userId);

        verify(userEntity).getAnswers();
    }

    @Test
    public void getRecentSurveyIfUserAlreadyAnswered_returnsNull(){
        initializeFields();

        List<SurveyAnswerEntity> userAnswers = new ArrayList<>();
        userAnswers.add(surveyAnswerEntity);

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        when( surveyDAO.getRecent() ).thenReturn( surveyEntity );
        when( userEntity.getAnswers() ).thenReturn( userAnswers );
        when( surveyAnswerEntity.getOption() ).thenReturn( optionEntity );
        when( optionEntity.getSurvey() ).thenReturn( surveyEntity );

        SurveyEntity result = surveyManager.getRecentSurvey(userId);

        assertNull(result);
    }

    @Test
    public void answer_findsUser(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        when( optionDAO.getOptionsById(optionId) ).thenReturn( optionEntity );
        when( optionEntity.getSurvey() ).thenReturn( surveyEntity );
        when( surveyEntity.getSurveyId() ).thenReturn( surveyId );

        surveyManager.answer(userId, optionId, surveyId);

        verify(userDAO).getUserById(userId);
    }

    @Test
    public void answer_findsOption(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        when( optionDAO.getOptionsById(optionId) ).thenReturn( optionEntity );
        when( optionEntity.getSurvey() ).thenReturn( surveyEntity );
        when( surveyEntity.getSurveyId() ).thenReturn( surveyId );

        surveyManager.answer(userId, optionId, surveyId);

        verify(optionDAO).getOptionsById(optionId);
    }

    @Test
    public void answerOptionDoesNotBelongToSurvey_returnsFalse(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        when( optionDAO.getOptionsById(optionId) ).thenReturn( optionEntity );
        when( optionEntity.getSurvey() ).thenReturn( surveyEntity );
        when( surveyEntity.getSurveyId() ).thenReturn( surveyId + 10 );

        boolean result = surveyManager.answer(userId, optionId, surveyId);

        assertFalse(result);
    }

    @Test
    public void answer_addsAnswerToSurvey(){
        initializeFields();

        when( userDAO.getUserById(userId) ).thenReturn( userEntity );
        when( optionDAO.getOptionsById(optionId) ).thenReturn( optionEntity );
        when( optionEntity.getSurvey() ).thenReturn( surveyEntity );
        when( surveyEntity.getSurveyId() ).thenReturn( surveyId );

        surveyManager.answer(userId, optionId, surveyId);

        verify(surveyAnswerDAO).add(anyObject());
    }

    @Test
    public void add_setsOptionsToNewSurvey(){
        initializeFields();

        surveyManager.add(surveyEntity, new ArrayList<String>());

        verify(surveyEntity).setOptions(anyObject());
    }

    @Test
    public void addTwoSurveysArePublishedAtSameTime_setsNewOneUnpublished(){
        initializeFields();

        when( surveyEntity.getIsPublished() ).thenReturn( 1 );
        when( surveyDAO.existsPublished() ).thenReturn( true );

        surveyManager.add(surveyEntity, new ArrayList<String>());

        verify(surveyEntity).setIsPublished(0);
    }

    @Test
    public void addSuccessfully_returnsTrue(){
        initializeFields();

        when( surveyDAO.add(surveyEntity) ).thenReturn( true );

        boolean result = surveyManager.add(surveyEntity, new ArrayList<String>());

        assertTrue(result);
    }

    @Test
    public void addUnsuccessfully_returnsTrue(){
        initializeFields();

        when( surveyDAO.add(surveyEntity) ).thenReturn( false );

        boolean result = surveyManager.add(surveyEntity, new ArrayList<String>());

        assertFalse(result);
    }

    @Test
    public void update_findsOldSurvey(){
        initializeFields();

        List<String> optionsList = new ArrayList<>();
        optionsList.add("Option1");
        optionsList.add("Option2");
        optionsList.add("Option3");

        when( surveyDAO.getSurvey(surveyId) ).thenReturn( surveyEntityOld );
        when( surveyEntityNew.getIsPublished() ).thenReturn( 0 );
        when( surveyDAO.existsPublished() ).thenReturn( false );
        when( surveyEntityNew.getOptions() ).thenReturn( new ArrayList<>() );
        when( surveyEntityOld.getOptions() ).thenReturn( new ArrayList<>() );

        surveyManager.update(surveyEntityNew, optionsList, surveyId);

        verify(surveyDAO).getSurvey(surveyId);
    }

    @Test
    public void update_setsNewSurveyId(){
        initializeFields();

        List<String> optionsList = new ArrayList<>();
        optionsList.add("Option1");
        optionsList.add("Option2");
        optionsList.add("Option3");

        when( surveyDAO.getSurvey(surveyId) ).thenReturn( surveyEntityOld );
        when( surveyEntityNew.getIsPublished() ).thenReturn( 0 );
        when( surveyDAO.existsPublished() ).thenReturn( false );
        when( surveyEntityNew.getOptions() ).thenReturn( new ArrayList<>() );
        when( surveyEntityOld.getOptions() ).thenReturn( new ArrayList<>() );

        surveyManager.update(surveyEntityNew, optionsList, surveyId);

        verify(surveyEntityNew).setSurveyId(surveyId);
    }

    @Test
    public void updateThereAreTwoPublishedSurveys_returnsFalse(){
        initializeFields();

        List<String> optionsList = new ArrayList<>();
        optionsList.add("Option1");
        optionsList.add("Option2");
        optionsList.add("Option3");

        when( surveyDAO.getSurvey(surveyId) ).thenReturn( surveyEntityOld );
        when( surveyEntityNew.getIsPublished() ).thenReturn( 1 );
        when( surveyDAO.existsPublished() ).thenReturn( true );
        when( surveyEntityNew.getOptions() ).thenReturn( new ArrayList<>() );
        when( surveyEntityOld.getOptions() ).thenReturn( new ArrayList<>() );

        boolean result = surveyManager.update(surveyEntityNew, optionsList, surveyId);

        assertFalse(result);
    }


    @Test
    public void update_addsNewSurveyToDao(){
        initializeFields();

        List<String> optionsList = new ArrayList<>();
        optionsList.add("Option1");
        optionsList.add("Option2");
        optionsList.add("Option3");

        when( surveyDAO.getSurvey(surveyId) ).thenReturn( surveyEntityOld );
        when( surveyEntityNew.getIsPublished() ).thenReturn( 0 );
        when( surveyDAO.existsPublished() ).thenReturn( false );
        when( surveyEntityNew.getOptions() ).thenReturn( new ArrayList<>() );
        when( surveyEntityOld.getOptions() ).thenReturn( new ArrayList<>() );

        surveyManager.update(surveyEntityNew, optionsList, surveyId);

        verify(surveyDAO).update(surveyEntityNew);
    }
}