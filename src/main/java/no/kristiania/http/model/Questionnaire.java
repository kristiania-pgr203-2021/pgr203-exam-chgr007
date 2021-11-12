package no.kristiania.http.model;

import java.util.ArrayList;
import java.util.List;

public class Questionnaire {

    private long id;
    private long personId;
    private String name;
    private List<Question> questions;

    public Questionnaire() {
        this.questions = new ArrayList<>();
    }

    public Questionnaire(List<Question> questions) {
        this.questions = questions;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }
    public List<Question> getQuestions() {
        return this.questions;
    }
    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public long getPersonId(){
        return personId;
    }

    public void setPersonId(long personId){
        this.personId = personId;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "Questionnaire{" +
                "id=" + id +
                ", personId=" + personId +
                ", name='" + name + '\'' +
                ", questions=" + questions +
                '}';
    }
}
