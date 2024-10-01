package feedback;

import java.util.ArrayList;

public class Feedback<T> {
    private ArrayList<T> feedbackList;

    public Feedback() {
        this.feedbackList = new ArrayList<>();
    }

    // Add feedback to the list
    public void addFeedback(T feedback) {
        feedbackList.add(feedback);
    }

    // Get all feedbacks
    public ArrayList<T> getFeedbackList() {
        return feedbackList;
    }
}
