package tsp.slimebot.command.discord;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.bukkit.ChatColor;
import org.mini2Dx.gettext.GetText;
import tsp.slimebot.util.Utils;

public class ItemCommand extends SlimeCommand {

    public ItemCommand() {
      super("item");
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        String rawItem = event.getOption("name").getAsString();

        for (SlimefunItem item : Utils.getItems()) {
            String name = Utils.stripColor(item.getItemName());
            if (Utils.matches(rawItem, item)) {
                String wiki = item.getWikipage().isPresent() ? item.getWikipage().get() : "";

                event.getHook().editOriginalEmbeds(Utils.embed(event)
                        .setAuthor(name, !wiki.isEmpty() ? wiki : "https://github.com/Slimefun/Slimefun4/wiki")
                        .appendDescription(GetText.tr("**Information**") + "\n")
                        .appendDescription(Utils.information(item))
                        .appendDescription("\n")
                        .appendDescription(GetText.tr("**Description**") + "\n")
                        .appendDescription(Utils.description(item))
                        .appendDescription("\n")
                        .appendDescription(GetText.tr("**Category**") + "\n")
                        .appendDescription(Utils.category(item.getItemGroup()))
                        .appendDescription("\n")
                        .appendDescription(GetText.tr("**Research**") + "\n")
                        .appendDescription(Utils.research(item.getResearch()))
                        .appendDescription("\n")
                        .appendDescription(GetText.tr("**Recipe**") + "\n")
                        .appendDescription(Utils.recipe(item) + "\n")
                        .appendDescription(Utils.recipeGrid(item))
                        .build()
                ).queue();
                return;
            }
        }

        event.getHook().editOriginalEmbeds(Utils.embed(event)
                .setAuthor(GetText.tr("Invalid item."))
                .appendDescription(GetText.tr("Could not find item: ") + Utils.wrap(rawItem))
                .build()).queue();
    }
}
