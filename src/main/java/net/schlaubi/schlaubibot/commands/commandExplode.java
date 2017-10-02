package net.schlaubi.schlaubibot.commands;

import net.schlaubi.schlaubibot.core.permissionHandler;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.util.MySQL;
import net.schlaubi.schlaubibot.util.commandLogger;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class commandExplode implements Command {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(final String[] args, final MessageReceivedEvent event) {
        User author = event.getAuthor();
        final MessageChannel channel = event.getChannel();
        final Message message = event.getMessage();
        Guild guild = event.getGuild();
        String prefix = MySQL.getValue(guild, "prefix");
        channel.sendTyping().queue();
        message.delete().queue();
        if(args.length > 0){
            if(permissionHandler.check(event)){
                if(permissionHandler.check(event)){

                    embedSender.sendEmbed("Sorry, " + author.getAsMention() + " but you don't have the permission to perform that command!", channel, Color.red);
                    return;
                }
            }
                    event.getMessage().delete().queue();
                    final Message mymsg = channel.sendMessage("Message `" + args[0] + "` will explode in `5` secconds").complete();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mymsg.editMessage("Message `" + args[0] + "` will explode in `4` secconds").complete();
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    mymsg.editMessage("Message `" + args[0] + "` will explode in `3` secconds").complete();
                                    new Timer().schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            mymsg.editMessage("Message `" + args[0] + "` will explode in `2` secconds").complete();
                                            new Timer().schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    mymsg.editMessage("Message `" + args[0] + "` will explode in `1` seccond").complete(); //second als Einzahl, sind ja net mehr geworden XD
                                                    new Timer().schedule(new TimerTask() {
                                                        @Override
                                                        public void run() {
                                                            mymsg.editMessage(":boom: :boom: :boom: EXPLOSION :boom: :boom: :boom: ").complete();
                                                            message.delete().queue();
                                                            mymsg.delete().queue();
                                                        }
                                                    }, 1000);
                                                }
                                            }, 1000);
                                        }
                                    }, 1000);
                                }
                            }, 1000);
                        }
                    }, 1000);

        } else {
            final Message mymsg = channel.sendMessage("This message will explode in `5` secconds").complete();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    event.getMessage().delete().queue();
                    mymsg.editMessage("This message will explode in `4` secconds").complete();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mymsg.editMessage("This message will explode in `3` secconds").complete();
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    mymsg.editMessage("This message will explode in `2` secconds").complete();
                                    new Timer().schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            mymsg.editMessage("This message will explode in `1` seccond").complete();
                                            new Timer().schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    mymsg.editMessage(":boom: :boom: :boom: EXPLOSION :boom: :boom: :boom: ").complete();
                                                    mymsg.delete().queue();
                                                }
                                            },1000);
                                        }
                                    }, 1000);
                                }
                            }, 1000);
                        }
                    }, 1000);
                }
            },1000);
        }

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("explode", event);

    }

    @Override
    public String help() {
        return null;
    }
}
