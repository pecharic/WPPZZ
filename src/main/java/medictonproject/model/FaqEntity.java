package medictonproject.model;

import javax.persistence.*;

@Entity
@Table(name = "faq", schema = "swprojekt")
public class FaqEntity {
    private int faqId;
    private String question;
    private String answer;
    private int isGlobal;

    @Id
    @Column(name = "faq_id", nullable = false)
    public int getFaqId() {
        return faqId;
    }

    public void setFaqId(int faqId) {
        this.faqId = faqId;
    }

    @Basic
    @Column(name = "question", nullable = false, length = 200)
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Basic
    @Column(name = "answer", nullable = false, length = 3000)
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Basic
    @Column(name = "is_global", nullable = false)
    public int getIsGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(int isGlobal) {
        this.isGlobal = isGlobal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FaqEntity faqEntity = (FaqEntity) o;

        if (faqId != faqEntity.faqId) return false;
        if (isGlobal != faqEntity.isGlobal) return false;
        if (question != null ? !question.equals(faqEntity.question) : faqEntity.question != null) return false;
        if (answer != null ? !answer.equals(faqEntity.answer) : faqEntity.answer != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = faqId;
        result = 31 * result + (question != null ? question.hashCode() : 0);
        result = 31 * result + (answer != null ? answer.hashCode() : 0);
        result = 31 * result + isGlobal;
        return result;
    }
}
