package net.schlaubi.schlaubibot.core;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.util.STATIC;

import java.util.Arrays;


public class permissionHandler {

    public static Boolean check(MessageReceivedEvent event){
        for(Role r : event.getGuild().getMember(event.getAuthor()).getRoles()){
            if(Arrays.stream(STATIC.PERMS).parallel().anyMatch(r.getName()::contains))
                return false;
        }
        return true;
    }

    public static Boolean isOwner(User user){
        User a = user;
        if(Arrays.stream(STATIC.OWNERS).parallel().anyMatch(a.getId()::contains)){
            return false;
        }
        return true;
    }

    public static String getPermissonIcon(int permlvl){
        if(permlvl == 0){
            return ":white_small_square: ";
        } else if(permlvl == 1){
            return ":small_blue_diamond: ";
        } else if(permlvl == 2){
            return ":large_blue_diamond: ";
        } else if(permlvl == 3){
            return ":small_red_triangle: ";
        }
        return null;
    }



}
