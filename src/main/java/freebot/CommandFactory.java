package freebot;

import java.util.*;

class CommandFactory {

    private final Map<CommandId, Command> commands;

    enum CommandId {
        CLEAR, HELP, START
    }

    CommandFactory() {
        commands = new HashMap<>() {
            {
                put(CommandId.CLEAR, new ClearCommand());
                put(CommandId.HELP, new HelpCommand());
                put(CommandId.START, new StartCommand());
            }
        };
    }


    Command makeCommand(String commandString){
        try {
            final CommandId command = CommandId.valueOf(commandString);
            if(commands.containsKey(command)) {
                return commands.get(command);
            } else {
                throw new RuntimeException("Command not implemented yet: " + commandString);
            }
        } catch (IllegalArgumentException ex){
            return new NotSupportedCommand();
        }
    }
     Collection<Command> getCommands(){
        return commands.values();
    }
}
