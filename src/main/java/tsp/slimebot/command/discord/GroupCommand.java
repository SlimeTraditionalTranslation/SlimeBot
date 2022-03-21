package tsp.slimebot.command.discord;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import tsp.slimebot.util.Utils;

import java.util.List;
import java.util.Optional;

public class GroupCommand implements SlimeCommand {

    @Override
    public String getName() {
        return "group";
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        String name = event.getOption("name").getAsString();
        List<ItemGroup> groups = Slimefun.getRegistry().getAllItemGroups();
        Optional<ItemGroup> group = groups.stream()
                .filter(g -> g.getUnlocalizedName().equalsIgnoreCase(name))
                .findFirst();

        String result = "無.";
        if (group.isPresent()) {
            result = Utils.category(group.get());
        }

        event.getHook().editOriginalEmbeds(Utils.embed(event)
                .setAuthor(name)
                .appendDescription(result)
                .build()).queue();
    }
}
