package medictonproject.model;

import org.apache.catalina.User;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "survey_answer", schema = "swprojekt")
public class SurveyAnswerEntity {
    private int surveyAnswerId;
    private Timestamp answerDate;
    private UserEntity user;
    private OptionEntity option;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public UserEntity getUser() {
        return user;
    }

    public void setUser( UserEntity u ){
        this.user = u;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "option_id")
    public OptionEntity  getOption() {
        return option;
    }

    public void setOption( OptionEntity o  ){
        this.option = o;
    }

    @Id
    @Column(name = "survey_answer_id", nullable = false)
    public int getSurveyAnswerId() {
        return surveyAnswerId;
    }

    public void setSurveyAnswerId(int surveyAnswerId) {
        this.surveyAnswerId = surveyAnswerId;
    }

    @Basic
    @Column(name = "answer_date", nullable = false )
    public Timestamp getAnswerDate() {
        return answerDate;
    }

    public void setAnswerDate(Timestamp answerDate) {
        this.answerDate = answerDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SurveyAnswerEntity that = (SurveyAnswerEntity) o;

        if (surveyAnswerId != that.surveyAnswerId) return false;
        if (answerDate != null ? !answerDate.equals(that.answerDate) : that.answerDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = surveyAnswerId;
        result = 31 * result + (answerDate != null ? answerDate.hashCode() : 0);
        return result;
    }
}
