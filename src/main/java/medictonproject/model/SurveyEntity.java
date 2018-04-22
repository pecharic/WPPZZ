package medictonproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "survey", schema = "swprojekt")
public class SurveyEntity {
    private int surveyId;
    private String question;
    private int isPrepared;
    private int isPublished;

    private List<OptionEntity> options = new ArrayList<OptionEntity>();

    private List<Double> optionsStat = new ArrayList<>();


    public void setOptionsStat(List<Double> optionsStat) {
        this.optionsStat = optionsStat;
    }

    @Transient
    public List<Double> getOptionsStat() {
        return this.optionsStat ;
    }

    @OneToMany(mappedBy = "survey", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    public List<OptionEntity> getOptions() {
        return options;
    }

    public void setOptions(List<OptionEntity> options) {
        this.options= options;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_id", nullable = false)
    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    @Basic
    @Column(name = "question", nullable = false, length = 3000)
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Basic
    @Column(name = "is_prepared", nullable = false)
    public int getIsPrepared() {
        return isPrepared;
    }

    public void setIsPrepared(int isPrepared) {
        this.isPrepared = isPrepared;
    }

    @Basic
    @Column(name = "is_published", nullable = false)
    public int getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(int isPublished) {
        this.isPublished = isPublished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SurveyEntity that = (SurveyEntity) o;

        if (surveyId != that.surveyId) return false;
        if (isPrepared != that.isPrepared) return false;
        if (isPublished != that.isPublished) return false;
        if (question != null ? !question.equals(that.question) : that.question != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = surveyId;
        result = 31 * result + (question != null ? question.hashCode() : 0);
        result = 31 * result + isPrepared;
        result = 31 * result + isPublished;
        return result;
    }
}
