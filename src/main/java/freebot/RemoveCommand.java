package freebot;

import javax.swing.*;
import java.io.File;
import java.util.Iterator;

public class RemoveCommand implements Command{
    private final SessionManager sessionManager;

    RemoveCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void run(String[] inputs, JTextArea ta) {
        if(sessionManager.removeSession(inputs[1])){
            ta.append("\n>");
        } else {
            throw new RuntimeException("Session could not be removed.");
        }
    }

    @Override
    public String description() {
        return "REMOVE <NAME>: Removes session with name NAME.";
    }
}
