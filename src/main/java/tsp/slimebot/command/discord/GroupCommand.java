package tsp.slimebot.command.discord;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.mini2Dx.gettext.GetText;
import tsp.slimebot.util.Utils;

import java.util.List;
import java.util.Optional;

public class GroupCommand extends SlimeCommand {

    public GroupCommand() {
        super("group");
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        String name = event.getOption("name").getAsString();
        List<ItemGroup> groups = Slimefun.getRegistry().getAllItemGroups();
        Optional<ItemGroup> group = groups.stream()
                .filter(g -> g.getUnlocalizedName().equalsIgnoreCase(name))
                .findFirst();

        String result = GetText.tr("None.");
        if (group.isPresent()) {
            result = Utils.category(group.get());
        }

        event.getHook().editOriginalEmbeds(Utils.embed(event)
                .setAuthor(name)
                .appendDescription(result)
                .build()).queue();
    }
}
