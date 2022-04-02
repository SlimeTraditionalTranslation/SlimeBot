package tsp.slimebot.command.discord;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
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
                    .setAuthor("並不在一個 Discord 伺服器.")
                    .appendDescription("你必須在 Discord 伺服器中執行此指令!")
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
                .setAuthor("檢索到的 ID")
                .appendDescription(Utils.wrap(id))
                .build()).queue();
    }

}
