package net.Schlaubi.SchlaubiBot.core;

import net.Schlaubi.SchlaubiBot.commands.Command;

import java.util.HashMap;

public class commandHandler {

    public static final commandParser parser = new commandParser();
    public static HashMap<String, Command> commands = new HashMap<>();

    public static void handleCommand(commandParser.commandContainer cmd) {

        if (commands.containsKey(cmd.invoke.toLowerCase())) {

            boolean safe = commands.get(cmd.invoke.toLowerCase()).called(cmd.args, cmd.event);

            if (!safe) {
                commands.get(cmd.invoke.toLowerCase()).action(cmd.args, cmd.event);
                commands.get(cmd.invoke.toLowerCase()).executed(safe, cmd.event);
            } else {
                commands.get(cmd.invoke.toLowerCase()).executed(safe, cmd.event);
            }

        }

    }

}