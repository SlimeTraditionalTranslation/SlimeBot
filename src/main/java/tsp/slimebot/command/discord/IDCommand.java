package tsp.slimebot.command.discord;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
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

        String id = "";
        OptionMapping channelOption = event.getOption("channel");
        if (channelOption != null) {
            TextChannel textChannel = channelOption.getAsTextChannel();
            if (textChannel != null) {
                id = textChannel.getId();
            }
        } else {
            id = guild.getId();
        }

        event.getHook().editOriginalEmbeds(Utils.embed(event)
                .setAuthor("Retrieved ID")
                .appendDescription(Utils.wrap(id))
                .build()).queue();
    }

}
