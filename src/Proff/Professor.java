package Proff;

import admin.Administrator;
import course.Course;
import helper.User;
import helper.common;
import student.Student;
import student.StudentCourse;
import student.TA;
import feedback.*;
import javax.swing.*;
import java.util.*;

public class Professor extends User implements common {
    private ArrayList<Course> courses;
    private String office_hours;
    private Feedback feedback;

    public Professor(String name, String email, String password, String office_hours) {
        super(name, email, password);
        this.courses = new ArrayList<>();
        this.office_hours = office_hours;
        this.feedback = new Feedback();
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public ArrayList<String> coursenamelist() {
        ArrayList<String> list = new ArrayList<>();
        for (Course c : courses) {
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
        if (code == null) return;
        for (Course c : courses) {
            if (c.getCode().equals(code)) {
                String syllabus = JOptionPane.showInputDialog("Enter syllabus for this course:");
                if(syllabus == null){
                    JOptionPane.showMessageDialog(null,"No syllabus added");
                }
                c.setSyllabus(syllabus);
                JOptionPane.showMessageDialog(null, "Syllabus changed to " + c.getSyllabus());
                return;
            }
        }

        JOptionPane.showMessageDialog(null, "Course does not exist");
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
        try {
            view_courses_taught();
            String code = JOptionPane.showInputDialog("Enter code for the course:");
            if (code == null || code.isEmpty()) {
                return;
            }
            boolean flag = false;
            for (Course course : courses) {
                if (code.equalsIgnoreCase(course.getCode())) {
                    String prereqCountStr = JOptionPane.showInputDialog("Enter number of prerequisites to add:");
                    if (prereqCountStr == null || prereqCountStr.isEmpty()) {
                        return;
                    }
                    int prereqCount = Integer.parseInt(prereqCountStr);

                    for (int i = 0; i < prereqCount; i++) {
                        String prerequisite = JOptionPane.showInputDialog("Enter prerequisite course code:");
                        if (prerequisite != null && !prerequisite.isEmpty()) {
                            Course prereqCourse = admin.getCourseCatalog().get_course_by_code(prerequisite);
                            if (prereqCourse != null) {
                                course.getPrereq().add(prereqCourse);
                            } else {
                                JOptionPane.showMessageDialog(null, "Prerequisite course not found.");
                            }
                        }
                    }
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                JOptionPane.showMessageDialog(null, "Course not found.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void change_credit() {
        String code = JOptionPane.showInputDialog("Enter code for the course:");
        if(code == null) return;
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
        try {
            String code = JOptionPane.showInputDialog("Enter code for the course:");
            if (code == null || code.isEmpty()) return;

            Course course = admin.getCourseCatalog().get_course_by_code(code);
            if (course == null) {
                JOptionPane.showMessageDialog(null, "Course not found.");
                return;
            }
            boolean continueEditing = true;
            while (continueEditing) {
                if (course.getTiming().isEmpty()) {
                    String numDaysStr = JOptionPane.showInputDialog("No timings assigned. Enter number of days to assign:");
                    int numDays = Integer.parseInt(numDaysStr);  // Could throw NumberFormatException

                    for (int i = 0; i < numDays; i++) {
                        String day = JOptionPane.showInputDialog("Enter day of the week:");
                        if (day == null || day.isEmpty()) continue;
                        String timing = JOptionPane.showInputDialog("Enter Timings (Start-End | Location):");
                        if (timing != null && !timing.isEmpty()) {
                            course.getTiming().put(day.toLowerCase(), timing);
                        }
                    }
                } else {
                    String[] options = {"Add Timing", "Change Timing", "Exit"};
                    int choice = JOptionPane.showOptionDialog(null, "Choose an action", "Timetable Management",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                    switch (choice) {
                        case 0:  // Add Timing
                            String numDaysToAddStr = JOptionPane.showInputDialog("Enter number of days to assign:");
                            int numDaysToAdd = Integer.parseInt(numDaysToAddStr);  // Could throw NumberFormatException

                            for (int i = 0; i < numDaysToAdd; i++) {
                                String day = JOptionPane.showInputDialog("Enter day of the week:");
                                if (day == null || day.isEmpty()) continue;
                                String timing = JOptionPane.showInputDialog("Enter Timings (Start-End | Location):");
                                if (timing != null && !timing.isEmpty()) {
                                    course.getTiming().put(day.toLowerCase(), timing);
                                }
                            }
                            break;
                        case 1:  // Change Timing
                            String dayToChange = JOptionPane.showInputDialog("Enter the day to change timing:").toLowerCase();
                            if (course.getTiming().containsKey(dayToChange)) {
                                String newTiming = JOptionPane.showInputDialog("Enter new timings (Start-End | Location):");
                                if (newTiming != null && !newTiming.isEmpty()) {
                                    course.getTiming().put(dayToChange, newTiming);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "No timing found for the given day.");
                            }
                            break;
                        case 2:  // Exit
                            continueEditing = false;
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Invalid option. Please try again.");
                            break;
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Timetable editing finished.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void view_student(Administrator admin) {
        String code = JOptionPane.showInputDialog("Enter code for the course:");
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
                    studentInfo.append("student.Student Name: ").append(student.getName()).append("\n")
                            .append("student.Student Email: ").append(student.getEmail()).append("\n")
                            .append("Grade: ").append(studentCourse.getGrade()).append("\n")
                            .append("-----------------------------\n");
                    studentFound = true;
                    break;
                }
            }
            for (StudentCourse studentCourse : student.getCompleted_courses()) {
                if (code.equalsIgnoreCase(studentCourse.getCourse().getCode())) {
                    studentInfo.append("student.Student Name: ").append(student.getName()).append("\n")
                            .append("student.Student Email: ").append(student.getEmail()).append("\n")
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

    public void make_TA(Administrator admin) {
        String username = JOptionPane.showInputDialog("Enter the student's email to make TA:");
        if (username == null) return;
        Student student = admin.getStudentList().find_student_by_email(username);

        if (student != null) {
            String courseCode = JOptionPane.showInputDialog("Enter the course code to assign the TA:");
            Course course = admin.getCourseCatalog().get_course_by_code(courseCode);

            if (course != null) {
                TA newTA = new TA(student.getName(),
                        student.getEmail(),
                        student.getPassword(),
                        student.getCourses(),
                        student.getCourse_catalog(),
                        student.getCompleted_courses(),
                        student.getSGPA(),
                        student.getSemester(),
                        student.getCredit(),
                        student.getNo_completed_courses(),
                        true);
                admin.getStudentList().getStudentList().remove(student);
                admin.getStudentList().getStudentList().add(newTA);

                if (course.getTA_list().find_ta(newTA) != null) {
                    JOptionPane.showMessageDialog(null, "TA: " + newTA.getName() + " is already assigned to the course.");
                } else {
                    course.getTA_list().add(newTA);
                    newTA.add_course(course);
                    JOptionPane.showMessageDialog(null, "TA: " + newTA.getName() + " successfully added to the course " + course.getTitle());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Course does not exist.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Student not found.");
        }
    }

    public void remove_ta(Administrator admin) {
        String username = JOptionPane.showInputDialog("Enter the TA's email:");
        Student student = admin.getStudentList().find_student_by_email(username);

        if (student != null && student.isTA()) {
            String courseCode = JOptionPane.showInputDialog("Enter the course code to remove the TA:");
            Course course = admin.getCourseCatalog().get_course_by_code(courseCode);

            if (course != null) {
                TA taToRemove = course.getTA_list().find_ta(new TA(student.getName(), student.getEmail(), student.getPassword(), student.getCourses(), student.getCourse_catalog(), student.getCompleted_courses(), student.getSGPA(), student.getSemester(), student.getCredit(), student.getSemester(), true));
                if (taToRemove != null) {
                    taToRemove.remove_course(course);
                    course.getTA_list().remove(taToRemove);
                    JOptionPane.showMessageDialog(null, "TA removed from the course: " + course.getTitle());
                } else {
                    JOptionPane.showMessageDialog(null, "TA not assigned to the course.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Course not found.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Student is not a TA or does not exist.");
        }
    }

    public void view_ta() {
        String courseCode = JOptionPane.showInputDialog("Enter the course code:");
        if (courseCode == null) return;
        Course course = null;

        for (Course c : this.courses) {
            if (courseCode.equalsIgnoreCase(c.getCode())) {
                course = c;
                break;
            }
        }

        if (course == null) {
            JOptionPane.showMessageDialog(null, "Course code does not exist.");
        } else {
            if (course.getTA_list().getTa_list().isEmpty()) {
                JOptionPane.showMessageDialog(null, "There are no TAs assigned to this course.");
            } else {
                StringBuilder taInfo = new StringBuilder("TAs for the course " + course.getTitle() + ":\n");
                for (TA ta : course.getTA_list().getTa_list()) {
                    taInfo.append(ta.getName()).append(" - ").append(ta.getEmail()).append("\n");
                }
                JOptionPane.showMessageDialog(null, taInfo.toString());
            }
        }
    }

    public void viewFeedback() {
        String code = JOptionPane.showInputDialog("Enter the course code:");
        if (code == null) return;
        Course course = null;
        for (Course c : this.courses) {
            if (c.getCode().equals(code)) {
                course = c;
            }
        }
        if (course == null) {
            JOptionPane.showMessageDialog(null, "Course is not assigned to you.");
        } else {
            ArrayList<Feedback<?>> feedbackList = course.getFeedback();

            if (!feedbackList.isEmpty()) {
                StringBuilder feedbackMessages = new StringBuilder("Feedback for course: ").append(course.getTitle()).append("\n");
                for (Feedback<?> feedback : feedbackList) {
                    feedbackMessages.append(feedback.getFeedbackList().toString()).append("\n");
                }
                JOptionPane.showMessageDialog(null, feedbackMessages.toString());
            } else {
                JOptionPane.showMessageDialog(null, "No feedback available for this course.");
            }
        }
    }
}