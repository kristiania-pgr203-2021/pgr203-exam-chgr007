package no.kristiania.http.model;

import java.util.Arrays;
import java.util.Objects;

public class Question {

    private long id;
    private String question;
    private String answer;
    private Answer[] allAnswers;

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question1 = (Question) o;
        return id == question1.id && Objects.equals(question, question1.question) && Objects.equals(answer, question1.answer) && Arrays.equals(allAnswers, question1.allAnswers);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, question, answer);
        result = 31 * result + Arrays.hashCode(allAnswers);
        return result;
    }
}
