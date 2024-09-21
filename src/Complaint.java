public class Complaint {
    private String complaint;
    private boolean pending;
    private boolean resolved;
    private Student student;
    private int complaint_id;
    private String remarks;

    public Complaint(String complaint ,Student student,int complaint_id) {
        this.complaint = complaint;
        this.pending = true;
        this.resolved = false;
        this.student = student;
        this.complaint_id = complaint_id;
        this.remarks = "";
    }

    public String getComplaint() {
        return complaint;
    }
    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }
    public boolean isPending() {
        return pending;
    }
    public void setPending(boolean pending) {
        this.pending = pending;
    }
    public boolean isResolved() {
        return resolved;
    }
    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }
    public Student getStudent() {
        return student;
    }
    public void setStudent(Student student) {
        this.student = student;
    }
    public int getComplaint_id() {
        return complaint_id;
    }
    public void setComplaint_id(int complaint_id) {
        this.complaint_id = complaint_id;
    }
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}


