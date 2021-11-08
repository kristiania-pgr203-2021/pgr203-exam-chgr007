package no.kristiania.http.model;

import java.util.Arrays;
import java.util.Objects;

public class Question {

    private long id;
    private String question;
    private long questionnaireId;
    private boolean hasAnswerOptions = false;


    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(long questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question1 = (Question) o;
        return id == question1.id && Objects.equals(question, question1.question);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, question);
        result = 31 * result;
        return result;
    }

    public boolean getHasAnswerOptions() {
        return hasAnswerOptions;
    }

    public void setHasAnswerOptions(boolean hasAnswerOptions) {
        this.hasAnswerOptions = hasAnswerOptions;
    }
}
