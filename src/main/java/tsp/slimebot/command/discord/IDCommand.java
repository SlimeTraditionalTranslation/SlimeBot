package tsp.slimebot.command.discord;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.mini2Dx.gettext.GetText;
import tsp.slimebot.util.Utils;

/**
 * Convenience command for getting the id of a {@link Guild}.
 */
public class IDCommand extends SlimeCommand {

    public IDCommand() {
        super("getid");
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        if (guild == null) {
            event.getHook().editOriginalEmbeds(Utils.embed(event)
                    .setAuthor(GetText.tr("Not in a guild."))
                    .appendDescription(GetText.tr("You must run this command in a guild!"))
                    .build()).queue();
            return;
        }

        String id = "";
        OptionMapping channelOption = event.getOption("channel");
        if (channelOption != null) {
            ChannelType chType = channelOption.getAsChannel().getType();
            if (chType == ChannelType.TEXT) {
                TextChannel textChannel = (TextChannel) channelOption.getAsChannel();
                id = textChannel.getId();
            } else if (chType == ChannelType.CATEGORY) {
                Category category = channelOption.getAsChannel().asCategory();
                id = category.getId();
            } else {
                id = GetText.tr("Not a text channel!");
            }
        } else {
            id = guild.getId();
        }

        event.getHook().editOriginalEmbeds(Utils.embed(event)
                .setAuthor(GetText.tr("Retrieved ID"))
                .appendDescription(Utils.wrap(id))
                .build()).queue();
    }

}
