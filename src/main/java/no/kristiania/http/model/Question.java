package no.kristiania.http.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Question {

    private long id;
    private String question;
    private long questionnaireId;
    private QuestionType questionType;
    private boolean hasAnswerOptions = false;
    private List<QuestionOptions> questionOptionsList;

    public QuestionType getQuestionType() {
        return this.questionType;
    }
    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }
    public Question() {
        questionOptionsList = new ArrayList<>();
    }
    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public boolean getHasAnswerOptions() {
        return hasAnswerOptions;
    }

    public List<QuestionOptions> getQuestionOptionList() {
        return questionOptionsList;
    }

    public void addAnswerOption(QuestionOptions questionOptionsList) {
        this.questionOptionsList.add(questionOptionsList);
    }
    public void setAnswerOptionList(List<QuestionOptions> questionOptionsList) {
        this.questionOptionsList = questionOptionsList;
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

    public void setHasAnswerOptions(boolean hasAnswerOptions) {
        this.hasAnswerOptions = hasAnswerOptions;
    }
}
