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

    public static void main(String[] args) {
        Citizen citizen = new Citizen("JohnDoe@asdasd.dk", true);
        Issue issue = new Issue("Park", "Broken pipe", Category.WATER, citizen);

        issue.sendFeedbackToCitizen(issue.status, issue.reportedBy);

        issue.status = issue.changeStatus(issue.status);  // Change from PENDING to IN_PROGRESS
        issue.sendFeedbackToCitizen(issue.status, issue.reportedBy);  // Prints

        issue.status = issue.changeStatus(issue.status);  // Change from IN_PROGRESS to RESOLVED
        issue.sendFeedbackToCitizen(issue.status, issue.reportedBy);  // Prints
    }

    public void sendFeedbackToCitizen(Status status, Citizen wantsFeedback){
        if (reportedBy.wantsFeedback())  //Ja, vi føler det næste step vil være at lave automatisk email, hvilket er lidt meget
            if(status==Status.PENDING) {
                System.out.println("We have received your submission");
            }
            else if (status==Status.IN_PROGRESS) {
                System.out.println("We have started to work on your submission");
            }
            else if (status==Status.RESOLVED) {
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

    public void archiveIssue(Issue issue){

    }

    public void addIssueToDatabase(Issue issue){

    }

    public void removeIssueFromDatabase(Issue issue){

    }

}

