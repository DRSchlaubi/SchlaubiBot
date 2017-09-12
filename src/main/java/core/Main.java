package core;




import commands.*;
import listeners.GuildMemberJoinListener;
import listeners.GuildMemberLeaveListener;
import listeners.commandListener;
import listeners.readyListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import util.SECRETS;
import util.STATIC;

import javax.security.auth.login.LoginException;



public class Main {

    public static void main(String[] Args){
        System.out.println("[SchlaubiBot] Starting bot...");
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(SECRETS.token);
        builder.setAutoReconnect(true);
        builder.setGame(Game.of(STATIC.game));
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        builder.addEventListener(new commandListener());
        builder.addEventListener(new GuildMemberJoinListener());
        builder.addEventListener(new GuildMemberLeaveListener());
        builder.addEventListener(new readyListener());
        addCommands();


        try {
            JDA jda = builder.buildBlocking();
        } catch (LoginException e) {
            System.out.println("INVALID KEY!!" );
        } catch (InterruptedException | RateLimitedException e) {
            e.printStackTrace();
        }
        System.out.println("[SchlaubiBot] Bot started");
    }



    public static void addCommands(){

       commandHandler.commands.put("ping", new commandPing());
       commandHandler.commands.put("kys", new commandKYS());
       commandHandler.commands.put("clear", new commandClear());
       commandHandler.commands.put("rip", new commandRIP());
       commandHandler.commands.put("version", new commandVersion());
       commandHandler.commands.put("help", new commandHelp());
       commandHandler.commands.put("kick", new commandKick());
       commandHandler.commands.put("ban", new commandBan());
       commandHandler.commands.put("explode", new commandExplode());
       commandHandler.commands.put("vote", new commandVote());
       commandHandler.commands.put("medal", new commandMedal());
       commandHandler.commands.put("status", new commandStatus());
       commandHandler.commands.put("short", new commandShort());
       commandHandler.commands.put("roles", new commandRoles());
       commandHandler.commands.put("lmgtfy", new commandlmgtfy());
       commandHandler.commands.put("userid", new commandUserid());
       commandHandler.commands.put("servers", new commandServers());
       commandHandler.commands.put("joinmessages", new commandJoinmessages());
    }

}
