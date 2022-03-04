package tsp.slimebot.command.discord;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * An interface for discord commands
 */
public interface SlimeCommand {
    
    String getName();

    default Permission getPermission() {
        return Permission.MESSAGE_SEND;
    }

    void onCommand(SlashCommandInteractionEvent event);
    
}
