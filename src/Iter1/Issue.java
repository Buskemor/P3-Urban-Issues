package Iter1;

import database.DbInserter;

import java.util.Date;


public class Issue {

    private int issueId;
    private Date date;
    private String road;
    private int houseNumber;
    private String description;
    private Category category;
    private Status status;
    private Citizen citizen;


    public Issue(int issueId, Date date, String road, int houseNumber, String description, Category category, Status status, Citizen citizen){
        this.issueId = issueId;
        this.date = date;
        this.road=road;
        this.houseNumber = houseNumber;
        this.description=description;
        this.category=category;
        this.status = status;
        this.citizen =citizen;
    }

    public Issue(int issueId, Date date, String road, int houseNumber, String description, Category category, Status status){
        this.issueId = issueId;
        this.date = date;
        this.road = road;
        this.houseNumber = houseNumber;
        this.description = description;
        this.category = category;
        this.status = status;
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

    public Status getStatus() {
        return this.status;
    }

    public Citizen getReportedBy() {
        return this.citizen;
    }

    public Date getDate() {
        return this.date;
    }

    public int getIssueId() {
        return this.issueId;
    }




    public void setRoad(String road) {
        this.road = road;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setReportedBy(Citizen citizen) {
        this.citizen = citizen;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
