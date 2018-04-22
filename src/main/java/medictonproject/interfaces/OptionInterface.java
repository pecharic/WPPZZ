package medictonproject.interfaces;

import medictonproject.model.OptionEntity;
import medictonproject.model.SurveyEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface OptionInterface {
    List getOptionsBySurvey (SurveyEntity s);

    Object getOptionsById(int id);

    @Transactional
    void add (OptionEntity o);

    @Transactional
    void delete(OptionEntity o);

    @Transactional
    void update(OptionEntity o);
}
