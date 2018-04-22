package medictonproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "`option`", schema = "swprojekt")
public class OptionEntity {
    private int optionId;
    private String text;
    @JsonIgnore
    private SurveyEntity survey;
    @JsonIgnore
    private List<SurveyAnswerEntity> answers;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    public SurveyEntity getSurvey() {
        return survey;
    }

    public void setSurvey(SurveyEntity survey ) {
        this.survey = survey;
    }

    @OneToMany(mappedBy = "option")
    public List<SurveyAnswerEntity> getAnswers(){
        return  this.answers;
    }

    public void setAnswers( List<SurveyAnswerEntity> answers){
        this.answers = answers;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "option_id", nullable = false)
    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    @Basic
    @Column(name = "`text`", nullable = false, length = 500)

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OptionEntity that = (OptionEntity) o;

        if (optionId != that.optionId) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = optionId;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
