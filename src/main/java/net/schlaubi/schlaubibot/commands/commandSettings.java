package net.schlaubi.schlaubibot.commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.core.permissionHandler;
import net.schlaubi.schlaubibot.util.MySQL;
import net.schlaubi.schlaubibot.util.STATIC;
import net.schlaubi.schlaubibot.util.commandLogger;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;

public class commandSettings implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        User author = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        Guild guild = event.getGuild();
        String prefix = MySQL.getValue(guild, "prefix");
        message.delete().queue();
        channel.sendTyping().queue();


        if (!(args.length > 0)) {
            embedSender.sendEmbed(help(), channel, Color.red);
            return;
        }
        if(!MySQL.ifGuildExists(guild))
            MySQL.createServer(guild);

        switch (args[0].toLowerCase()) {
            case "info":
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Schlaubibot Settings")
                        .setColor(new Color(0x18BAC0))
                        .setFooter("(c) Schlaubi 2017 | SchlaubiBot", "https://cdn.discordapp.com/avatars/264048760580079616/4306cb8bcf063c3cbfa4998fc40080ec.png");
                if (!MySQL.getValue(guild, "logchannel").equals("0")) {
                    embed.addField(":vhs: Log channel", "`" + guild.getTextChannelById(MySQL.getValue(guild, "logchannel")).getName() + "`", false);
                } else {
                    embed.addField(":vhs: Log channel", "`disabled`", false);
                }
                if (!MySQL.getValue(guild, "joinmessages").equals("0")) {
                    embed.addField(":wave: Join messages", "Joinmessage:  `" + MySQL.getValue(guild, "joinmessage") + "` (" + guild.getTextChannelById(MySQL.getValue(guild, "joinmessagechannel")).getAsMention() + ")\n Leave message: `" + MySQL.getValue(guild, "leavemessage") + "` (" + guild.getTextChannelById(MySQL.getValue(guild, "joinmessagechannel")).getAsMention() + ")", false);
                } else {
                    embed.addField(":wave: Join messages", "`disabled`", false);
                }
                embed.addField(":exclamation: Prefix", "`" + MySQL.getValue(guild, "prefix") + "`", false);
                channel.sendMessage(embed.build()).queue();
                break;
            case "log":
                if (!(args.length > 1)) {
                    embedSender.sendEmbed(logHelp(), channel, Color.red);
                    return;
                }

                switch (args[1].toLowerCase()) {
                    case "toggle":
                        if (!MySQL.getValue(guild, "logchannel").equals("0")) {
                            MySQL.updateValue(guild, "logchannel", guild.getDefaultChannel().getId());
                            embedSender.sendEmbed(":white_check_mark: Successfully enabled command logger", channel, Color.green);
                        } else {
                            MySQL.updateValue(guild, "logchannel", "0");
                            embedSender.sendEmbed(":white_check_mark: Successfully disabled command logger", channel, Color.green);

                        }
                        break;
                    case "channel":
                        if (!(message.getMentionedChannels().size() > 0)) {
                            embedSender.sendEmbed("Usage : `" + prefix + "joinmessages channel <#Channel>`", channel, Color.red);
                            return;
                        }
                        MySQL.updateValue(guild, "logchannel", message.getMentionedChannels().get(0).getId());
                        embedSender.sendEmbed(":white_check_mark: Succesfully set logchannel to " + message.getMentionedChannels().get(0).getAsMention(), channel, Color.green);

                }
                break;
            case "joinmessages":
                if (!(args.length > 1)) {
                    embedSender.sendEmbed(joinmessagesHelp(), channel, Color.red);
                    return;
                }
                String joinmessage = MySQL.getValue(guild, "joinmessage");
                String leavemessage = MySQL.getValue(guild, "leavemessage");
                switch (args[1].toLowerCase()){
                    case "toggle":

                        String enabled = MySQL.getValue(guild, "joinmessages");

                        if(enabled.equals("1")){
                            MySQL.updateValue(guild, "joinmessages", "0");
                            embedSender.sendEmbed(":white_check_mark: Succesfully disabled joinmessages!", channel, Color.green);
                        } else if(enabled.equals("0")){
                            MySQL.updateValue(guild, "joinmessages", "1");
                            embedSender.sendEmbed(":white_check_mark: Succesfully enabled joinmessages!", channel, Color.green);
                        }
                        break;

                    case "channel":
                        if(!(message.getMentionedChannels().size() > 0)){
                            embedSender.sendEmbed("Usage : `" + prefix + "joinmessages channel <#Channel>`", channel, Color.red);
                            return;
                        }

                        String channelid = message.getMentionedChannels().get(0).getId();
                        MySQL.updateValue(guild,"joinmessagechannel", channelid);
                        embedSender.sendEmbed(":white_check_mark: Succesfully set joinmessagechannel to " + message.getMentionedChannels().get(0).getAsMention(), channel, Color.green);
                        break;

                    case "join":
                        if(!(args.length > 0)){
                            embedSender.sendEmbed("Usage : `" + prefix + "joinmessages join <Hello %user% on %guild%>`", channel, Color.red);
                            return;
                        }

                        for(int i = 2; i < args.length; i++){
                            joinmessage += " " + args[i];
                        }
                        MySQL.updateValue(guild, "joinmessage", joinmessage.replaceFirst("null ", ""));
                        embedSender.sendEmbed(":white_check_mark:  Successfully set joinmessage to `" + joinmessage.replaceFirst("null ", "") + "`!", channel, Color.green);
                        break;
                    case "leave":
                        if(!(args.length > 0)){
                            embedSender.sendEmbed("Usage : `" + prefix + "joinmessages leave <Bye %user%>`", channel, Color.red);
                            return;
                        }

                        for(int i = 2; i < args.length; i++){
                            leavemessage += " " + args[i];
                        }
                        MySQL.updateValue(guild, "leavemessage", leavemessage.replaceFirst("null ", ""));
                        embedSender.sendEmbed(":white_check_mark:  Successfully set leavemessage to `" + leavemessage.replaceFirst("null ", "") + "`!", channel, Color.green);

                }
                break;
            case "prefix":
                if (!(args.length > 1)) {
                    embedSender.sendEmbed(prefixHelp(), channel, Color.red);
                    return;
                }
                if(args[1].equals("reset")){
                    MySQL.updateValue(guild, "prefix", prefix);
                    embedSender.sendEmbed(":repeat: Successfully restored default prefix", channel, Color.green);
                    return;
                }

                if(args[1].length() > 2){
                    embedSender.sendEmbed(":warning: Your prefix can't be longer than 2 chars", channel, Color.red);
                    return;
                }

                MySQL.updateValue(guild, "prefix", args[1]);
                embedSender.sendEmbed(":white_check_mark: Successfully set prefix to `" + args[1] + "` !", channel, Color.green);


        }


    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("settings", event);

    }

    @Override
    public String help() {
        return "USAGE: \n" +
                "`  " + STATIC.prefix + "settings log  toggle`  -  Toggles command logger\n" +
                "`  " + STATIC.prefix + "settings log channel  <#Channel>`  -  Sets log channel\n" +
                "`  " + STATIC.prefix + "settings joinmessages toggle`  -  Toggles joinmesasges\n" +
                "`  " + STATIC.prefix + "settings joinmessages toggle`  -  Toggles joinmesasges\n" +
                "`  " + STATIC.prefix + "settings joinmessages leave `<Godbye %user%> -  Sets leave message\n" +
                "`  " + STATIC.prefix + "settings joinmessages channel <#Channel>` - Sets message channel\n" +
                "`  " + STATIC.prefix + "settings prefix <prefix/reset>`  -  Sets the server's prefix"
                ;
    }

    @Override
    public String description() {
        return "Server settings for your own server";
    }

    @Override
    public String usage() {
        return "::settings <command> <subcommand> <argument>";
    }

    @Override
    public CommandCategory category() {
        return CommandCategory.SERVER_SETTINGS;
    }

    @Override
    public int permissionlevel() {
        return 0;
    }

    public String logHelp() {
        return "USAGE: \n" +
                "`  " + STATIC.prefix + "settings log  toggle`  -  Toggles command logger\n" +
                "`  " + STATIC.prefix + "settings log channel  <#Channel>`  -  Sets log channel\n"
                ;
    }

    public String joinmessagesHelp() {
        return  "`  " + STATIC.prefix + "settings joinmessages toggle`  -  Toggles joinmesasges\n" +
                "`  " + STATIC.prefix + "settings joinmessages join  <Hello %user% on %guild%>`  -  Sets join message\n" +
                "`  " + STATIC.prefix + "settings joinmessages leave `<Godbye %user%> -  Sets leave message\n" +
                "`  " + STATIC.prefix + "settings joinmessages channel <#Channel>` - Sets message channel"
                ;
    }

    public String prefixHelp() {
        return  "`  " + STATIC.prefix + "settings prefix <prefix/reset>`  -  Sets the server's prefix"
                ;
    }
}
