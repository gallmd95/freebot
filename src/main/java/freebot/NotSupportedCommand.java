package freebot;

import javax.swing.*;

public class NotSupportedCommand implements Command {
    public void run(String[] inputs, JTextArea ta) {
        ta.append("\nCommand not supported: " + inputs[0] + "\n>");
    }

    @Override
    public String description() {
        return null;
    }
}
