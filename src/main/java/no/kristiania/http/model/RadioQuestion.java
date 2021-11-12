package no.kristiania.http.model;

public class RadioQuestion extends QuestionOptions{
    private String choice;

    public String getChoice() {
        return choice;
    }
    public String generateChoiceAsId(){
        String[] choiceArr = choice.split(" ");

        if(choiceArr.length > 1){
            StringBuilder stringBuilder = new StringBuilder();
            for(String s : choiceArr){
                stringBuilder.append(s);
                stringBuilder.append("-");
            }
            stringBuilder.setLength(stringBuilder.length()-1);

            return stringBuilder.toString();
        }

        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    @Override
    public String generateHtml(){

        return "<div class=\"radio-answers\">" +
                "<input type=\"radio\" id=\"" + generateChoiceAsId() + "\" name=\"answer\" value=\"" + getChoice() + "\">" +
                "<label for=\"" + generateChoiceAsId() + "\">" + getChoice() + "</label>" +
                "</div>" +
                "<br>";

    }
}
