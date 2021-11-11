package no.kristiania.http.model;

public class TextQuestion extends QuestionOptions{
    private Integer maxChars;
    private String placeholder;

    public Integer getMaxChars() {
        return maxChars;
    }

    public void setMaxChars(Integer maxChars) {
        this.maxChars = maxChars;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }
}
