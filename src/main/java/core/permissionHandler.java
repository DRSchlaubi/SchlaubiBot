package core;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.STATIC;

import java.util.Arrays;


public class permissionHandler {

    public static Boolean check(MessageReceivedEvent event){
        User a = event.getAuthor();
        for(Role r : event.getGuild().getMember(event.getAuthor()).getRoles()){
            if(Arrays.stream(STATIC.PERMS).parallel().anyMatch(r.getName()::contains))
                return false;


        }
        return true;
    }

    public static Boolean isOwner(MessageReceivedEvent event){
        User a = event.getAuthor();
        if(Arrays.stream(STATIC.OWNERS).parallel().anyMatch(a.getId()::contains)){
            return false;
        }
        return true;
    }



}
