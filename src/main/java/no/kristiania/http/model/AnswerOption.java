package no.kristiania.http.model;

public class AnswerOption {
    private long id, answerId;
    private int range;
    private String answerOption, minRangeName, maxRangeName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(long answerId) {
        this.answerId = answerId;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public String getAnswerOption() {
        return answerOption;
    }

    public void setAnswerOption(String answerOption) {
        this.answerOption = answerOption;
    }

    public String getMinRangeName() {
        return minRangeName;
    }

    public void setMinRangeName(String minRangeName) {
        this.minRangeName = minRangeName;
    }

    public String getMaxRangeName() {
        return maxRangeName;
    }

    public void setMaxRangeName(String maxRangeName) {
        this.maxRangeName = maxRangeName;
    }
}
