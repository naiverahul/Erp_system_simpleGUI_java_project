package helper;

import javax.swing.JOptionPane;
import exceptions.InvalidLoginExcepiton;

public abstract class User {
    private String name;
    private String email;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public boolean login() throws InvalidLoginExcepiton {
        boolean flag = true;
        int attemptsLeft = 4;

        while (flag) {
            String inputEmail = JOptionPane.showInputDialog("Enter your email to login:");
            if (inputEmail == null) {
                return false;
            }

            String inputPassword = JOptionPane.showInputDialog("Enter your password:");
            if (inputPassword == null) {
                return false;
            }

            if (inputEmail.equals(this.email) && inputPassword.equals(this.password)) {
                return true;  // Successful login
            }

            // Decrease attempts and notify user
            if (attemptsLeft > 0) {
                JOptionPane.showMessageDialog(null, "Incorrect email or password. You have " + attemptsLeft + " attempts left. Please try again.");
                --attemptsLeft;
            } else {
                flag = false;
                // Exceeded attempts, throw InvalidLoginException
                throw new InvalidLoginExcepiton("Exceeded maximum login attempts. Contact Rahul.");
            }
        }

        return false;
    }

    abstract public void changepass() ;
}
