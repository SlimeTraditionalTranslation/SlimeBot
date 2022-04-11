package tsp.slimebot.listener.discord;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;
import tsp.slimebot.SlimeBot;
import tsp.slimebot.util.Log;

public class BotReadyListener extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Guild guild = event.getJDA().getGuildById(SlimeBot.getInstance().getConfig().getString("bot.guildId"));
        if (guild == null) {
            Log.error("Invalid guild id!");
            SlimeBot.getInstance().getBot().stopBot();
            return;
        }

        guild.upsertCommand("getid", "Retrieve an id.")
                .addOption(OptionType.CHANNEL, "channel", "Retrieve id of the text channel.")
                .queue();

        guild.upsertCommand("player", "Retrieve information about a player.")
                .addOption(OptionType.STRING, "name", "Player name", true)
                .queue();
        guild.upsertCommand("group", "Retrieve information about a group (category).")
                .addOption(OptionType.STRING, "name", "Group name", true)
                .queue();
        guild.upsertCommand("groups", "List all groups.")
                .addOption(OptionType.INTEGER, "page", "Page")
                .queue();
        guild.upsertCommand("item", "Retrieve detailed information about an item.")
                .addOption(OptionType.STRING, "name", "Item name", true)
                .queue();
        guild.upsertCommand("items", "List all items")
                .addOption(OptionType.STRING, "type", "ALL|ENABLED|DISABLED|RADIOACTIVE|a:ADDON|r:RESEARCH")
                .addOption(OptionType.INTEGER, "page", "Page")
                .queue();
        guild.upsertCommand("recipe", "Retrieve information about a recipe")
                .addOption(OptionType.STRING, "name", "Item name", true)
                .queue();
        guild.upsertCommand("research", "Retrieve information about a research")
                .addOption(OptionType.STRING, "name", "Research key", true)
                .queue();
        guild.upsertCommand("researches", "List all researches.")
                .addOption(OptionType.INTEGER, "page", "Page")
                .queue();
        guild.upsertCommand("addons", "List all installed addons.")
                .addOption(OptionType.INTEGER, "page", "Page")
                .addOption(OptionType.BOOLEAN, "detailed", "Detailed list")
                .queue();
    }
}
