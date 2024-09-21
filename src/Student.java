import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

class Student extends User implements common {
    private ArrayList<StudentCourse> courses;
    private Course_catalog course_catalog;
    private ArrayList<StudentCourse> completed_courses;
    private HashMap<Integer, Float> SGPA;
    private int semester;
    private int Credit;

    public Student(String name, String email, String password, Course_catalog course_catalog, int semester) {
        super(name, email, password);
        this.courses = new ArrayList<>();
        this.course_catalog = course_catalog;
        this.completed_courses = new ArrayList<>();
        this.SGPA = new HashMap<>();
        this.Credit = 20;
        this.semester = semester;
    }

    public ArrayList<StudentCourse> getCourses() { return this.courses; }
    public void setCourses(ArrayList<StudentCourse> courses) { this.courses = courses; }
    public Course_catalog getCourse_catalog() { return course_catalog; }
    public void setCourse_catalog(Course_catalog course_catalog) { this.course_catalog = course_catalog; }
    public ArrayList<StudentCourse> getCompleted_courses() { return completed_courses; }
    public void setCompleted_courses(ArrayList<StudentCourse> completed_courses) { this.completed_courses = completed_courses; }
    public HashMap<Integer, Float> getSGPA() { return SGPA; }
    public void setSGPA(HashMap<Integer, Float> SGPA) { this.SGPA = SGPA; }
    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    @Override
    public ArrayList<Course> get_preeq(Course course) {
        return course.getPrereq();
    }

    @Override
    public void view_timing() {
        String course_code = JOptionPane.showInputDialog("Enter the course code or type 'exit' to go back:");
        if (course_code == null || course_code.equalsIgnoreCase("exit")) {
            return;
        }

        try {
            for (StudentCourse c : courses) {
                if (course_code.equalsIgnoreCase(c.getCourse().getCode())) {
                    HashMap<String, String> timings = c.getCourse().getTiming();
                    StringBuilder message = new StringBuilder();
                    for (Map.Entry<String, String> entry : timings.entrySet()) {
                        message.append("Day ").append(entry.getKey())
                                .append(" - Timing + Location: ").append(entry.getValue()).append("\n");
                    }
                    JOptionPane.showMessageDialog(null, message.toString());
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "Course not found.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occurred while viewing timings: " + e.getMessage());
        }
    }
    @Override
    public void changepass() {
        String username = JOptionPane.showInputDialog("Enter the username:");
        if(username == null) return;
        String currentPassword = JOptionPane.showInputDialog("Enter your current password:");
        if(currentPassword == null) return;
        if (currentPassword != null && currentPassword.equals(this.getPassword())) {
            String newPassword = JOptionPane.showInputDialog("Enter your new password:");
            if (newPassword == null) return;
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                this.setPassword(newPassword);
                JOptionPane.showMessageDialog(null, "Password successfully changed.");
            } else {
                JOptionPane.showMessageDialog(null, "New password cannot be empty.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Incorrect current password.");
        }
    }
    public void view_available_courses(Administrator admin) {
        try {
            StringBuilder message = new StringBuilder("Courses available for this semester are:\n");
            int sem_no = this.semester;
            String sem = "Monsoon";
            if(sem_no%2 == 0){
                sem = "Winter";
            }
            boolean courseFound = false;

            for (Course course : course_catalog.getCourses()) {
                if (sem.equalsIgnoreCase(course.getSemester())) {
                    message.append("Course Title: ").append(course.getTitle())
                            .append(" Course Code: ").append(course.getCode()).append("\n");
                    courseFound = true;
                }
            }
            if (!courseFound) {
                message.append("No course found for this semester.");
            }
            JOptionPane.showMessageDialog(null, message.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occurred while viewing available courses: " + e.getMessage());
        }
    }

    public void enroll_in_course(String courseCode) {
        try {
            Course course1 = course_catalog.get_course_by_code(courseCode);
//            Course newcourse = course.deepclone();
            StudentCourse course = new StudentCourse(course1);
            if (course != null) {
                if (course.getCourse().getCredits() <= this.Credit) {
                    boolean satisfied = true;
                    for (Course c : course.getCourse().getPrereq()) {
                        if (!completed_courses.contains(c)) {
                            JOptionPane.showMessageDialog(null, "Prerequisites not satisfied.");
                            satisfied = false;
                            break;
                        }
                        for(StudentCourse c1 : courses){
                            if(courseCode.equalsIgnoreCase(c1.getCourse().getCode())){
                                JOptionPane.showMessageDialog(null, "Already Enrolled in the course");
                                satisfied = false;
                                break;
                            }
                        }
                        if(course.getCourse().getno_of_student() > course.getCourse().getEnrollment_limit() ){
                            satisfied = false;
                        }
                    }
                    if (satisfied) {
                        courses.add(course);
                        JOptionPane.showMessageDialog(null, "Course added to the list.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Credits limit exceeded.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Course not found.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occurred while enrolling in the course: " + e.getMessage());
        }
    }

    public void view_enrolled_course() {
        try {
            if (courses.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No courses enrolled.");
            } else {
                StringBuilder message = new StringBuilder("Enrolled courses:\n");
                for (StudentCourse c : this.courses) {
                    message.append("Course ").append(c.getCourse().getTitle()).append(" has been enrolled.\n");
                }
                JOptionPane.showMessageDialog(null, message.toString());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occurred while viewing enrolled courses: " + e.getMessage());
        }
    }

    public void view_completed_course() {
        try {
            if (completed_courses.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No completed courses.");
            } else {
                StringBuilder message = new StringBuilder("Completed courses:\n");
                for (StudentCourse c : completed_courses) {
                    message.append("Course ").append(c.getCourse().getTitle()).append(" has been completed.\n");
                }
                JOptionPane.showMessageDialog(null, message.toString());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occurred while viewing completed courses: " + e.getMessage());
        }
    }
    public void view_grade(){
        String coursecode = JOptionPane.showInputDialog("Enter the course code  to view grade:");
        boolean courseFound = false;
        for(StudentCourse c: this.completed_courses){
            if(coursecode.equalsIgnoreCase(c.getCourse().getCode())){
                JOptionPane.showMessageDialog(null, "Grade :" + c.getGrade());
                courseFound = true;
            }
        }
        if(!courseFound){
            JOptionPane.showMessageDialog(null, "Course not found.");
        }

    }

    public void view_SGPA() {
        try {
            String semInput = JOptionPane.showInputDialog("Enter the semester to view SGPA:");
            if (semInput != null && !semInput.trim().isEmpty()) {
                Integer sem = Integer.parseInt(semInput);
                if (SGPA.containsKey(sem)) {
                    JOptionPane.showMessageDialog(null, "SGPA for semester " + sem + " is " + SGPA.get(sem));
                } else {
                    JOptionPane.showMessageDialog(null, "SGPA not available for the given semester.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occurred while viewing SGPA: " + e.getMessage());
        }
    }

    public void find_view_CGPA() {
        try {
            if (SGPA.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No SGPA records found.");
                return;
            }
            int numSemesters = SGPA.size();
            float totalSGPA = 0;
            for (Float sgpa : SGPA.values()) {
                totalSGPA += sgpa;
            }
            float cgpa = totalSGPA / numSemesters;
            JOptionPane.showMessageDialog(null, "Your current CGPA is " + cgpa);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occurred while calculating CGPA: " + e.getMessage());
        }
    }

    public void drop_course() {
        String courseCode = JOptionPane.showInputDialog("Enter course code to drop:");
        try {
            boolean courseRemoved = false;
            for (StudentCourse c : this.courses) {
                if (c.getCourse().getCode().equalsIgnoreCase(courseCode)) {
                    c.setGrade("W");
                    courses.remove(c);
                    JOptionPane.showMessageDialog(null, "Removed " + c.getCourse().getTitle() + ". Grade set to W.");
                    courseRemoved = true;
                    break;
                }
            }
            if (!courseRemoved) {
                JOptionPane.showMessageDialog(null, "Course not found in your enrolled list.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occurred while dropping the course: " + e.getMessage());
        }
    }

    public void add_complaint(Administrator admin) {
        String complaint = JOptionPane.showInputDialog("Enter your complaint:");
        try {
            if (complaint != null && !complaint.trim().isEmpty()) {
                int com_id = admin.getcomplaints().size() + 1;
                Student s = admin.getStudentList().find_student_by_email(this.getEmail());
                Complaint com = new Complaint(complaint, s, com_id);
                admin.getcomplaints().add(com);
                JOptionPane.showMessageDialog(null, "Complaint registered with complaint_id " + com_id);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occurred while adding the complaint: " + e.getMessage());
        }
    }

    public void view_complaints(Administrator admin) {
        try {
            StringBuilder message = new StringBuilder();
            boolean complaintFound = false;
            for (Complaint c : admin.getcomplaints()) {
                if (c.getStudent().getEmail().equalsIgnoreCase(this.getEmail())) {
                    message.append("Complaint_ID: ").append(c.getComplaint_id()).append(" - ")
                            .append(c.getComplaint()).append("\n");
                    complaintFound = true;
                }
            }
            if (!complaintFound) {
                message.append("No complaints found.");
            }
            JOptionPane.showMessageDialog(null, message.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occurred while viewing complaints: " + e.getMessage());
        }
    }

    public void complaint_status(Administrator admin) {
        String com_id_str = JOptionPane.showInputDialog("Enter complaint_ID of your complaint:");
        try {
            if (com_id_str != null && !com_id_str.trim().isEmpty()) {
                int com_id = Integer.parseInt(com_id_str);
                boolean complaintFound = false;
                for (Complaint c : admin.getcomplaints()) {
                    if (c.getComplaint_id() == com_id) {
                        String status = c.isPending() ? "Pending" : c.isResolved() ? "Resolved\nRemark: " + c.getRemarks() : "";
                        JOptionPane.showMessageDialog(null, "Your complaint: " + c.getComplaint() + "\nStatus: " + status);
                        complaintFound = true;
                        break;
                    }
                }
                if (!complaintFound) {
                    JOptionPane.showMessageDialog(null, "Complaint ID not found.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occurred while checking complaint status: " + e.getMessage());
        }
    }

    @Override
    public void view_syllabus() {
        String courseCode = JOptionPane.showInputDialog("Enter course code to view syllabus:");
        try {
            Course c = course_catalog.get_course_by_code(courseCode);
            if (c != null) {
                JOptionPane.showMessageDialog(null, "Syllabus for: " + c.getTitle() + " is " + c.getSyllabus());
            } else {
                JOptionPane.showMessageDialog(null, "Course not found.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occurred while viewing the syllabus: " + e.getMessage());
        }
    }
    public void show_timetable(){
        String courseCode = JOptionPane.showInputDialog("Enter course code to view timetable:");

    }
}
