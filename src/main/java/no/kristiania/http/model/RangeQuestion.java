package no.kristiania.http.model;

public class RangeQuestion extends QuestionOptions {
    private Integer min;
    private Integer max;
    private String minLabel;
    private String maxLabel;

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public String getMinLabel() {
        return minLabel;
    }

    public void setMinLabel(String minLabel) {
        this.minLabel = minLabel;
    }

    public String getMaxLabel() {
        return maxLabel;
    }

    public void setMaxLabel(String maxLabel) {
        this.maxLabel = maxLabel;
    }

    @Override
    public String getHtml(){

        String minLabel = "<label>"+ getMinLabel() + "</label>";
        String input = "<input name=\"answer\" type=\"range\" min=\"" + getMin() + "\" max=\"" + getMax() + "\">";
        String maxLabel = "<label>"+ getMaxLabel() + "</label>";

        return minLabel + input + maxLabel;
    }
}
