import javax.swing.JOptionPane;

public class infinite_input {
    protected static int loop_input() {
        boolean flag = true;
        int ans = 0;

        while (flag) {
            try {
                String input = JOptionPane.showInputDialog(null, "Enter an integer (or type 'exit' to cancel):");

                if (input == null || input.equalsIgnoreCase("exit")) {
                    JOptionPane.showMessageDialog(null, "Input cancelled.");
                    return -1;
                }

                ans = Integer.parseInt(input);
                flag = false;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer.");
            }
        }
        return ans;
    }
}
