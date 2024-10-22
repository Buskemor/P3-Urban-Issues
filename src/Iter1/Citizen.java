package Iter1;

public class Citizen {
    private String email;
    private boolean wantsFeedback;

    public Citizen(String email, boolean wantsFeedback){
        this.email="";
        this.wantsFeedback=false;

    }

    public Issue sumbmitIssue(String location,String description, Category category){

        return new Issue(location, description, category,this);
    }

}
