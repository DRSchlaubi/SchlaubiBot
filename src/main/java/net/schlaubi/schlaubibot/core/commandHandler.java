package net.schlaubi.schlaubibot.core;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.commands.Command;
import net.schlaubi.schlaubibot.util.MySQL;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class commandHandler {

    public static final commandParser parser = new commandParser();
    public static HashMap<String, Command> commands = new HashMap<>();
    public static ArrayList<Command> commandlist = new ArrayList<>();

    public static void handleCommand(commandParser.commandContainer cmd, MessageReceivedEvent event) {

        if (commands.containsKey(cmd.invoke.toLowerCase())) {
            boolean safe = commands.get(cmd.invoke.toLowerCase()).called(cmd.args, cmd.event);
            int permlevel = MySQL.getUserPermissionLevel(event.getAuthor());
            if(commands.get(cmd.invoke.toLowerCase()).permissionlevel() > permlevel){
                event.getMessage().delete().queue();
                embedSender.sendEmbed(":warning: You have no permission to perform that command! \n Needed permission level: `" + commands.get(cmd.invoke.toLowerCase()).permissionlevel() + "` \nYour permission level: `" + permlevel + "`", event.getChannel(), Color.red);
                return;
            }
            if (!safe) {
                commands.get(cmd.invoke.toLowerCase()).action(cmd.args, cmd.event);
                commands.get(cmd.invoke.toLowerCase()).executed(safe, cmd.event);
            } else {
                commands.get(cmd.invoke.toLowerCase()).executed(safe, cmd.event);
            }

        }

    }
    public static void registerCommand(String invoke, Command command){
        commands.put(invoke, command);
        commandlist.add(command);
    }

}