package freebot;

import javax.swing.*;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ListCommand implements Command {
    private final SessionManager sessionManager;

    ListCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void run(String[] inputs, JTextArea ta) {
        final Set<Session> sessionSet = sessionManager.getSessions();
        final String sessionsString =  sessionSet
                .stream()
                .map((s)->s.getName()+" "+ formatTime(s.getElapsedTime()))
                .collect(Collectors.joining("\n"));
        ta.append(sessionSet.isEmpty() ?"\nNo available sessions.\n>" : "\n"+sessionsString+"\n>");
    }

    @Override
    public String description() {
        return "LIST: Lists available sessions.";
    }

    private String formatTime(long time){
        return String.format("%d:%02d",
                TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time) -
                        TimeUnit.MINUTES.toMinutes(TimeUnit.MILLISECONDS.toHours(time))
        );
    }
}
