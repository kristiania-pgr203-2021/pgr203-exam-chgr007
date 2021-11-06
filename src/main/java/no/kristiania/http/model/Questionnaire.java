package no.kristiania.http.model;

public class Questionnaire {

    private long id;
    private long personId;
    private String name;

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

}
