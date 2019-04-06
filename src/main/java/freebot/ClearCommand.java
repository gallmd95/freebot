package freebot;

import javax.swing.*;

public class ClearCommand implements Command {
    public void run(String[] inputs, JTextArea ta) {
        ta.setText(">");
    }

    @Override
    public String description() {
        return "CLEAR: clears the console";
    }
}
