package tsp.slimebot.command.discord;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.bukkit.ChatColor;
import tsp.slimebot.util.Utils;

public class ItemCommand implements SlimeCommand {

    @Override
    public String getName() {
        return "item";
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        String rawItem = event.getOption("name").getAsString();

        for (SlimefunItem item : Utils.getItems()) {
            String name = ChatColor.stripColor(item.getItemName());
            if (Utils.matches(rawItem, item)) {
                String wiki = item.getWikipage().isPresent() ? item.getWikipage().get() : "";

                event.getHook().editOriginalEmbeds(Utils.embed(event)
                        .setAuthor(name, !wiki.isEmpty() ? wiki : "https://github.com/Slimefun/Slimefun4/wiki")
                        .appendDescription("**資訊**" + "\n")
                        .appendDescription(Utils.information(item))
                        .appendDescription("\n")
                        .appendDescription("**介紹**" + "\n")
                        .appendDescription(Utils.description(item))
                        .appendDescription("\n")
                        .appendDescription("**類別**\n")
                        .appendDescription(Utils.category(item.getItemGroup()))
                        .appendDescription("\n")
                        .appendDescription("**研究**\n")
                        .appendDescription(Utils.research(item.getResearch()))
                        .appendDescription("\n")
                        .appendDescription("**配方**" + "\n")
                        .appendDescription(Utils.recipe(item) + "\n")
                        .appendDescription(Utils.recipeGrid(item))
                        .build()
                ).queue();
                return;
            }
        }
    }
}
