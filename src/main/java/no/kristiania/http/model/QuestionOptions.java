package no.kristiania.http.model;

public class QuestionOptions {
    private long id;
    private long questionId;
    private String question;
    private int range;
    private String nameMinVal;
    private String nameMaxVal;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public String getNameMinVal() {
        return nameMinVal;
    }

    public void setNameMinVal(String nameMinVal) {
        this.nameMinVal = nameMinVal;
    }

    public String getNameMaxVal() {
        return nameMaxVal;
    }

    public void setNameMaxVal(String nameMaxVal) {
        this.nameMaxVal = nameMaxVal;
    }
}
