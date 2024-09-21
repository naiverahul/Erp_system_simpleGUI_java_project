import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class Course {
    private String title;
    private String code;
    private Professor proff;
    private int credits;
    private ArrayList<Course> prereq;
    private HashMap<String, String> timing;
    private String Semester;
    private String Syllabus;
    private int enrollment_limit;
    private String location;
    private int no_of_student;



    public Course(String title, String code, int credits, ArrayList<Course> prereq, String Semester,int enrollment_limit,String location) {
        this.title = title;
        this.code = code;
        this.proff = null;
        this.credits = credits;
        this.prereq = prereq;
        this.timing = new HashMap<>();
        this.Semester = Semester;
        this.enrollment_limit = enrollment_limit;
        this.location = location;
        this.no_of_student = 0;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }
    public void add_student(){this.no_of_student++;}
    public int getno_of_student() {return no_of_student;}

    public void setCode(String code) {
        this.code = code;
    }

    public Professor getProff() {
        return proff;
    }

    public void setProff(Professor proff) {
        this.proff = proff;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public ArrayList<Course> getPrereq() {
        return prereq;
    }

    public ArrayList<String> prereqcoursename() {
        ArrayList<String> prereqname = new ArrayList<>();
        for (Course c : prereq) {
            prereqname.add(c.getCode());
        }
        return prereqname;
    }

    public void setPrereq(ArrayList<Course> prereq) {
        this.prereq = prereq;
    }

    public HashMap<String, String> getTiming() {
        return timing;
    }

    public void setTiming(HashMap<String, String> timing) {
        this.timing = timing;
    }

    public String getSemester() {
        return Semester;
    }

    public void setSemester(String semester) {
        Semester = semester;
    }

    public String getSyllabus() {
        return Syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.Syllabus = syllabus;
    }

    public void showTimetable() {
        if (this.timing.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No timing information available.");
            return;
        }

        StringBuilder timetable = new StringBuilder("Timetable:\n");
        for (Map.Entry<String, String> entry : this.timing.entrySet()) {
            timetable.append(entry.getKey()).append(" --> ").append(entry.getValue()).append("\n");
        }

        JOptionPane.showMessageDialog(null, timetable.toString());
    }
    public int getEnrollment_limit() {
        return enrollment_limit;
    }
    public void setEnrollment_limit(int enrollment_limit) {
        this.enrollment_limit = enrollment_limit;
    }

    public String getlocation(){
        return location;
    }
    public void setlocation(String location){
        this.location = location;
    }

}
