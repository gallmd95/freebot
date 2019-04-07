package freebot;

import javax.swing.*;

public interface Command {


    void run(String[] inputs, JTextArea ta);

    String description();
}
