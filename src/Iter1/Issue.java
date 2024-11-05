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

    }

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
