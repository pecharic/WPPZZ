package medictonproject.interfaces;

import medictonproject.model.SurveyAnswerEntity;
import org.springframework.transaction.annotation.Transactional;


public interface SurveyAnswerInterface {
    @Transactional
    void add (SurveyAnswerEntity a);

    @Transactional
    void delete(SurveyAnswerEntity s);

    @Transactional
    void update(SurveyAnswerEntity s);
}
