package tsp.slimebot.command.discord;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@FunctionalInterface
public interface CommandEvent {

    void onCommand(SlashCommandInteractionEvent event);

}
