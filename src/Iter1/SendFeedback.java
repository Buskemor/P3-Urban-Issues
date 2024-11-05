package Iter1;

public class SendFeedback {

    public static void sendFeedbackToCitizen(Status status, Citizen citizen){

        if (!citizen.wantsFeedback())
            return;

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
}
