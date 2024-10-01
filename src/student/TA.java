package student;


import admin.*;
import course.Course;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TA extends Student{

    private ArrayList<Course> Ta_courses;

    public TA(String name, String email, String password, ArrayList<StudentCourse> courses, Course_catalog course_catalog, ArrayList<StudentCourse> completedCourses, HashMap<Integer, Float> sgpa, int studentSemester, int credit, int semester, boolean b) {
        super(name, email, password, course_catalog, semester);
        this.setTA(true);
        this.Ta_courses = new ArrayList<>();
    }
    public void add_course(Course course) {
        Ta_courses.add(course);
    }
    public void remove_course(Course course) {
        Ta_courses.remove(course);
    }

    ArrayList<String> get_courses(){
        ArrayList<String> courses = new ArrayList<>();
        for(Course c : Ta_courses){
            courses.add(c.getCode());
        }
        return courses;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getEmail().equals(((TA)obj).getEmail());
    }
    @Override
    public String toString() {
        return "Name: " + this.getName() + " " + "Email :" +this.getEmail() + "\n" + "Codes of Assigned courses: " + this.getCourses() ;
    }
    public void view_ta_courses() {
        if (Ta_courses == null || Ta_courses.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No TA courses assigned.");
            return;
        }

        StringBuilder courseinfo = new StringBuilder("Your TAship courses are:\n");
        for (Course c : Ta_courses) {
            courseinfo.append(c.getTitle()).append(" : ")
                    .append(c.getCode()).append(" : ")
                    .append(c.getProff().getName()).append("\n");
        }
        JOptionPane.showMessageDialog(null, courseinfo.toString());
    }

    public void  grade_student(Administrator admin){
        admin.getStudentList().assign_grade();
    }
    public void view_student_progress(Administrator admin) {
        String username = JOptionPane.showInputDialog("Enter the username");
        if(username == null){return;}
        Student s = admin.getStudentList().find_student_by_email(username);
        s.view_grade();
    }
}
