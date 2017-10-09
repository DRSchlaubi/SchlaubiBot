package net.schlaubi.schlaubibot.core;





import net.schlaubi.schlaubibot.commands.*;
import net.schlaubi.schlaubibot.listeners.*;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import net.schlaubi.schlaubibot.util.MySQL;
import net.schlaubi.schlaubibot.util.SECRETS;
import net.schlaubi.schlaubibot.util.STATIC;

import javax.security.auth.login.LoginException;
import java.io.File;


public class Main {

    public static JDABuilder builder;

    public static void main(String[] args){
        System.out.println("[SchlaubiBot] Starting bot...");
        if(!new File("secrets.json").exists())
            Configuration.create_config();
            MySQL.connect();
            JDABuilder builder = new JDABuilder(AccountType.BOT);
            builder.setToken(SECRETS.token);
            builder.setAutoReconnect(true);
            builder.setGame(Game.of(STATIC.game));
            builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
            builder.addEventListener(new commandListener());
            builder.addEventListener(new GuildMemberJoinListener());
            builder.addEventListener(new GuildMemberLeaveListener());
            builder.addEventListener(new readyListener());
            builder.addEventListener(new ReactionListener());
            builder.addEventListener(new MentionListener());
            builder.addEventListener(new GuildJoinListener());
            addCommands();





        try {
            JDA jda = builder.buildBlocking();
        } catch (LoginException e) {
            System.out.println("INVALID KEY!!" );
            String token = Configuration.prompt("token");
            Configuration.set("token", token);
        } catch (InterruptedException | RateLimitedException e) {
            e.printStackTrace();
        }
        System.out.println("[SchlaubiBot] Bot started");
    }




    public static void addCommands(){

       commandHandler.registerCommand("ping", new commandPing());
       commandHandler.registerCommand("kys", new commandKYS());
       commandHandler.registerCommand("clear", new commandClear());
       commandHandler.registerCommand("rip", new commandRIP());
       commandHandler.registerCommand("version", new commandVersion());
       commandHandler.registerCommand("help", new commandHelp());
       commandHandler.registerCommand("kick", new commandKick());
       commandHandler.registerCommand("ban", new commandBan());
       commandHandler.registerCommand("explode", new commandExplode());
       commandHandler.registerCommand("vote", new commandVote());
       commandHandler.registerCommand("medal", new commandMedal());
       commandHandler.registerCommand("status", new commandStatus());
       commandHandler.registerCommand("short", new commandShort());
       commandHandler.registerCommand("roles", new commandRoles());
       commandHandler.registerCommand("lmgtfy", new commandlmgtfy());
       commandHandler.registerCommand("userid", new commandUserid());
       commandHandler.registerCommand("servers", new commandServers());
       commandHandler.registerCommand("kms", new commandKMS());
       commandHandler.registerCommand("mute", new commandMute());
       commandHandler.registerCommand("unmute", new commandUnmute());
       commandHandler.registerCommand("serverinfo", new commandServerInfo());
       commandHandler.registerCommand("userinfo", new commandUserInfo());
       commandHandler.registerCommand("music", new Music());
       commandHandler.registerCommand("m", new Music());
       commandHandler.registerCommand("addrole", new commandAddrole());
       commandHandler.registerCommand("removerole", new commandRemoverole());
       commandHandler.registerCommand("settings", new commandSettings());
       commandHandler.registerCommand("terminate", new commandTerminate());
       commandHandler.registerCommand("restart", new commandRestart());
       commandHandler.registerCommand("gif", new commandGif());
       commandHandler.registerCommand("speedtest", new commandSpeedtest());
       commandHandler.registerCommand("bug", new commandBug());

    }





}
