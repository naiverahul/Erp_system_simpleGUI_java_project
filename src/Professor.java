import javax.swing.*;
import java.util.*;

public class Professor extends User implements common {
    private ArrayList<Course> courses;
    private String office_hours;

    public Professor(String name, String email, String password, String office_hours) {
        super(name, email, password);
        this.courses = new ArrayList<>();
        this.office_hours = office_hours;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public ArrayList<String>coursenamelist(){
        ArrayList<String> list = new ArrayList<>();
        for(Course c : courses){
            list.add(c.getCode());
        }
        return list;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public String getOffice_hours() {
        return office_hours;
    }

    public void setOffice_hours(String office_hours) {
        this.office_hours = office_hours;
    }

    @Override
    public void view_syllabus() {
        StringBuilder syllabus = new StringBuilder("Printing the syllabus for all your courses:\n");
        for (Course course : courses) {
            syllabus.append(course.getSyllabus()).append("\n");
        }
        JOptionPane.showMessageDialog(null, syllabus.toString());
    }

    @Override
    public ArrayList<Course> get_preeq(Course course) {
        return null;
    }

    @Override
    public void view_timing() {
        StringBuilder timingsDisplay = new StringBuilder("Printing the timing for all your courses:\n");
        for (Course course : courses) {
            HashMap<String, String> timings = course.getTiming();
            for (Map.Entry<String, String> entry : timings.entrySet()) {
                timingsDisplay.append("Day: ").append(entry.getKey())
                        .append(", Timing + Location: ").append(entry.getValue()).append("\n");
            }
        }
        JOptionPane.showMessageDialog(null, timingsDisplay.toString());
    }

    public void change_syllabus() {
        String code = JOptionPane.showInputDialog("Enter code of course:");
        if(code == null) return;
        for(Course c : courses){
            if(c.getCode().equals(code)){
                String syllabus = JOptionPane.showInputDialog("Enter syllabus for this course:");
                c.setSyllabus(syllabus);
                JOptionPane.showMessageDialog(null,"Syllabus changed to " + c.getSyllabus());
                return;
            }
        }

        JOptionPane.showMessageDialog(null,"Course does not exist");
    }

    public void view_courses_taught() {
        StringBuilder coursesTaught = new StringBuilder("Courses you are teaching:\n");
        if (!courses.isEmpty()) {
            for (Course c : courses) {
                coursesTaught.append(c.getTitle()).append(" : ")
                        .append(c.getCode()).append(" : ")
                        .append(c.getCredits()).append("\n");
            }
        } else {
            coursesTaught.append("You are not teaching any courses.");
        }
        JOptionPane.showMessageDialog(null, coursesTaught.toString());
    }

    public void add_prereg(Administrator admin) {
        view_courses_taught();
        String code = JOptionPane.showInputDialog("Enter code for the course:");
        boolean flag = false;
        for (Course course : courses) {
            if (code.equalsIgnoreCase(course.getCode())) {
                String prereqCountStr = JOptionPane.showInputDialog("Enter number of prerequisites to add:");
                int prereqCount = Integer.parseInt(prereqCountStr);

                for (int i = 0; i < prereqCount; i++) {
                    String prerequisite = JOptionPane.showInputDialog("Enter prerequisite course code:");
                    course.getPrereq().add(admin.getCourseCatalog().get_course_by_code(prerequisite));
                }
                flag = true;
                break;
            }
        }
        if (!flag) {
            JOptionPane.showMessageDialog(null, "Course not found.");
        }
    }

    public void change_credit() {
        String code = JOptionPane.showInputDialog("Enter code for the course:");
        boolean flag = false;
        for (Course course : courses) {
            if (code.equalsIgnoreCase(course.getCode())) {
                String creditStr = JOptionPane.showInputDialog("Enter new credit value:");
                int credit = Integer.parseInt(creditStr);
                course.setCredits(credit);
                flag = true;
                break;
            }
        }
        if (!flag) {
            JOptionPane.showMessageDialog(null, "Course not found.");
        }
    }

    public void change_timetable(Administrator admin) {
        String code = JOptionPane.showInputDialog("Enter code for the course:");
        Course course = admin.getCourseCatalog().get_course_by_code(code);

        boolean continueEditing = true;
        while (continueEditing) {
            if (course.getTiming().isEmpty()) {
                String numDaysStr = JOptionPane.showInputDialog("No timings assigned. Enter number of days to assign:");
                int numDays = Integer.parseInt(numDaysStr);

                for (int i = 0; i < numDays; i++) {
                    String day = JOptionPane.showInputDialog("Enter day of the week:");
                    String timing = JOptionPane.showInputDialog("Enter Timings (Start-End | Location):");
                    course.getTiming().put(day.toLowerCase(), timing);
                }
            } else {
                String[] options = {"Add Timing", "Change Timing", "Exit"};
                int choice = JOptionPane.showOptionDialog(null, "Choose an action", "Timetable Management",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                switch (choice) {
                    case 0:
                        String numDaysToAddStr = JOptionPane.showInputDialog("Enter number of days to assign:");
                        int numDaysToAdd = Integer.parseInt(numDaysToAddStr);

                        for (int i = 0; i < numDaysToAdd; i++) {
                            String day = JOptionPane.showInputDialog("Enter day of the week:");
                            String timing = JOptionPane.showInputDialog("Enter Timings (Start-End | Location):");
                            course.getTiming().put(day.toLowerCase(), timing);
                        }
                        break;

                    case 1:
                        String dayToChange = JOptionPane.showInputDialog("Enter the day to change timing:").toLowerCase();
                        if (course.getTiming().containsKey(dayToChange)) {
                            String newTiming = JOptionPane.showInputDialog("Enter new timings (Start-End | Location):");
                            course.getTiming().put(dayToChange, newTiming);
                        } else {
                            JOptionPane.showMessageDialog(null, "No timing found for the given day.");
                        }
                        break;

                    case 2:
                        continueEditing = false;
                        break;

                    default:
                        JOptionPane.showMessageDialog(null, "Invalid option. Please try again.");
                        break;
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Timetable editing finished.");
    }

    public void view_student(Administrator admin) {
        String code = JOptionPane.showInputDialog("Enter code for the course:").trim();
        boolean studentFound = false;
        boolean courseExists = false;

        for (Course course : this.courses) {
            if (code.equalsIgnoreCase(course.getCode())) {
                courseExists = true;
                break;
            }
        }

        if (!courseExists) {
            JOptionPane.showMessageDialog(null, "Course code does not exist.");
            return;
        }

        List<Student> students = admin.getStudentList().getStudentList();
        StringBuilder studentInfo = new StringBuilder("Students enrolled in the course:\n");

        for (Student student : students) {
            for (StudentCourse studentCourse : student.getCourses()) {
                if (code.equalsIgnoreCase(studentCourse.getCourse().getCode())) {
                    studentInfo.append("Student Name: ").append(student.getName()).append("\n")
                            .append("Student Email: ").append(student.getEmail()).append("\n")
                            .append("Grade: ").append(studentCourse.getGrade()).append("\n")
                            .append("-----------------------------\n");
                    studentFound = true;
                    break;
                }
            }
            for (StudentCourse studentCourse : student.getCompleted_courses()) {
                if (code.equalsIgnoreCase(studentCourse.getCourse().getCode())) {
                    studentInfo.append("Student Name: ").append(student.getName()).append("\n")
                            .append("Student Email: ").append(student.getEmail()).append("\n")
                            .append("Grade: ").append(studentCourse.getGrade()).append("\n")
                            .append("-----------------------------\n");
                    studentFound = true;
                    break;
                }
            }
        }

        if (!studentFound) {
            JOptionPane.showMessageDialog(null, "No students found for the given course.");
        } else {
            JOptionPane.showMessageDialog(null, studentInfo.toString());
        }
    }
    @Override
    public void changepass() {
        String username = JOptionPane.showInputDialog("Enter the username:");
        String currentPassword = JOptionPane.showInputDialog("Enter your current password:");
        if (currentPassword != null && currentPassword.equals(this.getPassword())) {
            String newPassword = JOptionPane.showInputDialog("Enter your new password:");
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                setPassword(newPassword);
                JOptionPane.showMessageDialog(null, "Password successfully changed.");
            } else {
                JOptionPane.showMessageDialog(null, "New password cannot be empty.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Incorrect current password.");
        }
    }
}
