package admin;


import course.*;
import student.*;
import Proff.*;
import helper.*;
import complain.*;

import javax.swing.*;
import java.util.ArrayList;

public class Administrator extends User  {
    private Course_catalog course_catalog;
    private Student_list student_list;
    private ArrayList<Complaint> complaints;
    private String semester;
    private Professor_list professor_list;

    public Administrator() {
        super("Admin", "admin@iiitd.ac.in", "12345");
        this.course_catalog = new Course_catalog();
        this.student_list = new Student_list();
        this.complaints = new ArrayList<>();
        this.professor_list = new Professor_list();
        this.semester = "Monsoon";
    }

    public Course_catalog getCourseCatalog() {
        return this.course_catalog;
    }

    public Student_list getStudentList() {
        return this.student_list;
    }

    public ArrayList<Complaint> getcomplaints() {
        return this.complaints;
    }

    public void setcomplaints(ArrayList<Complaint> complaints) {
        this.complaints = complaints;
    }

    public String getSemester() {
        return this.semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void change_sem(String sem) {
        this.semester = sem;
    }

    public Professor_list getProfessor_list() {
        return this.professor_list;
    }

    public void setProfessor_list(Professor_list professor_list) {
        this.professor_list = professor_list;
    }

    public void manage_students(Administrator admin) {
        boolean exit = false;
        while (!exit) {
            String[] options = {"Add student", "View all students", "Delete student", "Assign grade to student", "Assign SGPA to student", "Update student", "Exit"};
            int choice = JOptionPane.showOptionDialog(null, "Manage Students", "Choose an action", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0:
                    this.student_list.add_student(admin);
                    break;
                case 1:
                    this.student_list.view_students();
                    break;
                case 2:
                    this.student_list.delete_student();
                    break;
                case 3:
                    this.student_list.assign_grade();
//                    this.student_list.change_sem();
                    break;
                case 4:
                    this.student_list.find_and_assign_sgpa();
                    break;
                case 5:
                    this.student_list.update_student();
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid choice, please try again.");
            }
        }
    }

    public Student find_student(String email) {
        return student_list.find_student_by_email(email);
    }

    public Professor find_professor(String email) {
        return professor_list.findProfessorByEmail(email);
    }

    public void view_complaint() {
        StringBuilder complaintView = new StringBuilder();
        for (Complaint c : complaints) {
            complaintView.append("Complaint_ID: ").append(c.getComplaint_id())
                    .append(", Student_email: ").append(c.getStudent().getEmail())
                    .append(" - ").append(c.getComplaint()).append("\n");
        }
        JOptionPane.showMessageDialog(null, complaintView.toString(), "View Complaints", JOptionPane.INFORMATION_MESSAGE);
    }

    public void resolved_complaint() {
        try {
            String input = JOptionPane.showInputDialog("Enter Complaint_ID:");
            if (input == null || input.isEmpty()) {
                return;
            }
            int id = Integer.parseInt(input);
            boolean complaintFound = false;
            for (Complaint c : this.complaints) {
                if (c.getComplaint_id() == id) {
                    c.setResolved(true);
                    c.setPending(false);
                    String remarks = JOptionPane.showInputDialog("Enter remarks:");
                    c.setRemarks(remarks);
                    JOptionPane.showMessageDialog(null,
                            "Complaint_ID: " + c.getComplaint_id() + " resolved successfully.",
                            "Resolved Complaint",
                            JOptionPane.INFORMATION_MESSAGE);
                    complaintFound = true;
                    break;
                }
            }
            if (!complaintFound) {
                JOptionPane.showMessageDialog(null, "Complaint_ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid Complaint_ID format. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void manage_course_catalog(Administrator admin) {
        boolean exit = false;
        while (!exit) {
            String[] options = {"Add course", "View all courses", "Delete course", "Manage Drop Deadline","Exit"};
            int choice = JOptionPane.showOptionDialog(null, "Manage Courses", "Choose an action", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0:
                    this.course_catalog.add_course(admin);
                    break;
                case 1:
                    this.course_catalog.view_all_courses();
                    break;
                case 2:
                    this.course_catalog.delete_course(admin);
                    break;
                case 3:
                    this.course_catalog.deadline(admin);
                case 4:
                    exit = true;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid choice, please try again.");
            }
        }
    }

    public void manage_complaints() {
        boolean exit = false;
        while (!exit) {
            String[] options = {"Resolve complaint", "View all complaints", "Exit"};
            int choice = JOptionPane.showOptionDialog(null, "Manage Complaints", "Choose an action", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0:
                    resolved_complaint();
                    break;
                case 1:
                    view_complaint();
                    break;
                case 2:
                    exit = true;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid choice, please try again.");
            }
        }
    }

    public void manage_proff() {
        boolean exit = false;
        while (!exit) {
            String[] options = {"Add professor", "View all professors", "Delete professor", "Assign course to professor", "Update professor", "Exit"};
            int choice = JOptionPane.showOptionDialog(null, "Manage Professors", "Choose an action", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0:
                    this.professor_list.addProfessor();
                    break;
                case 1:
                    this.professor_list.viewProfessors();
                    break;
                case 2:
                    this.professor_list.deleteProfessor();
                    break;
                case 3:
                    String course_code = JOptionPane.showInputDialog("Enter course code to assign to this professor:");
                    if (course_code == null || course_code.isEmpty()) {
                        return;
                    }
                    Course c = this.getCourseCatalog().get_course_by_code(course_code);
                    if (c == null) {
                        JOptionPane.showMessageDialog(null, "Course does not exist", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        this.professor_list.assign_prof_to_course(c);
                    }
                    break;
                case 4:
                    this.professor_list.updateProfessor();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid choice, please try again.");
            }
        }
    }

    @Override
    public void changepass() {
        String currentPassword = JOptionPane.showInputDialog("Enter your current password:");
        if (currentPassword == null || currentPassword.isEmpty()) {
            return;
        }
        if (currentPassword != null && currentPassword.equals(this.getPassword())) {
            String newPassword = JOptionPane.showInputDialog("Enter your new password:");
            if (newPassword == null) {
                JOptionPane.showMessageDialog(null, "Password not changed");
                return;
            }
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
