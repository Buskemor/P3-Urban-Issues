package Iter1;

public class Citizen {

    private String email;
    private boolean wantsFeedback;

    public Citizen(String email, boolean wantsFeedback){
        this.email= email;
        this.wantsFeedback=wantsFeedback;

    }

    public Issue submitIssue(String location,String description, Category category){

        return new Issue(location, description, category,this);
    }

    // Getter to check if the citizen wants feedback
    public boolean wantsFeedback() {
        return this.wantsFeedback;
    }
}
