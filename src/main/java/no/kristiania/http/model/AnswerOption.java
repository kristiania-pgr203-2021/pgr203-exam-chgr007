package no.kristiania.http.model;

public class AnswerOption {
    private long id, questionId;
    private Enum<AnswerType> answerType;
    private String value;
    private String name;

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

    public Enum<AnswerType> getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = AnswerType.valueOf(answerType);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
