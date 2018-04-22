package medictonproject.interfaces;

import medictonproject.model.SurveyEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface SurveyInterface {
    @Transactional
    SurveyEntity getSurvey(Integer survey_id);

    @Transactional
    List getBriefSurveys();

    List<SurveyEntity> getSurveys();

    SurveyEntity getRecent();

    boolean existsPublished();

    @Transactional
    List<SurveyEntity> getPreparedSurveys();

    @Transactional
    boolean add(SurveyEntity s);

    @Transactional
    void delete(SurveyEntity s);

    @Transactional
    void update(SurveyEntity s);
}
