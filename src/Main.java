import Proff.Professor;
import admin.Administrator;
import exceptions.*;
import helper.infinite_input;
import student.Student;
import student.TA;


import javax.swing.JOptionPane;
public class Main extends infinite_input {
    public static void main(String[] args) {

        Administrator admin = new Administrator();
        boolean appRunning = true;
        while (appRunning) {
            String[] options = {"Login as Admin", "Login as Student", "Login as Professor", "Exit Application"};
            int choice = JOptionPane.showOptionDialog(null, "Welcome to ERP of IIIT-Delhi", "ERP-IIITD", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            try {
                switch (choice) {
                    case 0:
                        handleAdminLogin(admin);
                        break;

                    case 1:
                        handleStudentLogin(admin);
                        break;

                    case 2:
                        handleProfessorLogin(admin);
                        break;

                    case 3:
                        appRunning = false;
                        JOptionPane.showMessageDialog(null, "Exiting the application...");
                        break;

                    default:
                        JOptionPane.showMessageDialog(null, "Invalid choice. Please try again.");
                        break;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private static void handleAdminLogin(Administrator admin) {
        try {
            if (admin.login()) {
                boolean adminSession = true;
                JOptionPane.showMessageDialog(
                        null, "Admin logged in. | Current semester is " + admin.getSemester() + ", change for students/professors to access winter sem.");
                while (adminSession) {
                    String[] adminOptions = {"Manage Course Catalog", "Manage Students", "Manage Professors", "Manage Complaints", "Change Semester", "Change Password", "Logout"};
                    int adminChoice = JOptionPane.showOptionDialog(null, "Admin Menu", "Select an option", JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, null, adminOptions, adminOptions[0]);

                    switch (adminChoice) {
                        case 6:
                            adminSession = false;
                            JOptionPane.showMessageDialog(null, "Logged out as Admin.");
                            break;
                        case 0:
                            admin.manage_course_catalog(admin);
                            break;
                        case 1:
                            admin.manage_students(admin);
                            break;
                        case 2:
                            admin.manage_proff();
                            break;
                        case 3:
                            admin.manage_complaints();
                            break;
                        case 4:
                            String semester = JOptionPane.showInputDialog("Enter the new semester (Monsoon or Winter):");
                            if (semester == null) {
                                admin.change_sem(semester);
                            }
                            break;
                        case 5:
                            admin.changepass();
                        default:
                            JOptionPane.showMessageDialog(null, "Invalid choice. Please try again.");
                            break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Admin login failed. Try again.");
            }
        }catch (InvalidLoginExcepiton e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private static void handleStudentLogin(Administrator admin) {
        String email = JOptionPane.showInputDialog("Enter your email to search your account:");
        if(email == null) return;
        Student student = admin.find_student(email);
        try {
            if (student != null && student.login()) {
                boolean studentSession = true;
                JOptionPane.showMessageDialog(null, "Student logged in.");
                if (!student.isTA()) {
                    while (studentSession) {
                        // Superoptions menu
                        String[] superOptions = {"Course-related options", "Complaint-related options", "Grade-related options", "Change Password", "Logout"};
                        int superChoice = JOptionPane.showOptionDialog(null, "Student Menu", "Select an option", JOptionPane.DEFAULT_OPTION,
                                JOptionPane.INFORMATION_MESSAGE, null, superOptions, superOptions[0]);

                        switch (superChoice) {
                            case 4: // Logout
                                studentSession = false;
                                JOptionPane.showMessageDialog(null, "Logged out as Student.");
                                break;

                            case 0: // Course-related options
                                String[] courseOptions = {"View available courses", "Enroll in a course", "Drop a course", "View enrolled courses",
                                        "View completed courses", "View course syllabus", "View Schedule", "Feedback Menu", "Exit"};

                                boolean exit0 = false;
                                while (!exit0) {
                                    int courseChoice = JOptionPane.showOptionDialog(null, "Course Menu", "Select an option", JOptionPane.DEFAULT_OPTION,
                                            JOptionPane.INFORMATION_MESSAGE, null, courseOptions, courseOptions[0]);
                                    switch (courseChoice) {
                                        case 0:
                                            student.view_available_courses(admin);
                                            break;
                                        case 1:
                                            String courseCode = JOptionPane.showInputDialog("Enter the course code:");
                                            if (courseCode == null) return;

                                            try {
                                                student.enroll_in_course(courseCode);
                                            } catch (CourseFullException e) {
                                                JOptionPane.showMessageDialog(null, "Course is full: " + e.getMessage());
                                            } catch (PrerequisiteNotMetException e) {
                                                JOptionPane.showMessageDialog(null, "Prerequisites not met: " + e.getMessage());
                                            } catch (DuplicateEnrollmentException e) {
                                                JOptionPane.showMessageDialog(null, "Already enrolled in the course: " + e.getMessage());
                                            } catch (CreditLimitExceededException e) {
                                                JOptionPane.showMessageDialog(null, "Credit limit exceeded: " + e.getMessage());
                                            } catch (Exception e) {
                                                JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e.getMessage());
                                            }
                                            break;

                                        case 2:
                                            try {
                                                student.drop_course();
                                            } catch (DropDeadlinePassedException e) {
                                                JOptionPane.showMessageDialog(null, e.getMessage());
                                            }
                                            break;
                                        case 3:
                                            student.view_enrolled_course();
                                            break;
                                        case 4:
                                            student.view_completed_course();
                                            break;
                                        case 5:
                                            student.view_syllabus();
                                            break;
                                        case 6:
                                            student.view_timing();
                                            break;
                                        case 7:
                                            student.give_feedback();
                                            break;
                                        case 8:
                                            exit0 = true;
                                            break;
                                        default:
                                            JOptionPane.showMessageDialog(null, "Invalid choice. Please try again.");
                                    }
                                }
                                break;


                            case 1: // Complaint-related options
                                String[] complaintOptions = {"Add a complaint", "View complaints", "Check complaint status", "Exit"};

                                boolean exit1 = false;
                                while (!exit1) {
                                    int complaintChoice = JOptionPane.showOptionDialog(null, "Complaint Menu", "Select an option", JOptionPane.DEFAULT_OPTION,
                                            JOptionPane.INFORMATION_MESSAGE, null, complaintOptions, complaintOptions[0]);
                                    switch (complaintChoice) {
                                        case 0:
                                            student.add_complaint(admin);
                                            break;
                                        case 1:
                                            student.view_complaints(admin);
                                            break;
                                        case 2:
                                            student.complaint_status(admin);
                                            break;
                                        case 3:
                                            exit1 = true;
                                            break;
                                        default:
                                            JOptionPane.showMessageDialog(null, "Invalid choice. Please try again.");
                                    }
                                }
                                break;


                            case 2: // Grade-related options
                                String[] gradeOptions = {"View SGPA", "Calculate CGPA", "View Grade", "Exit"};

                                boolean exit2 = false;
                                while (!exit2) {
                                    int gradeChoice = JOptionPane.showOptionDialog(null, "Grade Menu", "Select an option", JOptionPane.DEFAULT_OPTION,
                                            JOptionPane.INFORMATION_MESSAGE, null, gradeOptions, gradeOptions[0]);
                                    switch (gradeChoice) {
                                        case 0:
                                            student.view_SGPA();
                                            break;
                                        case 1:
                                            student.find_view_CGPA();
                                            break;
                                        case 2:
                                            student.view_grade();
                                            break;
                                        case 3:
                                            exit2 = true;
                                            break;
                                        default:
                                            JOptionPane.showMessageDialog(null, "Invalid choice. Please try again.");
                                    }
                                }
                                break;


                            case 3: // Change password
                                student.changepass();
                                break;

                            default:
                                JOptionPane.showMessageDialog(null, "Invalid choice. Please try again.");
                                break;
                        }
                    }
                } else {
                    // Superoptions menu
                    String[] superOptions = {"Course-related options", "Complaint-related options", "Grade-related options", "Change Password", "Manage TAship", "Logout"};
                    int superChoice = JOptionPane.showOptionDialog(null, "Student Menu", "Select an option", JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, null, superOptions, superOptions[0]);

                    switch (superChoice) {
                        case 5: // Logout
                            studentSession = false;
                            JOptionPane.showMessageDialog(null, "Logged out as Student.");
                            break;

                        case 0: // Course-related options
                            String[] courseOptions = {"View available courses", "Enroll in a course", "Drop a course", "View enrolled courses",
                                    "View completed courses", "View course syllabus", "View Schedule", "Feedback Menu", "Exit"};

                            boolean exit0 = false;
                            while (!exit0) {
                                int courseChoice = JOptionPane.showOptionDialog(null, "Course Menu", "Select an option", JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.INFORMATION_MESSAGE, null, courseOptions, courseOptions[0]);
                                switch (courseChoice) {
                                    case 0:
                                        student.view_available_courses(admin);
                                        break;
                                    case 1:
                                        String courseCode = JOptionPane.showInputDialog("Enter the course code:");
                                        if (courseCode == null) return;

                                        try {
                                            student.enroll_in_course(courseCode);
                                        } catch (CourseFullException e) {
                                            JOptionPane.showMessageDialog(null, "Course is full: " + e.getMessage());
                                        } catch (PrerequisiteNotMetException e) {
                                            JOptionPane.showMessageDialog(null, "Prerequisites not met: " + e.getMessage());
                                        } catch (DuplicateEnrollmentException e) {
                                            JOptionPane.showMessageDialog(null, "Already enrolled in the course: " + e.getMessage());
                                        } catch (CreditLimitExceededException e) {
                                            JOptionPane.showMessageDialog(null, "Credit limit exceeded: " + e.getMessage());
                                        } catch (Exception e) {
                                            JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e.getMessage());
                                        }
                                        break;

                                    case 2:
                                        try {
                                            student.drop_course();
                                        } catch (DropDeadlinePassedException e) {
                                            JOptionPane.showMessageDialog(null, e.getMessage());
                                        }
                                        break;
                                    case 3:
                                        student.view_enrolled_course();
                                        break;
                                    case 4:
                                        student.view_completed_course();
                                        break;
                                    case 5:
                                        student.view_syllabus();
                                        break;
                                    case 6:
                                        student.view_timing();
                                        break;
                                    case 7:
                                        student.give_feedback();
                                        break;
                                    case 8:
                                        exit0 = true;
                                        break;
                                    default:
                                        JOptionPane.showMessageDialog(null, "Invalid choice. Please try again.");
                                }
                            }
                            break;

                        case 1: // Complaint-related options
                            String[] complaintOptions = {"Add a complaint", "View complaints", "Check complaint status", "Exit"};

                            boolean exitt1 = false;
                            while (!exitt1) {
                                int complaintChoice = JOptionPane.showOptionDialog(null, "Complaint Menu", "Select an option", JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.INFORMATION_MESSAGE, null, complaintOptions, complaintOptions[0]);
                                switch (complaintChoice) {
                                    case 0:
                                        student.add_complaint(admin);
                                        break;
                                    case 1:
                                        student.view_complaints(admin);
                                        break;
                                    case 2:
                                        student.complaint_status(admin);
                                        break;
                                    case 3:
                                        exitt1 = true;
                                        break;
                                    default:
                                        JOptionPane.showMessageDialog(null, "Invalid choice. Please try again.");
                                }
                            }
                            break;


                        case 2: // Grade-related options
                            String[] gradeOptions = {"View SGPA", "Calculate CGPA", "View Grade", "Exit"};

                            boolean exitt2 = false;
                            while (!exitt2) {
                                int gradeChoice = JOptionPane.showOptionDialog(null, "Grade Menu", "Select an option", JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.INFORMATION_MESSAGE, null, gradeOptions, gradeOptions[0]);
                                switch (gradeChoice) {
                                    case 0:
                                        student.view_SGPA();
                                        break;
                                    case 1:
                                        student.find_view_CGPA();
                                        break;
                                    case 2:
                                        student.view_grade();
                                        break;
                                    case 3:
                                        exitt2 = true;
                                        break;
                                    default:
                                        JOptionPane.showMessageDialog(null, "Invalid choice. Please try again.");
                                }
                            }
                            break;


                        case 3: // Change password
                            student.changepass();
                            break;

                        case 4:
                            TA new_ta = (TA) student;
                            String[] TAOptions = {"View TAship course", "Grade A student", "View Student's progress", "Exit"};

                            boolean exitt3 = false;
                            while (!exitt3) {
                                int TAChoice = JOptionPane.showOptionDialog(null, "TA Menu", "Select an option", JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.INFORMATION_MESSAGE, null, TAOptions, TAOptions[0]);
                                switch (TAChoice) {
                                    case 0:
                                        new_ta.view_ta_courses();
                                        break;
                                    case 1:
                                        new_ta.grade_student(admin);
                                        break;
                                    case 2:
                                        new_ta.view_student_progress(admin);
                                        break;
                                    case 3:
                                        exitt3 = true;
                                        break;
                                    default:
                                        JOptionPane.showMessageDialog(null, "Invalid choice. Please try again.");
                                }
                            }
                            break;

                        default:
                            JOptionPane.showMessageDialog(null, "Invalid choice. Please try again.");
                            break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Student not found or login failed.");
            }
        }catch (InvalidLoginExcepiton e){
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }

    private static void handleProfessorLogin(Administrator admin) {
        String email = JOptionPane.showInputDialog("Enter your email to search your account:");
        if(email == null) return;
        Professor professor = admin.find_professor(email);
        try {
            if (professor != null && professor.login()) {
                boolean professorSession = true;
                JOptionPane.showMessageDialog(null, "Professor logged in.");

                while (professorSession) {
                    // Superoptions menu
                    String[] superOptions = {"Course-related options", "Syllabus-related options", "Account-related options", "TA-related problem", "Logout"};
                    int superChoice = JOptionPane.showOptionDialog(null, "Professor Menu", "Select an option", JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, null, superOptions, superOptions[0]);

                    switch (superChoice) {
                        case 4: // Logout
                            professorSession = false;
                            JOptionPane.showMessageDialog(null, "Logged out as Professor.");
                            break;

                        case 0: // Course-related options
                            boolean courseSession = true;
                            while (courseSession) {
                                String[] courseOptions = {"View Courses Taught", "Change Credits for a Course", "Change Timetable for a Course",
                                        "View Student Details for a Course", "Add Prerequisites to a Course", "Add Syllabus", "View Feedback", "Exit"};
                                int courseChoice = JOptionPane.showOptionDialog(null, "Course Menu", "Select an option", JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.INFORMATION_MESSAGE, null, courseOptions, courseOptions[0]);

                                switch (courseChoice) {
                                    case 0:
                                        professor.view_courses_taught();
                                        break;
                                    case 1:
                                        professor.change_credit();
                                        break;
                                    case 2:
                                        professor.change_timetable(admin);
                                        break;
                                    case 3:
                                        professor.view_student(admin);
                                        break;
                                    case 4:
                                        professor.add_prereg(admin);
                                        break;
                                    case 5:
                                        professor.change_syllabus();
                                        break;
                                    case 6:
                                        professor.viewFeedback();
                                        break;
                                    case 7:
                                        courseSession = false; // Exit course options
                                        break;
                                    default:
                                        JOptionPane.showMessageDialog(null, "Invalid choice. Please try again.");
                                }
                            }
                            break;

                        case 1: // Syllabus-related options
                            boolean syllabusSession = true;
                            while (syllabusSession) {
                                String[] syllabusOptions = {"View Syllabus for All Courses", "Change Syllabus", "Exit"};
                                int syllabusChoice = JOptionPane.showOptionDialog(null, "Syllabus Menu", "Select an option", JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.INFORMATION_MESSAGE, null, syllabusOptions, syllabusOptions[0]);

                                switch (syllabusChoice) {
                                    case 0:
                                        professor.view_syllabus();
                                        break;
                                    case 1:
                                        professor.change_syllabus();
                                        break;
                                    case 2:
                                        syllabusSession = false; // Exit syllabus options
                                        break;
                                    default:
                                        JOptionPane.showMessageDialog(null, "Invalid choice. Please try again.");
                                }
                            }
                            break;

                        case 2: // Account-related options
                            boolean accountSession = true;
                            while (accountSession) {
                                String[] accountOptions = {"Change Password", "Exit"};
                                int accountChoice = JOptionPane.showOptionDialog(null, "Account Menu", "Select an option", JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.INFORMATION_MESSAGE, null, accountOptions, accountOptions[0]);

                                switch (accountChoice) {
                                    case 0:
                                        professor.changepass();
                                        break;
                                    case 1:
                                        accountSession = false; // Exit account options
                                        break;
                                    default:
                                        JOptionPane.showMessageDialog(null, "Invalid choice. Please try again.");
                                }
                            }
                            break;
                        case 3:
                            boolean TASession = true;
                            while (TASession) {
                                String[] TAOptions = {"Make TA for course", "Remove TA", "View TA", "Exit"};
                                int TAChoice = JOptionPane.showOptionDialog(null, "Account Menu", "Select an option", JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.INFORMATION_MESSAGE, null, TAOptions, TAOptions[0]);

                                switch (TAChoice) {
                                    case 0:
                                        JOptionPane.showMessageDialog(null, "Enter the number of TAs to add");

                                        int no_of_TA = loop_input();
                                        for (int i = 0; i < no_of_TA; i++) {
                                            professor.make_TA(admin);
                                        }
                                        break;
                                    case 1:
                                        professor.remove_ta(admin);
                                        break;
                                    case 2:
                                        professor.view_ta();
                                        break;
                                    case 3:
                                        TASession = false;
                                        break;
                                    default:
                                        JOptionPane.showMessageDialog(null, "Invalid choice. Please try again.");
                                }
                            }
                            break;

                        default:
                            JOptionPane.showMessageDialog(null, "Invalid choice. Please try again.");
                            break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Professor not found or login failed.");
            }
        }catch (InvalidLoginExcepiton e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

}