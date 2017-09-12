package commands;

import core.permissionHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.STATIC;
import util.embedSender;

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
        Guild guild = event.getGuild();
        Member member = guild.getMember(author);
        final MessageChannel channel = event.getChannel();
        final Message message = event.getMessage();
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
                                                    mymsg.editMessage("Message `" + args[0] + "` will explode in `1` secconds").complete();
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
                                            mymsg.editMessage("This message will explode in `1` secconds").complete();
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

        System.out.println("[INFO] Command '" + STATIC.prefix + "explode' was executed by " + event.getAuthor().getName());



    }

    @Override
    public String help() {
        return null;
    }
}
