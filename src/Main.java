import javax.swing.JOptionPane;
public class Main extends infinite_input {
    public static void main(String[] args) {

        Administrator admin = new Administrator();
        boolean appRunning = true;
        while (appRunning) {
            String[] options = {"Login as Admin", "Login as Student", "Login as Teacher", "Exit Application"};
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
        if (admin.login()) {
            boolean adminSession = true;
            JOptionPane.showMessageDialog(
                    null, "Admin logged in. | Current semester is "+admin.getSemester()+", change for students/professors to access winter sem.");
            while (adminSession) {
                String[] adminOptions = {"Manage Course Catalog", "Manage Students", "Manage Professors", "Manage Complaints", "Change Semester","Change Password", "Logout"};
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
    }
    private static void handleStudentLogin(Administrator admin) {
        String email = JOptionPane.showInputDialog("Enter your email to search your account:");
        Student student = admin.find_student(email);

        if (student != null && student.login()) {
            boolean studentSession = true;
            JOptionPane.showMessageDialog(null, "Student logged in.");

            while (studentSession) {
                String[] studentOptions = {"View available courses", "Enroll in a course", "Drop a course", "View enrolled courses", "View completed courses",
                        "View SGPA", "Calculate CGPA", "Add a complaint", "View complaints", "Check complaint status", "View course syllabus","View Schedule","Change Password","View Grade", "Logout"};
                int studentChoice = JOptionPane.showOptionDialog(null, "Student Menu", "Select an option", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE, null, studentOptions, studentOptions[0]);

                switch (studentChoice) {
                    case 14:
                        studentSession = false;
                        JOptionPane.showMessageDialog(null, "Logged out as Student.");
                        break;
                    case 0:
                        student.view_available_courses(admin);
                        break;
                    case 1:
                        String courseCode = JOptionPane.showInputDialog("Enter the course code:");
                        if (courseCode == null) return;
                        if (courseCode != null) {
                            student.enroll_in_course(courseCode);
                        }
                        break;
                    case 2:
                        student.drop_course();
                        break;
                    case 3:
                        student.view_enrolled_course();
                        break;
                    case 4:
                        student.view_completed_course();
                        break;
                    case 5:
                        student.view_SGPA();
                        break;
                        
                    case 6:
                        student.find_view_CGPA();
                        break;
                    case 7:
                        student.add_complaint(admin);
                        break;
                    case 8:
                        student.view_complaints(admin);
                        break;
                    case 9:
                        student.complaint_status(admin);
                        break;
                    case 10:
                        student.view_syllabus();
                        break;
                    case 11:
                        student.view_timing();
                    case 12:
                        student.changepass();
                    case 13:
                        student.view_grade();
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid choice. Please try again.");
                        break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Student not found or login failed.");
        }
    }
    private static void handleProfessorLogin(Administrator admin) {
        String email = JOptionPane.showInputDialog("Enter your email to search your account:");
        Professor professor = admin.find_professor(email);

        if (professor != null && professor.login()) {
            boolean professorSession = true;
            JOptionPane.showMessageDialog(null, "Professor logged in.");

            while (professorSession) {
                String[] professorOptions = {"View Courses Taught", "Change Credits for a Course", "Change Timetable for a Course", "View Student Details for a Course",
                        "Add Prerequisites to a Course", "View Syllabus for All Courses", "Change password","Add syllabus","Logout"};
                int professorChoice = JOptionPane.showOptionDialog(null, "Professor Menu", "Select an option", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE, null, professorOptions, professorOptions[0]);

                try {
                    switch (professorChoice) {
                        case 8:
                            professorSession = false;
                            JOptionPane.showMessageDialog(null, "Logged out as Professor.");
                            break;
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
                            professor.view_syllabus();
                            break;
                        case 6:
                            professor.changepass();
                        case 7:
                            professor.change_syllabus();
                        default:
                            JOptionPane.showMessageDialog(null, "Invalid choice. Please try again.");
                            break;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Professor not found or login failed.");
        }
    }
}
