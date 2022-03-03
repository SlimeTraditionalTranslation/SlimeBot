package tsp.slimebot.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import tsp.slimebot.util.Utils;

/**
 * Convenience command for getting the id of a {@link Guild}.
 */
public class IDCommand implements SlimeCommand {

    @Override
    public String getName() {
        return "getid";
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        if (guild == null) {
            event.getHook().editOriginalEmbeds(Utils.embed(event)
                    .setAuthor("Not in a guild.")
                    .appendDescription("You must run this command in a guild!")
                    .build()).queue();
            return;
        }

        event.getHook().editOriginalEmbeds(Utils.embed(event)
                .setAuthor("Guild ID | " + guild.getName(), guild.getIconUrl())
                .appendDescription(Utils.wrap(guild.getId()))
                .build()).queue();
    }

}
