import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Professor_list {
    private ArrayList<Professor> professorList;

    public Professor_list() {
        this.professorList = new ArrayList<>();
    }

    public ArrayList<Professor> getProfessor_list() {
        return this.professorList;
    }

    public void setProfessorList(ArrayList<Professor> professorList) {
        this.professorList = professorList;
    }

    public Professor findProfessorByEmail(String email) {
        for (Professor professor : professorList) {
            if (professor.getEmail().equalsIgnoreCase(email)) {
                return professor;
            }
        }
        return null;
    }

    public void addProfessor() {
        String name = JOptionPane.showInputDialog("Enter professor name:");
        if(name == null) {return;}
        String email = JOptionPane.showInputDialog("Enter professor email:");
        if(email == null) {return;}
        Professor p = findProfessorByEmail(email);

        if (p == null) {
            String password = JOptionPane.showInputDialog("Enter Password:");
            if(password == null) {return;}
            String department = JOptionPane.showInputDialog("Enter office hours of professor (Day | Timing(Start-End) | Location):");
            if(department == null) {return;}
            Professor newProfessor = new Professor(name, email, password, department);
            professorList.add(newProfessor);
            JOptionPane.showMessageDialog(null, "Professor " + name + " added successfully.");
        } else {
            JOptionPane.showMessageDialog(null, "Professor already exists.");
        }
    }

    public void viewProfessors() {
        if (this.professorList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No professors available.");
        } else {
            StringBuilder output = new StringBuilder();
            Iterator<Professor> iterator = this.professorList.iterator();
            while (iterator.hasNext()) {
                Professor p = iterator.next();
                output.append("Professor name: ").append(p.getName())
                        .append(", email: ").append(p.getEmail())
                        .append(", courses: ").append(p.coursenamelist()).append("\n");
            }
            JOptionPane.showMessageDialog(null, output.toString());
        }
    }

    public void updateProfessor() {
        String email = JOptionPane.showInputDialog("Enter professor email:");
        Professor p = findProfessorByEmail(email);
        if (p == null) {
            JOptionPane.showMessageDialog(null, "Professor does not exist.");
        } else {
            JOptionPane.showMessageDialog(null, "Professor name: " + p.getName() + ", email: " + p.getEmail());

            String[] options = {"Change Name", "Change Email", "Change Password", "Exit"};
            boolean exit = false;
            while (!exit) {
                int x = JOptionPane.showOptionDialog(null, "Select an option to update", "Update Professor",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                        null, options, options[0]);
                switch (x) {
                    case 0:
                        String newName = JOptionPane.showInputDialog("Enter professor new name:");
                        if (newName == null) {return;}
                        p.setName(newName);
                        JOptionPane.showMessageDialog(null, "Name updated successfully.");
                        break;
                    case 1:
                        String newEmail = JOptionPane.showInputDialog("Enter professor new email:");
                        if (newEmail == null) {return;}
                        p.setEmail(newEmail);
                        JOptionPane.showMessageDialog(null, "Email updated successfully.");
                        break;
                    case 2:
                        String newPassword = JOptionPane.showInputDialog("Enter professor new password:");
                        if (newPassword == null) {return;}
                        p.setPassword(newPassword);
                        JOptionPane.showMessageDialog(null, "Password updated successfully.");
                        break;
                    case 3:
                        exit = true;
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid input. Please try again.");
                        break;
                }
            }
        }
    }

    public void deleteProfessor() {
        String email = JOptionPane.showInputDialog("Enter professor email:");
        if (email == null) {return;}
        Professor p = findProfessorByEmail(email);
        if (p == null) {
            JOptionPane.showMessageDialog(null, "Professor does not exist.");
        } else {
            professorList.remove(p);
            JOptionPane.showMessageDialog(null, "Professor " + p.getName() + " successfully deleted.");
        }
    }

    public void assign_prof_to_course(Course course) {
        String email = JOptionPane.showInputDialog("Enter email of professor:");
        if (email == null) {return;}
        Professor p = findProfessorByEmail(email);
        if (p == null) {
            JOptionPane.showMessageDialog(null, "Professor does not exist.");
            return;
        }
        course.setProff(p);
        p.getCourses().add(course);
        JOptionPane.showMessageDialog(null, "Professor " + p.getName() + " successfully assigned to the course.");
    }
}
