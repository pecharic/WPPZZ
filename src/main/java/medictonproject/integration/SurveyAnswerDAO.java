package medictonproject.integration;

import medictonproject.model.SurveyAnswerEntity;
import medictonproject.model.SurveyEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
public class SurveyAnswerDAO implements medictonproject.interfaces.SurveyAnswerInterface {

    @PersistenceContext
    EntityManager em;

    /**
     * Add new  surveyanswer in the the database
     *
     * @param a SurveyAnswerEntity object
     */
    @Override
    @Transactional
    public void add(SurveyAnswerEntity a)
    {
        em.persist( a );
    }


    /**
     * Delete surveyanswer from the the database
     *
     * @param s SurveyAnswerEntity object
     */
    @Override
    @Transactional
    public void delete(SurveyAnswerEntity s)
    {
        em.remove( s );
    }


    /**
     * Update survey answer in the the database
     *
     * @param s SurveyAnswerEntity object
     */
    @Override
    @Transactional
    public void update(SurveyAnswerEntity s)
    {
        em.merge( s );
    }

    @Transactional
    public List<SurveyAnswerEntity> getOptionAnswers( int optionId ){
        String query = "SELECT * FROM survey_answer WHERE option_id = ?1";
        Query q = em.createNativeQuery( query, SurveyAnswerEntity.class );
        q.setParameter(1, optionId);
        return q.getResultList();
    }

}
