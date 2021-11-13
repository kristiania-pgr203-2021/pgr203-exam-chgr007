package no.kristiania.http.controller;

import no.kristiania.http.dao.QuestionnaireDao;
import no.kristiania.http.model.Questionnaire;
import no.kristiania.http.util.HttpRequest;
import no.kristiania.http.util.HttpResponse;

import java.io.IOException;
import java.sql.SQLException;

public class NewQuestionnaireController implements HttpController {
    private QuestionnaireDao questionnaireDao;
    private String path = "/api/newQuestionnaire";

    public NewQuestionnaireController(QuestionnaireDao questionnaireDao) {
        this.questionnaireDao = questionnaireDao;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException, IOException {

         if (request.getRequestType().equalsIgnoreCase("post")) {
            Questionnaire questionnaire = new Questionnaire();
            questionnaire.setName(request.getPostParams().get("questionnaireName"));
            questionnaire.setPersonId(Long.parseLong(request.getPostParams().get("userId")));

            questionnaireDao.save(questionnaire);

            HttpResponse response = new HttpResponse(303, "See other");
            response.setHeaderField("Connection", "close");
            response.setHeaderField("Location", "/index.html");
            return response;

        }
        return new HttpResponse(500, "Internal server error");
    }

    @Override
    public String getPath() {
        return this.path;
    }
}
