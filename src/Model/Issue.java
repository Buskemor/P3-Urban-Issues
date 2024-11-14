package Model;

import javafx.util.Pair;

import java.util.Date;


public class Issue {

    private int issueId;
    private Date date;
    private String road;
    private int houseNumber;
    private String description;
    private Pair<Integer, String> category;
    private Status status;
    private Citizen citizen;

    private String categoryDisplayString;


    public Issue(int issueId, Date date, String road, int houseNumber, String description, Pair<Integer, String> category, Status status, Citizen citizen){
        this.issueId = issueId;
        this.date = date;
        this.road=road;
        this.houseNumber = houseNumber;
        this.description=description;
        this.category=category;
        this.status = status;
        this.citizen =citizen;

        categoryDisplayString = category.getValue();
    }

    public Issue(int issueId, Date date, String road, int houseNumber, String description, Pair<Integer, String> category, Status status){
        this.issueId = issueId;
        this.date = date;
        this.road = road;
        this.houseNumber = houseNumber;
        this.description = description;
        this.category = category;
        this.status = status;

        categoryDisplayString = category.getValue();
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

    public Pair<Integer, String> getCategory() {
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

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCategoryDisplayString() {
        return this.categoryDisplayString;
    }
}
