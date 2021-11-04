package no.kristiania.http.model;

public class Question {

    private long id;
    private String question;
    private String amswer;

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setAmswer(String amswer) {
        this.amswer = amswer;
    }

    public String getAmswer() {
        return amswer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
