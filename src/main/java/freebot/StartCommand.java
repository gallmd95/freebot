package freebot;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Arrays;

public class StartCommand implements Command {
    private final SessionManager sessionManager;
    private final String windowText = "Started new session: %s\nToggle PAUSE with SPACE\n%s";

    StartCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void run(String[] inputs, JTextArea ta) {
        KeyListener listener = ta.getKeyListeners()[0];
        Arrays.stream(ta.getKeyListeners()).forEach(ta::removeKeyListener);
        if(inputs.length > 1){
            try {
                if(sessionManager.startSession(inputs[1],ta.getText())){
                    ta.setText(String.format(windowText, inputs[1], "Session is playing..."));
                    ta.addKeyListener(new KeyAdapter() {
                        public void keyPressed(KeyEvent e) {
                            if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                                ta.setText(sessionManager.getConsoleText()+"\n>");
                                Arrays.stream(ta.getKeyListeners()).forEach(ta::removeKeyListener);
                                ta.addKeyListener(listener);
                                sessionManager.endSession();
                            } else if (e.getKeyCode()==32){
                                if (!sessionManager.isPaused()) {
                                    ta.setText(String.format(windowText, inputs[1], "Session is paused."));
                                    sessionManager.pauseSession();
                                } else {
                                    ta.setText(String.format(windowText, inputs[1], "Session is playing..."));
                                    sessionManager.unPauseSession();
                                }
                            }
                        }
                    });

                } else {
                    ta.append("\nUnable to start session. There is already a session with name: "+inputs[1]+"\n>");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else {
            ta.append("\nMissing session name. Try START <NAME>\n>");
        }
    }

    @Override
    public String description() {
        return "START <NAME>: starts a new session with specified name";
    }
}
