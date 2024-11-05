package Iter1;

import database.DbInserter;


public class Issue {

    private String road;
    private int houseNumber;
    private String description;
    private Category category;
    private Status status;
    private Citizen reportedBy;

    public Issue(String road, int houseNumber, String description, Category category, Citizen reportedBy){
        this.road=road;
        this.houseNumber = houseNumber;
        this.description=description;
        this.category=category;
        this.status=Status.PENDING;
        this.reportedBy=reportedBy;

        new DbInserter(this).addIssueToDatabase();
    }

    public Issue(String road, int houseNumber, String description, Category category){
        this.road=road;
        this.houseNumber = houseNumber;
        this.description=description;
        this.category=category;
        this.status=Status.PENDING;

        new DbInserter(this).addIssueToDatabase();
    }

    public static void main(String[] args) {
        Citizen citizen = new Citizen("JohnDoe@asdasd.dk", true);
        Issue issue = new Issue("Park",10, "Broken pipe", Category.WATER, citizen);

        issue.sendFeedbackToCitizen(issue.status, issue.reportedBy);

        issue.status = issue.changeStatus(issue.status);  // Change from PENDING to IN_PROGRESS
        issue.sendFeedbackToCitizen(issue.status, issue.reportedBy);  // Prints

        issue.status = issue.changeStatus(issue.status);  // Change from IN_PROGRESS to RESOLVED
        issue.sendFeedbackToCitizen(issue.status, issue.reportedBy);  // Prints
    }

    public void sendFeedbackToCitizen(Status status, Citizen wantsFeedback){
        if (!reportedBy.wantsFeedback())        //Ja, vi føler det næste step vil være at lave automatisk email, hvilket er lidt meget
            return ;
        if(status==Status.PENDING) {
            System.out.println("We have received your submission");
        }
        if (status==Status.IN_PROGRESS) {
            System.out.println("We have started to work on your submission");
        }
        if (status==Status.RESOLVED) {
            System.out.println("Your submission has been resolved");
        }
    }

    public Status changeStatus(Status status){
        if (status==Status.PENDING) {
            status = status.IN_PROGRESS;
        }
        else if (status==Status.IN_PROGRESS) {
            status = Status.RESOLVED;
        }
        return status;
    }

    /*private void iterateStatus() {
        if(this.status.ordinal()+1 > Status.values().length)
            throw new RuntimeException("Attempted to iterate enum out of bounds");
        this.status = Status.values()[this.status.ordinal()+1];
    }*/

    public void archiveIssue(Issue issue){

    }

    public void addIssueToDatabase(Issue issue){

    }

    public void removeIssueFromDatabase(Issue issue){

    }


    public String getRoad() {
        return this.road;
    }

    public int getHouseNumber() {
        return this.houseNumber;
    }

    public String getDescription() {
        return this.description;
    }

    public Category getCategory() {
        return this.category;
    }

    public Citizen getReportedBy() {
        return this.reportedBy;
    }
}
