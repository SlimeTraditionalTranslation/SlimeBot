package tsp.slimebot.command.discord;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.mini2Dx.gettext.GetText;
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
                        .appendDescription(GetText.tr("**Recipe**") + "\n")
                        .appendDescription(Utils.recipe(item) + "\n")
                        .appendDescription(Utils.recipeGrid(item))
                        .build()
                ).queue();
                return;
            }
        }

        event.getHook().editOriginalEmbeds(Utils.embed(event)
                .setTitle(GetText.tr("Invalid item."))
                .appendDescription(GetText.tr("That item does not exist!") + "\n")
                .appendDescription(GetText.tr("Example item: `backpack`"))
                .build()).queue();
    }
}
