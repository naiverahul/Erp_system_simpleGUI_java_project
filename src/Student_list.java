import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;

public class Student_list {
    private ArrayList<Student> student_list;

    public Student_list() {
        this.student_list = new ArrayList<>();
    }

    public void setStudent_list(ArrayList<Student> student_list) {
        this.student_list = student_list;
    }

    public ArrayList<Student> getStudentList() {
        return this.student_list;
    }

    public Student find_student_by_email(String email) {
        for(Student student : student_list) {
            if(student.getEmail().equalsIgnoreCase(email)) {
                return student;
            }
        }
        return null;
    }

    public void add_student(Administrator admin) {
        String name = JOptionPane.showInputDialog("Enter student name:");
        if(name == null) {return;}
        String email = JOptionPane.showInputDialog("Enter student email:");
        if(email == null) {return;}
        Student s = find_student_by_email(email);

        if (s == null) {
            String password = JOptionPane.showInputDialog("Enter Password:");
            if(password == null) return;
            Course_catalog courseCatalog = admin.getCourseCatalog();
            int sem = Integer.parseInt(JOptionPane.showInputDialog("Enter semester of this student:"));
            Student new_student = new Student(name, email, password, courseCatalog, sem);
            student_list.add(new_student);
            JOptionPane.showMessageDialog(null, "Student successfully added!");
        } else {
            JOptionPane.showMessageDialog(null, "Student already exists.");
        }
    }

    public void view_students() {
        if (this.student_list.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No students available.");
        } else {
            StringBuilder studentsInfo = new StringBuilder("Students List:\n");
            for (Student s : student_list) {
                studentsInfo.append("Name: ").append(s.getName()).append(", Email: ").append(s.getEmail()).append("\n");
            }
            JOptionPane.showMessageDialog(null, studentsInfo.toString());
        }
    }

    public void update_student() {
        String email = JOptionPane.showInputDialog("Enter student email:");
        if (email == null) {return;}
        Student s = find_student_by_email(email);
        if (s == null) {
            JOptionPane.showMessageDialog(null, "Student does not exist.");
        } else {
            String[] options = {"Change name", "Change email", "Change password", "Change course", "Change semester", "Exit"};
            boolean exit = false;
            while (!exit) {
                int choice = JOptionPane.showOptionDialog(null, "Select an action", "Update Student",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                switch (choice) {
                    case 0:
                        String name = JOptionPane.showInputDialog("Enter student name:");
                        if (name == null) {return;}
                        s.setName(name);
                    case 1 :
                        String email_id = JOptionPane.showInputDialog("Enter student new email:");
                        if (email_id == null) {return;}
                        s.setEmail(email_id);
                    case 2 :
                        String password = JOptionPane.showInputDialog("Enter student new password:");
                        if (password == null) {return;}
                        s.setPassword(password);
                    case 3 : {
                        String courseCode = JOptionPane.showInputDialog("Enter course code to remove:");
                        if (courseCode == null) {return;}
                        boolean courseFound = false;
                        for (StudentCourse c : s.getCourses()) {
                            if (courseCode.equalsIgnoreCase(c.getCourse().getCode())) {
                                courseFound = true;
                                s.getCourses().remove(c);
                                JOptionPane.showMessageDialog(null, "Course removed");
                                break;
                            }
                        }
                        if (!courseFound) {
                            JOptionPane.showMessageDialog(null, "Course not found.");
                        }
                    }
                    case 4 : {
                        int sem = Integer.parseInt(JOptionPane.showInputDialog("Enter new semester:"));
                        s.setSemester(sem);
                        break;
                    }
                    case 5 :
                        exit = true;
                    default :
                        JOptionPane.showMessageDialog(null, "Invalid input.");
                }
            }
        }
    }

    public void delete_student() {
        String name = JOptionPane.showInputDialog("Enter student email:");
        if (name == null) {return;}
        Student s = find_student_by_email(name);
        if (s == null) {
            JOptionPane.showMessageDialog(null, "Student does not exist.");
        } else {
            student_list.remove(s);
            JOptionPane.showMessageDialog(null, "Student " + s.getName() + " successfully deleted.");
        }
    }

    public void assign_grade() {
        String email = JOptionPane.showInputDialog("Enter student email:");
        if (email == null) {return;}
        Student student = find_student_by_email(email);
        if (student != null) {
            while (true) {
                String code = JOptionPane.showInputDialog("Enter course code to assign grade (or type 'exit' to cancel):");
                if (code.equalsIgnoreCase("exit")) {
                    JOptionPane.showMessageDialog(null, "Grade assignment cancelled.");
                    break;
                }
                if(code == null) return;
                StudentCourse courseToGrade = null;
                for (StudentCourse c : student.getCourses()) {
                    if (c.getCourse().getCode().equalsIgnoreCase(code)) {
                        courseToGrade = c;
                        break;
                    }
                }

                if (courseToGrade != null) {
                    String grade = JOptionPane.showInputDialog("Enter grade (A+, A, B, C, D, E, F):");
                    if (grade==null) return;
                    while (!grade.matches("A\\+|A|B|C|D|E|F")) {
                        grade = JOptionPane.showInputDialog("Invalid grade. Enter again (A+, A, B, C, D, E, F):");
                    }
                    courseToGrade.setGrade(grade);
                    student.getCourses().remove(courseToGrade);
                    if (!grade.equals("F")) {
                        student.getCompleted_courses().add(courseToGrade);
                    }
                    JOptionPane.showMessageDialog(null, "Grade " + grade + " assigned for course " + code);
                    break;
                } else {
                    JOptionPane.showMessageDialog(null, "Course not found.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Student not found.");
        }
    }

    public void find_and_assign_sgpa() {
        String email = JOptionPane.showInputDialog("Enter student email:");
        if (email == null) {return;}
        Student s = find_student_by_email(email);
        if (s == null) {
            JOptionPane.showMessageDialog(null, "Student does not exist.");
        } else {
            float totalGradePoints = 0;
            float totalCredit = 0;
            int numCourses = s.getCompleted_courses().size();
            if (numCourses == 0) {
                JOptionPane.showMessageDialog(null, "No completed courses to calculate SGPA.");
                return;
            }
            for (StudentCourse c : s.getCompleted_courses()) {
                totalGradePoints += floatgrade(c.getGrade())*c.getCourse().getCredits();
            }
            for(StudentCourse c : s.getCompleted_courses()) {
                totalCredit += c.getCourse().getCredits();
            }
            float sgpa =0;
            try {
                sgpa = totalGradePoints / totalCredit;
            }catch(ArithmeticException e){
                System.out.println("Arithmetic exception");
            }
            s.getSGPA().put(s.getSemester(), sgpa);
            JOptionPane.showMessageDialog(null, "Student " + s.getName() + " SGPA for semester " + s.getSemester() + " is " + sgpa);
        }
    }
    public void change_sem(){
        for(Student s : student_list){
            int sem = s.getSemester();
            sem = sem + (int)(s.getCompleted_courses().size()/5);
        }
    }

    public float floatgrade(String grade) {
        return switch (grade) {
            case "A+", "A" -> 10;
            case "B" -> 9;
            case "C" -> 8;
            case "D" -> 7;
            case "E" -> 6;
            case "F", "W" -> 0;
            default -> 0;
        };
    }
}
