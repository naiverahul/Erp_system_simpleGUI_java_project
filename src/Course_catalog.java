import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Course_catalog extends infinite_input {
    private ArrayList<Course> courses;

    public Course_catalog() {
        this.courses = new ArrayList<>();
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public Course get_course_by_code(String code) {
        for (Course c : this.courses) {
            if (c.getCode().equalsIgnoreCase(code)) {
                return c;
            }
        }
        return null;
    }

    public void add_course(Administrator admin) {
        String title = JOptionPane.showInputDialog("Enter new course title to add:");
        if (title == null) return;

        String code = JOptionPane.showInputDialog("Enter new course code:");
        if (code == null) return;

        for (Course c : this.courses) {
            if (c.getCode().equalsIgnoreCase(code)) {
                JOptionPane.showMessageDialog(null, "Course already exists.");
                return;
            }
        }

        String creditInput = JOptionPane.showInputDialog("Enter new course credits:");
        if (creditInput == null  ) {
            JOptionPane.showMessageDialog(null, "Enter corerct either 2 or 4.");

            return;
        }
        int credits = Integer.parseInt(creditInput);
        if(credits != 2 && credits != 4 ){
            JOptionPane.showMessageDialog(null, "Enter corerct either 2 or 4.");

            return;
        }

        String prereqInput = JOptionPane.showInputDialog("Enter number of prerequisites:");
        if (prereqInput == null) return;
        int numPrereq = Integer.parseInt(prereqInput);

        ArrayList<Course> prereq = new ArrayList<>();
        if (numPrereq != 0) {
            for (int i = 0; i < numPrereq; i++) {
                boolean courseFound = false;
                while (!courseFound) {
                    String prereqCode = JOptionPane.showInputDialog("Enter the course code of prerequisite " + (i + 1) + ":");
                    if (prereqCode == null) return;

                    Course c = admin.getCourseCatalog().get_course_by_code(prereqCode);
                    if (c == null) {
                        JOptionPane.showMessageDialog(null, "Course " + prereqCode + " not found. Try again.");
                    } else {
                        prereq.add(c);
                        courseFound = true;
                        JOptionPane.showMessageDialog(null, "Course " + prereqCode + " added.");
                    }
                }
            }
        }

        String semester = JOptionPane.showInputDialog("Enter semester in which this course is offered:");
        if (semester == null) return;

        String enrollment_limit = JOptionPane.showInputDialog("Enter enrollment_limit of this course is offered:");
        if (enrollment_limit == null) return;
        int en_limit ;
        try {
            en_limit = Integer.parseInt(enrollment_limit);
        }catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Enrollment_limit must be an integer.");
            return;
        }
        String location = JOptionPane.showInputDialog("Allocation classroom to this course:");
        if (location == null) return;
        Course new_course = new Course(title, code, credits, prereq, semester,en_limit,location);
        JOptionPane.showMessageDialog(null, "New course added: " + title + ". Please assign a professor in manage_prof section.");
        this.courses.add(new_course);
    }

    public void view_all_courses() {
        if (this.courses.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No courses available.");
        } else {
            StringBuilder coursesList = new StringBuilder("Available courses:\n");
            for (Course c : this.courses) {
                String professorName = (c.getProff() == null) ? "Professor not assigned. Please assign." : c.getProff().getName();
                coursesList.append(c.getTitle())
                        .append(" | ")
                        .append(c.getCode())
                        .append(" | Prof: ")
                        .append(professorName)
                        .append(" | Credits: ")
                        .append(c.getCredits())
                        .append(" | Prerequisites: ")
                        .append(c.prereqcoursename())
                        .append(" | Semester: ")
                        .append(c.getSemester())
                        .append("Enrollment Limit: ")
                        .append(c.getEnrollment_limit())
                        .append("Classroom")
                        .append(c.getlocation())
                        .append("\n");
            }
            JOptionPane.showMessageDialog(null, coursesList.toString());
        }
    }

    public void delete_course(Administrator admin) {
        String code = JOptionPane.showInputDialog("Enter course code to delete:");
        if (code == null) return;

        Course c = this.get_course_by_code(code);
        if (c == null) {
            JOptionPane.showMessageDialog(null, "Course " + code + " not found.");
        } else {
            this.courses.remove(c);
            JOptionPane.showMessageDialog(null, "Course " + code + " removed.");
        }
    }
}
