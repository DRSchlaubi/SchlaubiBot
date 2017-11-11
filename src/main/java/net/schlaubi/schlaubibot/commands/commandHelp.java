package net.schlaubi.schlaubibot.commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.core.commandHandler;
import net.schlaubi.schlaubibot.core.permissionHandler;
import net.schlaubi.schlaubibot.util.STATIC;
import net.schlaubi.schlaubibot.util.commandLogger;

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
        Guild guild = event.getGuild();
        String prefix = STATIC.prefix;
        PrivateChannel privch = author.openPrivateChannel().complete();
        channel.sendTyping().queue();
        privch.sendTyping().queue();
        message.delete().queue();


        /*EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.CYAN)
                .setDescription("SchlaubiBot Commands")
                .setFooter("SchlaubiBot made by Schlaubi", "https://drschlaubi.de/wp-content/uploads/2017/04/cropped-Schlaubi.png")
                .setAuthor("Bot-Help", "http://derschlaubi.xyz", "https://drschlaubi.de/wp-content/uploads/2017/04/cropped-Schlaubi.png")
                .addField("__**NOTICE**__","Some commands need Arguments `<>`are important arguments and `[]`are optional \nIf you found any bug feel free to contact schlaubi (http://derschlaubi.xyz) or just use the ```" + prefix + "bug <report>``` command" , true)
                .addField("Tools", "`" + prefix + "short <url>` - (URL Shortner) \n`" + prefix + "roles` - (Shows you the Server Roles and IDs)\n`" + prefix + "userid <@User>` - (Gets the user's id) \n`" + prefix + "serverinfo` -  (Provied some information about the server\n`" + prefix + "userinfo [@User]` -(Provides some user infomation)\n `" + prefix + "addrole <@User> <role>`(Assigns a role to a user\n `" + prefix + "removerole <@User> <role> (Removes a role from a member)", false)
                .addField("Fun", "`" + prefix +"kys <name>` - (Sends a hangman) \n`" + prefix + "rip <name> <line>` - (Make your own Tombstone) \n`" + prefix + "medal <name> <text>` - (Creates your own medal) \n`" + prefix + "lmgtfy <search query>` - (Generates an help link for people who don't check how to use google) \n`" + prefix + "kms` - (Kill myself)\n ```" + prefix + "(find your favourite gif)", false)
                .addField("Moderation", "`" + prefix + "clear <count>` - (clears messages)\n`" + prefix + "kick <@User>` - (Kicks a user)\n`" + prefix + "ban <@User>` - (Bans a user) \n`" + prefix + "mute/unmute <@User>` - (Mutes/Unmutes a user)", false)
                .addField("Botinfo", "`" + prefix + "ping` - (Sends the actual ping of the Bot to Discord) \n`" + prefix + "version` - (Sends Bot's version info) \n`" + prefix + "servers` - (Specifies a list of all servers where the bot is running) ", false)
                .addField("Music",  "`  " + prefix + "music play <link/keyword)  `  -  Queues a song\n`  " + prefix + "music stop  `  -  Stops the queue\n`  " + prefix + "music now  `  -  Shows information about the current song\n`  " + prefix + "music pause/resume  `  -  Pauses/resumes a song\n`  " + prefix + "music volume <volume> ` - Changes the volume of the bot \n`" + prefix + "shuffle` (Shuffles the queue) \n`" + prefix + "music disconnect` - (Let the Bot disconnect)\n`  " + prefix + "music skip [number of songs]` - (Skips the specified amount of songs)\n`  " + prefix + "music queue` - (Shows the queue)", false)
                .addField("Polls",  "`  " + prefix + "vote create <Title>|<Option1>|<Option2>|...  `  -  create a vote\n`  " + prefix + "vote vote <index of Option>  `  -  vote for a possibility\n`  " + prefix + "vote stats  `  -  get stats of a current vote\n`  " + prefix + "vote close  `  -  close a current vote", false)
                .addField("Server settings", "`" + prefix + "settings log  toggle`  -  Toggles command logger\n`" + prefix + "settings log channel  <#Channel>`  -  Sets log channel\n`" + prefix + "settings joinmessages toggle`  -  Toggles joinmesasges\n`" + prefix + "settings joinmessages toggle`  -  Toggles joinmesasges\n`" + prefix + "settings joinmessages leave `<Godbye %user%> -  Sets leave message\n`" + prefix + "settings joinmessages channel <#Channel>` - Sets message channel\n`" + prefix + "settings prefix <prefix/reset>`  -  Sets the server's prefix" , false);
        privch.sendMessage(embed.build()).queue();*/

        StringBuilder tools = new StringBuilder();
        StringBuilder fun = new StringBuilder();
        StringBuilder moderation = new StringBuilder();
        StringBuilder music = new StringBuilder();
        StringBuilder polls = new StringBuilder();
        StringBuilder serversettings = new StringBuilder();
        StringBuilder botinfo = new StringBuilder();

        commandHandler.commandlist.forEach(c -> {
            if(c.category().equals(CommandCategory.BOTINFO)){
                botinfo.append(permissionHandler.getPermissonIcon(c.permissionlevel())).append("`" + c.usage() + "`").append(" - ").append(c.description()).append("\n");
            } else if (c.category().equals(CommandCategory.FUN)){
                fun.append(permissionHandler.getPermissonIcon(c.permissionlevel())).append("`" + c.usage() + "`").append(" - ").append(c.description()).append("\n");
            } else if (c.category().equals(CommandCategory.MODERATION)){
                moderation.append(permissionHandler.getPermissonIcon(c.permissionlevel())).append("`" + c.usage() + "`").append(" - ").append(c.description()).append("\n");
            } else if (c.category().equals(CommandCategory.TOOLS)){
                tools.append(permissionHandler.getPermissonIcon(c.permissionlevel())).append("`" + c.usage() + "`").append(" - ").append(c.description()).append("\n");
            } else if (c.category().equals(CommandCategory.MUSIC)){
                music.append(c.help().replace("USAGE: ", ""));
            } else if (c.category().equals(CommandCategory.POLLS)){
                polls.append(c.help().replace("USAGE: ", ""));
            } else if (c.category().equals(CommandCategory.SERVER_SETTINGS)){
                serversettings.append(c.help().replace("USAGE: ", ""));
            }
        });
        EmbedBuilder help = new EmbedBuilder();
        help.setColor(Color.CYAN);
        help.setDescription("SchlaubiBot Commands");
        help.setFooter("SchlaubiBot made by Schlaubi", "https://drschlaubi.de/wp-content/uploads/2017/04/cropped-Schlaubi.png");
        help.setAuthor("Bot-Help", "http://derschlaubi.xyz", "https://drschlaubi.de/wp-content/uploads/2017/04/cropped-Schlaubi.png");
        help.addField("__**NOTICE**__","Some commands need Arguments `<>`are important arguments and `[]`are optional \nIf you found any bug feel free to contact schlaubi (http://derschlaubi.xyz) or just use the ```" + prefix + "bug <report>``` command" , true);
        help.addField("Permissions:", ":white_medium_small_square: For everyone \n:small_blue_diamond: For moderators \n :large_blue_diamond: For admins \n :small_red_triangle: Bot owner only", false);
        help.addField("Tools", tools.toString(), false);
        help.addField("fun", fun.toString(), false);
        help.addField("Moderation", moderation.toString(), false);
        help.addField("Music", music.toString(), false);
        help.addField("Polls", polls.toString(), false);
        help.addField("Server settings", serversettings.toString(),false);
        help.addField("Botinfo", botinfo.toString(), false);

        privch.sendMessage(help.build()).queue();




    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("help", event);

    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public String usage() {
        return null;
    }

    @Override
    public CommandCategory category() {
        return CommandCategory.HIDDEN;
    }

    @Override
    public int permissionlevel() {
        return 0;
    }
}
