package tsp.slimebot.command.discord;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import tsp.slimebot.util.Utils;

public class RecipeCommand extends SlimeCommand {

    public RecipeCommand() {
      super("recipe");
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
                        .appendDescription("**配方**" + "\n")
                        .appendDescription(Utils.recipe(item) + "\n")
                        .appendDescription(Utils.recipeGrid(item))
                        .build()
                ).queue();
                return;
            }
        }

        event.getHook().editOriginalEmbeds(Utils.embed(event)
                .setTitle("未知物品.")
                .appendDescription("這個物品並不存在!" + "\n")
                .appendDescription("範例物品: `背包`")
                .build()).queue();
    }
}
