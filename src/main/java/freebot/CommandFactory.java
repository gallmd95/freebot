package freebot;

import java.util.*;

class CommandFactory {

    private final Map<CommandId, Command> commands;

    enum CommandId {
        CLEAR, HELP, START, REMOVE, LIST
    }

    CommandFactory(SessionManager sessionManager) {
        commands = new HashMap<>() {
            {
                put(CommandId.CLEAR, new ClearCommand());
                put(CommandId.HELP, new HelpCommand(sessionManager));
                put(CommandId.START, new StartCommand(sessionManager));
                put(CommandId.LIST, new ListCommand(sessionManager));
                put(CommandId.REMOVE, new RemoveCommand(sessionManager));
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
