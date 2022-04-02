package tsp.slimebot.command.discord;

import io.github.thebusybiscuit.slimefun4.api.items.groups.FlexItemGroup;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import tsp.slimebot.util.Utils;

import java.util.stream.Collectors;

public class GroupListCommand extends SlimeCommand {

    public GroupListCommand() {
        super("groups");
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        OptionMapping pageOption = event.getOption("page");
        int page = 1;
        if (pageOption != null) {
            page = (int) pageOption.getAsLong();
        }
        String result = Utils.asString(Utils.getPage(Slimefun.getRegistry().getAllItemGroups().stream()
                .filter(group -> !(group instanceof FlexItemGroup))
                .collect(Collectors.toList()), page, 10));

        event.getHook().editOriginalEmbeds(Utils.embed(event)
                .setAuthor("組別 | 頁面: " + page)
                .appendDescription(result)
                .build()).queue();
    }

}
