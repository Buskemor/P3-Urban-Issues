package Iter1;

public class Citizen {

    private String email;
    private boolean wantsFeedback;

    public Citizen(String email, boolean wantsFeedback){
        this.email= email;
        this.wantsFeedback=wantsFeedback;
    }

    public Issue submitIssue(String road, int houseNumber,String description, Category category){
        return new Issue(road, houseNumber, description, category,this);
    }

    // Getter to check if the citizen wants feedback
    public boolean wantsFeedback() {
        return this.wantsFeedback;
    }

    public String getEmail() {
        return this.email;
    }
}
