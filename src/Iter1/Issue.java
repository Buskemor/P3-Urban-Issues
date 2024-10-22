package Iter1;
import java.util.Date;


public class Issue {

    private String location;
    private String description;
    private Category category;
    private Status status;
    private Citizen reportedBy;
    private Date date;


    public Issue(String location, String description, Category category, Citizen reportedBy){
        this.location=location;
        this.description=description;
        this.category=category;
        this.status=Status.PENDING;
        this.reportedBy=reportedBy;
        this.date=new Date();

    }

    public void sendFeedbackToCitizen(Status status){

    }

    public void changeStatus(Status status){

    }

    public void archiveIssue(Issue issue){

    }

    public void addIssueToDatabase(Issue issue){

    }

    public void removeIssueFromDatabase(Issue issue){

    }

}

