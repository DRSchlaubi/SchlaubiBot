package listeners;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;



public class readyListener extends ListenerAdapter {


    @Override
    public void onReady(ReadyEvent event){
        String out = "\n This bot is running on the following servers: \n";

        for (Guild g : event.getJDA().getGuilds()){
            out += g.getName() + " (" + g.getId() + ")  \n";
        }

        System.out.println(out);

        System.out.println("[SchlaubiBot] Loading Polls ...");


        commands.commandVote.loadPolls(event.getJDA());

        /*for(Guild guild : event.getJDA().getGuilds()){
            MessageChannel channel = guild.getDefaultChannel();
            channel.sendTyping().queue();
            Message mymsg = channel.sendMessage("Hey i am back again").complete();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    mymsg.delete().queue();
                }
            },5000);
        }*/



    }
}
