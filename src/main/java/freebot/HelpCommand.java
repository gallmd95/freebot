package freebot;

import javax.swing.*;
import java.util.stream.Collectors;

public class HelpCommand implements Command {
    public void run(String[] inputs, JTextArea ta) {
        final String commandsString = new CommandFactory().getCommands().stream().map(Command::description).collect(Collectors.joining("\n"));
        ta.append("\n" + commandsString+"\n>");
    }

    @Override
    public String description() {
        return "HELP: prints supported commands' descriptions";
    }
}
