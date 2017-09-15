package commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.STATIC;

import java.awt.*;
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
        PrivateChannel privch = author.openPrivateChannel().complete();
        channel.sendTyping().queue();
        privch.sendTyping().queue();
        message.delete().queue();


        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.CYAN)
                .setDescription("SchlaubiBot Commands")
                .setFooter("SchlaubiBot made by Schlaubi", "https://drschlaubi.de/wp-content/uploads/2017/04/cropped-Schlaubi.png")
                .setAuthor("Bot-Help", "http://derschlaubi.xyz", "https://drschlaubi.de/wp-content/uploads/2017/04/cropped-Schlaubi.png")
                .addField("__**NOTICE**__","Some commands need Arguments `<>`are important arguments and `[]`are optional \nIf you found any bug feel free to contact schlaubi (http://derschlaubi.xyz)", true)
                .addField("Tools", "`" + STATIC.prefix + "short <url>`  (URL Shortner) \n`" + STATIC.prefix + "roles`  (Shows you the Server Roles and IDs)\n`" + STATIC.prefix + "userid <@User>`  (Gets the user's id) \n`" + STATIC.prefix + "serverinfo (Provied some information about the server\n`" + STATIC.prefix + "userinfo [@User] (Provides some user infomation)", false)
                .addField("Fun", "`" + STATIC.prefix + "kys <name>`  (Sends a hangman) \n`" + STATIC.prefix + "rip <name> <line>`  (Make your own Tombstone) \n`" + STATIC.prefix + "medal <name> <text>`  (Creates your own medal) \n`" + STATIC.prefix + "lmgtfy <search query>`  (Generates an help link for people who don't check how to use google) \n`" + STATIC.prefix + "kms` (Kill myself)\"", false)
                .addField("Moderation", "`" + STATIC.prefix + "clear <count>`  (clears messages)\n`" + STATIC.prefix + "kick <@User>` (Kicks a user)\n`" + STATIC.prefix + "ban <@User>`  (Bans a user) \n`" + STATIC.prefix + "joinmessages` (Enables/Disables joinmessages on your server) \n`" + STATIC.prefix + "mute/unmute <@User>` (Mutes/Unmutes a user)", false)
                .addField("Botinfo", "`" + STATIC.prefix + "ping` (Sends the actual ping of the Bot to Discord) \n`" + STATIC.prefix + "version`(Sends Bot's version info) \n`" + STATIC.prefix + "servers`(Specifies a list of all servers where the bot is running) ", false);

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
