package commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.STATIC;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class commandHelp implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, final MessageReceivedEvent event) {

        MessageChannel channel = event.getChannel();
        User author = event.getAuthor();
        Message message = event.getMessage();
        PrivateChannel privch = (PrivateChannel)author.openPrivateChannel().complete();
        channel.sendTyping().queue();
        privch.sendTyping().queue();
        message.delete().queue();


        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.CYAN)
                .setDescription("SchlaubiBot Commands")
                .setFooter("SchlaubiBot made by Schlaubi", "https://drschlaubi.de/wp-content/uploads/2017/04/cropped-Schlaubi.png")
                .setAuthor("Bot-Help", "http://derschlaubi.xyz", "https://drschlaubi.de/wp-content/uploads/2017/04/cropped-Schlaubi.png")
                .addField("__**NOTICE**__","Some commands need Arguments `<>`are important arguments and `[]`are optional \n If you found any bug feel free to contact schlaubi (http://derschlaubi.xyz)", true)
                .addBlankField(true)
                .addBlankField(true)
                .addField("Tools", "`" + STATIC.prefix + "short <url>`  (URL Sortner) \n`" + STATIC.prefix + "roles`  (Shows you the Server Roles and IDs)\n`" + STATIC.prefix + "userid <@User>`  (Get the user's id)", true)
                .addBlankField(true)
                .addField("Fun", "`" + STATIC.prefix + "kys <name>`  (Sends an hangman) \n`" + STATIC.prefix + "rip <name> <line>`  (Make your own gravestone) \n`" + STATIC.prefix + "medal <name> <text>`  (Create your own medal) \n `" + STATIC.prefix + "lmgtfy <search query>`  (Generates an help link for people who don't check how to use google) ", true)
                .addBlankField(true)
                .addField("Moderation", "`" + STATIC.prefix + "clear <count>`  (clears messages)\n`" + STATIC.prefix + "kick <@User>` (Kick a user)\n`" + STATIC.prefix + "ban <@User>`  (Ban an user) ", true);
        privch.sendMessage(embed.build()).queue();

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        System.out.println("[INFO]Command '" + STATIC.prefix + "help' was executed by " + event.getAuthor().getName());

    }

    @Override
    public String help() {
        return null;
    }
}
